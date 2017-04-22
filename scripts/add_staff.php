<?php
	require("dbConnect.php");
	
	$email = $_POST["email"];
	$response = array();
	$query = "SELECT * FROM users WHERE email = '$email'";
	$result = $connect->query($query);

	if($result->num_rows > 0) {
		$response["success"] = "true";
		
		//get row and id
		$row = $result->fetch_assoc();
		$id = $row["id"];
		
		//update query
		$query = "UPDATE users SET type = 2 WHERE id = $id";
		if(!$connect->query($query)) {
			$response["success"] = "false no query";
		}
		
		
	} else {
		$response["success"] = "false";
	}

	
	echo json_encode($response);
?>
