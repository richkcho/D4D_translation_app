<?php
// Strings
$conversationdata_conversationidlabel = 'ConversationID: ';
$conversationdata_languagelabel = 'LanguageIDs: ';
$conversationdata_categoryidlabel = 'CategoryID: ';
$conversationdata_categorylabel = 'Category: ';
$conversationdata_descriptionlabel = 'Description: ';
$conversationdata_splitstring = "\n";

$conversationtree_languagelabel = 'LanguageIDs: ';
$conversationtree_conversationlabel = 'Conversation: ';
$conversationtree_split = "\n";

$language_split = ',';

$conversationtreenode_split = '::';
$conversationtreenode_pre = '(';
$conversationtreenode_post = ')';

$statementpair_split = ' / ';
$statementpair_pre = '[';
$statementpair_post = ']';

$statement_split = ' ';

$arraylist_split = ', ';

$nodata = 'NULL';

// functions

// complies to interface. expects supported_languages to be comma separated integers
function getConversationData($db, $category_id, $supported_languages)
{
	global $arraylist_split, 
		$conversationdata_conversationidlabel,
		$conversationdata_languagelabel,
		$conversationdata_categorylabel, 
		$conversationdata_descriptionlabel, 
		$conversationdata_splitstring,
        $conversationdata_categoryidlabel;
	// ensure category_id is integer
	$category_id = (int)$category_id;
	
	$langarr = strToIntArr($supported_languages);
	
	// fetch rows in database with same category, if -1 use wildcard '%'
	$catid = ($category_id == -1 ? "'%'" : "'$category_id'");
	$resultset = $db->query("SELECT * FROM conversation_data WHERE category LIKE $catid");
	
	$res = '[';
	
	for($temp = 0; $temp < $resultset->num_rows; $temp++)
	{
		$result = $resultset->fetch_array();
		
		if($supported_languages != '' && issubset($langarr, strToIntArr($result['supported_languages'])))
		{
			// add the separator
			if($temp > 0)
			{
				$res .= $arraylist_split;
			}
			
			// add the statement
			$res .= '(' . $conversationdata_conversationidlabel . $result['conversation_id'] . $conversationdata_splitstring;
			$res .= $conversationdata_languagelabel . $result['supported_languages'] . $conversationdata_splitstring;
            $res .= $conversationdata_categoryidlabel . $result['category'] . $conversationdata_splitstring;
			$res .= $conversationdata_categorylabel . getTranslationString($db, $result['category']) . $conversationdata_splitstring;
			$res .= $conversationdata_descriptionlabel . getTranslationString($db, $result['description']) . ')';
			
		}
	}
	
	return $res . ']';
}

// given conversation_id and languages, returns string form of a conversation
function getConversation($db, $conversation_id, $language1, $language2)
{
	global $conversationtree_languagelabel, $conversationtree_conversationlabel, $conversationtree_split, $language_split;
	// make sure inputs are integers
	$conversation_id = (int)$conversation_id;
	$language1 = (int)$language1;
	$language2 = (int)$language2;
	
	$conversationstring = $conversationtree_languagelabel . $language1 . $language_split . $language2 . $conversationtree_split;
	
	// check if valid Conversation parameters in ConversationData
	if(validConversationParameters($db, $conversation_id, $language1, $language2))
	{
		$resultset = $db->query("SELECT * FROM statements WHERE conversation_id=$conversation_id AND parent_statement_id=-1 LIMIT 1");
		$conversationstring .= $conversationtree_conversationlabel . build($db, $resultset->fetch_array(), $language1, $language2);
		
		if($conversationstring != NULL)
		{
			return  $conversationstring;
		}
	}
	
	return NULL;
}

function build($db, $row, $language1, $language2)
{
	global $conversationtreenode_pre, $conversationtreenode_post, $conversationtreenode_split;
	
	$res = $conversationtreenode_pre;
	$sp = getStatementPair($db, $row['translation_id'], $language1, $language2);
	
	if($sp == NULL)
	{
		return NULL;
	}
	
	$res .= $sp;
	
	$children = $db->query('SELECT * FROM statements WHERE conversation_id='. $row['conversation_id'] . ' AND parent_statement_id=' . $row['statement_id']);
	
	for($temp = 0; $temp < $children->num_rows; $temp++)
	{
		$child = build($db, $children->fetch_array(), $language1, $language2);
		
		if($child == NULL)
		{
			return NULL;
		}
		
		$res .= $conversationtreenode_split . $child;
	}
	
	return $res . $conversationtreenode_post;
}

