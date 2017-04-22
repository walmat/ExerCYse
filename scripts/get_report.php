<?php
	require("dbConnect.php");
	
	//Defining query and executing it.
	$query = "SELECT * FROM reports";
	$result = $connect->query($query);
	
	//Array and int for stepping through each row returned by the query.
	$response = array();
	$i = 0;
	
	//if the query comes back with results
	if($result->num_rows > 0) {
		$response["empty"] = "false";

		//Looping through rows retuned by query.
		while ($row = $result->fetch_assoc()) {		
			$response["e" . $i] = $row["equipment"];
			$response["f" . $i] = $row["floor"];
			$response["n" . $i] = $row["notes"];
			$i++;
		}
	} else 	{
		$response["empty"] = "true";
	}

	echo json_encode($response);
?>
