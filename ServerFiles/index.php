<!DOCTYPE html>
<html>
<body>

<?php
// Strings
$conversationtree_languagelabel = 'LanguageIDs: ';
$conversationtree_conversationlabel = 'Conversation: ';
$conversationtree_split = "\n";

$language_split = ',';

$conversationtreenode_split = ',';
$conversationtreenode_pre = '(';
$conversationtreenode_post = ')';

$statementpair_split = ' / ';
$statementpair_pre = '[';
$statementpair_post = ']';

$statement_split = ' ';

// functions

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
		$conversationstring .= $conversationtree_conversationlabel . build($db, mysqli_fetch_array($resultset), $language1, $language2);
		
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
	
	if(($numchildren = $children->num_rows) != 0)
	{
		for($temp = 0; $temp < $numchildren; $temp++)
		{
			$child = build($db, $children->fetch_array(), $language1, $language2);
			
			if($child == NULL)
			{
				return NULL;
			}
			
			$res .= $conversationtreenode_split . $child;
		}
	}
	
	return $res . $conversationtreenode_post;
}

/* gets a StatementPair string based on id and language pair
 * returns a valid StatementPair string if it exists otherwise returns NULL
*/
function getStatementPair($db, $translation_id, $language1, $language2)
{
	// make sure inputs are integers
	$translation_id = (int)$translation_id;
	$language1 = (int)$language1;
	$language2 = (int)$language2;

	// get global variables
	global $statementpair_pre, $statementpair_post, $statementpair_split, $statement_split;

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

function validConversationParameters($db, $conversation_id, $language1, $language2)
{
	// get global variables
	global $language_split;
	
	$row = mysqli_query($db, "SELECT supported_languages FROM conversation_data WHERE conversation_id=$conversation_id LIMIT 1");
	if(mysqli_num_rows($row) == 0)
	{
		return false;
	}
	
	$languages = explode($language_split, mysqli_fetch_array($row)['supported_languages']);
	
	return (exists($language1, $languages) && exists($language2, $languages));
}

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

// main script
if($_SERVER['REQUEST_METHOD'] == 'POST' || true)
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
				echo 'Successfully connected to database';
				echo '<br />';
				echo getConversation($db, 1, 1, 2);

				// close the database connection
				mysqli_close($db); 
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
?>

</body>
</html>