/* gets a StatementPair string based on id and language pair
 * returns a valid StatementPair string if it exists otherwise returns NULL
*/
function getStatementPair($db, $translation_id, $language1, $language2)
{
	global $statementpair_pre, $statementpair_post, $statementpair_split, $statement_split;
	// make sure inputs are integers
	$translation_id = (int)$translation_id;
	$language1 = (int)$language1;
	$language2 = (int)$language2;

	// fetch row from database
	$row = mysqli_query($db, "SELECT translation FROM translations WHERE translation_id=$translation_id LIMIT 1");

	// make sure we actually got something, else return NULL
	if(mysqli_num_rows($row) == 0)
	{
		return NULL;
	}

	// build the StatementPair string
	$part1 = NULL;
	$part2 = NULL;
	$trans = explode($statementpair_split, mysqli_fetch_array($row)['translation']);

	foreach($trans as $statement)
	{
		if(substr($statement, 0, strpos($statement, $statement_split)) == $language1)
		{
			$part1 = $statement;
		}
		elseif(substr($statement, 0, strpos($statement, $statement_split)) == $language2)
		{
			$part2 = $statement;
		}
	}
	
	// if we got both parts return it, else return NULL
	if($part1 != NULL && $part2 != NULL)
	{
		return $statementpair_pre . $part1 . $statementpair_split . $part2 . $statementpair_post;
	}
	else
	{
		return NULL;
	}
}

function getTranslationString($db, $translation_id)
{
	$res = $db->query("SELECT translation FROM translations WHERE translation_id=$translation_id LIMIT 1");
	
	if($res->num_rows == 1)
	{
		return $res->fetch_array()['translation'];
	}
}

function validConversationParameters($db, $conversation_id, $language1, $language2)
{
	global $language_split;
	
	$row = mysqli_query($db, "SELECT supported_languages FROM conversation_data WHERE conversation_id=$conversation_id LIMIT 1");
	if(mysqli_num_rows($row) == 0)
	{
		return false;
	}
	
	$languages = explode($language_split, mysqli_fetch_array($row)['supported_languages']);
	
	return (exists($language1, $languages) && exists($language2, $languages));
}

// tests if these variables exist in $_POST
function allset($params)
{
	foreach($params as $temp)
	{
		if(!isset($_POST[$temp]))
		{
			return false;
		}
	}
	
	return true;
}

function issubset($set, $supset)
{
	foreach($set as $elem)
	{
		if(!exists($elem, $supset))
		{
			return false;
		}
	}
	
	return true;
}

// tests it $elem is in set $arr
function exists($elem, $arr)
{
	foreach($arr as $temp)
	{
		if($elem == $temp)
		{
			return true;
		}
	}
	
	return false;
}

// breaks the language string returned from the database into an array of integers
function strToIntArr($str)
{
	global $language_split;
	
	$arr = explode($language_split, $str);
	
	for($temp = 0, $length = count($arr); $temp < $length; $temp++)
	{
		$arr[$temp] = (int)$arr[$temp];
	}
	
	return $arr;
}

// main script
if($_SERVER['REQUEST_METHOD'] == 'POST')
{
	// filename of file that contains connection info
	$filename = 'dbconnect.txt';	

	// open file with database connection info
	$connectfile = fopen($filename, 'r');
	
	// ensure that connectfile actually opened
	if($connectfile != 0)
	{
		// read info from file
		$connectinfo = array();
		while(!feof($connectfile))
		{
			$connectinfo[] = rtrim(fgets($connectfile));
		}

		// ensure enough data has been read from file
		if(count($connectinfo) >= 3)
		{
		
			// Connect to MySql
			$db =  mysqli_connect("localhost", $connectinfo[0], $connectinfo[1], $connectinfo[2]);
			
			// Check connection
			if(mysqli_connect_errno())
			{
				echo 'Failed to connect to MySQL: ' . mysqli_connect_error();
			}
			else
			{
				// now actually do stuff
				if(isset($_POST['method_name']))
				{
					if($_POST['method_name'] == 'getConversation')
					{
						$paramNames = array('conversation_id', 'language1', 'language2');
						if(allset($paramNames))
						{
							$res = getConversation($db, $_POST[$paramNames[0]], $_POST[$paramNames[1]], $_POST[$paramNames[2]]);
							if($res != NULL)
							{
								echo $res;
							}
							else
							{
								echo $nodata;
							}
						}
					}
					elseif($_POST['method_name'] == 'getConversationData')
					{
						$paramNames = array('category_id', 'supported_languages');
						if(allset($paramNames))
						{
							$res = getConversationData($db, $_POST[$paramNames[0]], $_POST[$paramNames[1]]);
							
							if($res != '[]')
							{
								echo $res;
							}
							else
							{
								echo $nodata;
							}
						}
					}
				}

				// close the database connection
				$db->close(); 
			}
		}
		else
		{
			echo "Not enough information in $filename\n";
		}
	
		fclose($connectfile);
	}
	else
	{
		echo "Database connection file $filename  not found\n";
	}
}
else
{
	$filename = 'dbconnect.txt';
	$connectfile = fopen($filename, 'r');
	$connectinfo = array();
	while(!feof($connectfile))
	{
		$connectinfo[] = rtrim(fgets($connectfile));
	}
	$db =  mysqli_connect("localhost", $connectinfo[0], $connectinfo[1], $connectinfo[2]);
	
	echo print_r(explode("asd", ""), true);
	echo '<br />';
	echo print_r(strToIntArr("WOLOLOL, 2"), true);
	echo '<br />';
	echo getConversation($db, 1, 1, 2);
	echo '<br />';
	echo getConversationData($db, -1, '1');
}
?>