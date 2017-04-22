<?php
	require("dbConnect.php");
	
	$toggle = $_POST["toggle"];
	$response = array();
	
	function changeType($email, $type) {
		global $connect, $response;
		
		$id = getUser($email);
		$response["success"] = true;
		if($id == "no user") {
			$response["error"] = "Not a valid email address";
			$response["success"] = false;
		}
		else {
			$query = "UPDATE users SET type = $type WHERE id = $id";
			if(!$connect->query($query)) {
				$response["error"] = "Update failed";
				$response["success"] = false;
			}
		}
		
	}
		
	function getUser($email) {
		global $connect;
		
		$query = "SELECT * FROM users WHERE email = '$email'";
		$result = $connect->query($query);
		
		if($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			return $row["id"];
		}
		else {
			return "no user";
		}
	}
	
	function getAllUsers() {
		global $connect, $response;
		
		$query = "SELECT * FROM users";
		$result = $connect->query($query);
		
		$i = 0;
		while($row = $result->fetch_assoc()) {
			$response["fn" . $i] = $row["firstname"];
			$response["ln" . $i] = $row["lastname"];
			$response["em" . $i] = $row["email"];
			$response["tp" . $i] = $row["type"];
			$i++;
		}
	}
	
	switch ($toggle) {
		case "addStaff":
			$email = $_POST["email"];
			changeType($email, 2);
			break;
		case "addEmployer":
			$email = $_POST["email"];
			changeType($email, 3);
			break;
		case "removeStaffEmployer":
			$email = $_POST["email"];
			changeType($email, 1);
			break;
		case "getStaffEmployer":
			getAllUsers();
			break;		
	}
	
	echo json_encode($response);
?>
