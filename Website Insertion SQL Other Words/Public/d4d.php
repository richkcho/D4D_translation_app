<html lang="en">

<head>
	<title>D4D Translation App Database Input Form</title>
	<base target="_blank">
	<link rel="stylesheet" type="text/css" href="style.css">
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script type="text/javascript" src="../includes/form.js"></script>
	<meta charset="UTF-8">
	<meta name="description" content="Form to input translation trees into the D4D app database">
</head>

<body>
	<section class="main">
		<div class="name_placeholder">
			<p>Despite not being able to delete leaves and branches, if you leave them blank it doesn't add erroneous entries into the database
			<br>Additionally, all statements should be prepared. Languages are validated. Everything displayed should be escaped. Attempt some injects I guess.</p>
			
			<form action="D4Dinput.php" method="post">
				<div id="languages">
					<?php
						/* This query uses mysqli in favor of the depreciated mysql commands */
						$mysqli = new mysqli("d4d.cs50events.com", "cs50events", "davidmalansucks", "cs50events_db");
						if ($mysqli -> connect_errno) {
							printf("Connection to mySQL failed. Error: %s\n", $mysqli>connect_error);
							exit();
							}

						$query = "SELECT language FROM languages";
						echo "<select name='language1'>";
						echo "<option selected disabled>Choose first language</option>";
						if ($result = $mysqli->query($query)) {
							while ($row = $result->fetch_row()) {
								echo "<option value='" . htmlspecialchars($row[0]) . "'>" . htmlspecialchars($row[0]) . "</option>";
								}
							echo "</select>";
							}

						echo "<select name='language2'>";
						echo "<option selected disabled>Choose second language</option>";
						if ($result = $mysqli->query($query)) {
							while ($row = $result->fetch_row()) {
								echo "<option value='" . htmlspecialchars($row[0]) . "'>" . htmlspecialchars($row[0]) . "</option>";
								}
							echo "</select>";
							}
					?>
				</div> <!-- Ends the languages div -->
				<div id="description">
				<input autocomplete="off" id="description" name="description" type="text" placeholder="Description of Conversation Tree"/>
				</div> <!-- Ends the description div -->

				<div id="addedElements">
				<input autocomplete="off" id="level_1_a" name="level_1_a" type="text" placeholder="Root Value A"/>
				<input autocomplete="off" id="level_1_b" name="level_1_b" type="text" placeholder="Root Value B"/>
				<button class="level_1" type="button">+Leaf</button>
				</div> <!-- Ends the addedElements div -->

				<div id="submitButton">
				<input type="submit">
				</div>

			</form>
		</div> <!-- Ends the name_placeholder div -->
	</section>
</body>
</html>