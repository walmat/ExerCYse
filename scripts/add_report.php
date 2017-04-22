<?php
	require("dbConnect.php");

	//get posted variables
	$equipment = $_POST["equipment"];
	$floor = $_POST["floor"];
	$notes = $_POST["notes"];

	//create query
	$query = "INSERT INTO reports (equipment, floor, notes) VALUES ('$equipment', '$floor', '$notes')";

	//create response array
	$response = array();
	$response["success"] = false;

	//execute query and check if successful
	if($connect->query($query)){
		$response["success"] = true;
	}
	
	echo json_encode($response);
	$connect->close();
?>
