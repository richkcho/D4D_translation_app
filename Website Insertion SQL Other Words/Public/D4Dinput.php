<?php

/* Check if it was a POST request */
if ($_SERVER["REQUEST_METHOD"] != "POST") {
	header("Location: http://cs50events.com/public/d4d.php");
	die();
	}
?>

<html lang="en">

<head>
	<title>D4D Translation App Database Output Log</title>
	<base target="_blank">
	<meta charset="UTF-8">
	<meta name="description" content="Logs the insertions into the SQL database.">
</head>

<body>

<?php

/* Start of actual stuff */
/* Check that language1 != language2 */
if ($_POST["language1"] == $_POST["language2"]) {
	printf("Error: Translated languages are the same\n");
	exit();
	}

$mysqli = new mysqli("d4d.cs50events.com", "cs50events", "davidmalansucks", "cs50events_db");

/* Check connection */
if (mysqli_connect_errno()) {
	printf("Connect failed: %s\n", mysqli_connect_error());
	exit();
	}

/* Validate that the languages are valid languages in the table */
if ($stmt = $mysqli->prepare("SELECT COUNT(language) FROM languages WHERE language = ?")) {

	/* Validates language1 */
	$stmt->bind_param('s', $_POST["language1"]);
	$stmt->execute();
	$stmt->bind_result($exists);
	$stmt->fetch();
	$language1 = htmlspecialchars($_POST["language1"]);
	if ($exists) {
		printf("%s exists in the database\n", $language1);
	} else if ($exists == 0) {
		printf("Error: %s doesn't exist in the database\n", $language1);
		exit();
	} else {
		printf("Error: $s exists %d times in the database\n", $language1, $exists);
		exit();
		}
	
	/* Validates language2 */
	$stmt->bind_param('s', $_POST["language2"]);
	$stmt->execute();
	$stmt->bind_result($exists);
	$stmt->fetch();
	$language2 = htmlspecialchars($_POST["language2"]);
	if ($exists) {
		printf("%s exists in the database\n", $language2);
	} else if ($exists == 0) {
		printf("Error: %s doesn't exist in the database\n", $language2);
		exit();
	} else {
		printf("Error: %s exists %d times in the database\n", $language2, $exists);
		exit();
		}
	$stmt->close();
	} else {
		/* Error */
		printf("Prepared Statement Error: %s\n", $mysqli->error);
		exit();
}

if ($stmt = $mysqli->prepare("INSERT INTO conversation_data (supported_languages, description) VALUES (?, ?)")) {
	/* Concatenate the languages */
	$languages = $language1 . " " . $language2;
	$description = htmlspecialchars($_POST["description"]);
	$stmt->bind_param('ss', $languages, $_POST["description"]);
	$stmt->execute();
	printf("Inserted %s into supported_languages and %s into description", $languages, $description);
	$stmt->close();
	
	/* Grabs and prints the last id */
	$conversation_id = $mysqli->insert_id;
	printf("<br> The conversation_id is %d<br>", $conversation_id);
	} else {
	printf("Prepared Statement Error %s\n", $mysqli->error);
	exit();
}

function input_tree($curr_level, $parent_statement_id, $new, $conversation_id) {
	if (($_POST[$curr_level . "_a"] != "") && ($_POST[$curr_level . "_b"] != "")) {
		$mysqli = new mysqli("d4d.cs50events.com", "cs50events", "davidmalansucks", "cs50events_db");

		/* Check connection */
		if (mysqli_connect_errno()) {
			printf("Connect failed: %s\n", mysqli_connect_error());
			exit();
			}
		
		if ($stmt_translation = $mysqli->prepare("INSERT INTO translations (translation) VALUES (?)")) {
			$stmt_translation->bind_param('s', $_POST[$curr_level . "_a"]);
			$stmt_translation->execute();
			$printed_value = htmlspecialchars($_POST[$curr_level . "_a"]);
			$stmt_a_id = $mysqli->insert_id;
			printf("%s inserted into the translations table with id %d<br>", $printed_value, $stmt_a_id);
			
			$stmt_translation->bind_param('s', $_POST[$curr_level . "_b"]);
			$printed_value = htmlspecialchars($_POST[$curr_level . "_b"]);
			$stmt_translation->execute();
			$stmt_b_id = $mysqli->insert_id;
			printf("%s inserted into the translations table with id %d<br>", $printed_value, $stmt_b_id);
			
			$stmt_translation->close();
		} else {
			printf("Prepared Statement Error %s<br>", $mysqli->error);
			exit();
		}
			
		if ($stmt = $mysqli->prepare("INSERT INTO statements (statement_id, parent_statement_id, conversation_id, translation_id) VALUES (?, ?, ?, ?)")) {
			$stmt->bind_param('iiii', $stmt_a_id, $parent_statement_id, $conversation_id, $stmt_b_id);
			$stmt->execute();
			
			/* Attempt to dynamically generate new parent statement id name */
			${"parent_" . $new} = $mysqli->insert_id;
			printf("%d was inserted into statements with parent id: %d, conversation id: %d, and translation id: %d. The new parent statement id is %d.<br>", $stmt_a_id, $parent_statement_id, $conversation_id, $stmt_b_id, ${"parent_" . $new});
			
			$stmt->close();
		} else {
			printf("Prepared Statement Error %s<br>", $mysqli->error);
			exit();
			}
		
		input_tree($curr_level . "_1", ${"parent_" . $new}, $new . "_1", $conversation_id);
		
		/* iterate through siblings */
		$last_level = substr($curr_level, -1);
		$new_last_level = $last_level + 1;
		$new_level = substr($curr_level, 0, -1) . $new_last_level;
		
		input_tree($new_level, $parent_statement_id, $new . "_1", $conversation_id);
		}
	}

input_tree("level_1", -1, "_1", $conversation_id);
echo("<br>Done!");

?>

</body>

</html>