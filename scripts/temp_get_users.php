<?php
	require("dbConnect.php");
	
	$query = "SELECT * FROM users";
	$result = $connect->query($query);
	
	$response = array();
	$i = 0;
	
	if($result->num_rows > 0) {
		$response["empty"] = "false";
		
		while($row = $result->fetch_assoc()) {
			//respond with "first name, last name, email, type"
			$response["fn" . $i] = $row["firstname"];
			$response["ln" . $i] = $row["lastname"];
			$response["em" . $i] = $row["email"];
			$response["tp" . $i] = $row["type"];
			$i++;
		}
		
	} else {
		$response["empty"] = "true";
	}
	
	echo json_encode($response);

?>
