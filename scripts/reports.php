<?php
	require("dbConnect.php");
	
	$toggle = $_POST["toggle"];	
	$response = array();
	
	function getActiveReports() {
		global $response, $connect;

		$query = "SELECT * FROM reports WHERE accepted = 1";
		
		$result = $connect->query($query);
		$i = 0;
		
		if($result->num_rows > 0) {
			while ($row = $result->fetch_assoc()) {
				$response["e" . $i] = $row["equipment"];
				$response["f" . $i] = $row["floor"];
				$response["n" . $i] = $row["notes"];
				$response["uID" . $i] = $row["uID"];
				$response["id" . $i] = $row["id"];
				$i++;
			}
			
			$response["count"] = $i + 1;
		} else {
			$response["error"] = "Query returned no results.";
		}
	}
	
	function getNewReports() {
		global $response, $connect;

		$query = "SELECT * FROM reports WHERE accepted = 0";
		
		$result = $connect->query($query);
		$i = 0;
		
		if($result->num_rows > 0) {
			while ($row = $result->fetch_assoc()) {
				$response["e" . $i] = $row["equipment"];
				$response["f" . $i] = $row["floor"];
				$response["n" . $i] = $row["notes"];
				$response["uID" . $i] = $row["uID"];
				$response["id" . $i] = $row["id"];
				$i++;
			}
			
			$response["count"] = $i + 1;
		} else {
			$response["error"] = "Query returned no results.";
		}
	}
	
	function addReport($equipment, $floor, $notes) {
		global $response, $connect;
		
		$query = "INSERT INTO reports (equipment, floor, notes) VALUES ('$equipment', '$floor', '$notes')";
		
		$response["success"] = false;
		if($connect->query($query)) {
			$response["success"] = true;
		} else {
			$response["error"] = "Report could not be added.";
		}
	}
	
	function removeReport($id) {
		global $response, $connect;
		
		$query = "DELETE FROM reports WHERE id = $id";
		
		$response["success"] = false;
		if($connect->query($query)) {
			$response["success"] = true;
		} else {
			$response["error"] = "Report could not be removed.";
		}
	}
	
	function acceptReport($id) {
		global $response, $connect;
		
		$query = "UPDATE reports SET accepted = 1 where id = $id";
	
		$response["success"] = false;	
		if($connect->query($query)) {
			$response["success"] = true;
		} else {
			$response["error"] = "Report could not be accepted.";
		}
	}
	
	switch ($toggle) {
		case "activeReports":
			getActiveReports();
			break;
		case "newReports":
			getNewReports();
			break;
		case "addReport":
			$equipment = $_POST["equipment"];
			$floor = $_POST["floor"];
			$notes = $_POST["notes"];
			addReport($equipment, $floor, $notes);
			break;
		case "removeReport":
			$id = $_POST["id"];
			removeReport($id);
			break;
		case "acceptReport":
			$id = $_POST["id"];
			acceptReport($id);
			break;
	}
	
	
	
	echo json_encode($response);
?>
