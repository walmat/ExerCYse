<?php
    require("dbConnect.php");

    $username = $_POST['username'];
    $toggle = $_POST['toggle'];
    $name = $_POST['name'];
    $eIDs = $_POST['eIDs'];
    $wID = $_POST['workoutID'];

    $response = array();

    function getWorkoutsFromUser() {
        global $connect, $username, $response;

        $sql = "SELECT * FROM users WHERE username='$username'";
        $result = $connect->query($sql);
        $row = $result->fetch_assoc();
        $userID = $row['id'];

        $sql = "SELECT * FROM workout WHERE uID='$userID'";
        $index = 0;
        $result = $connect->query($sql);
        while ($value = $result->fetch_assoc()) {
            $response[$index] = $value;
            $index++;
        }
    }

    function getWorkoutFromId() {
        global $connect, $wID, $response;

        $sql = "SELECT * FROM workout WHERE id='$wID'";
        $result = $connect->query($sql);
        $value = $result->fetch_assoc();
        $response[0] = $value;
    }

    function insertWorkout() {
        global $connect, $username, $response, $eIDs, $name;

        $sql = "SELECT * FROM users WHERE username='$username'";
        $result = $connect->query($sql);
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $userID = $row['id'];
            $sql = "INSERT INTO workout (uID, name, eIDs) VALUES ('$userID', '$name', '$eIDs')";
            $connect->query($sql);
        }
    }

    function getAllWorkouts() {
        global $connect, $response;

        $sql = "SELECT * FROM workout";

        $result = $connect->query($sql);
        $index = 0;
        if ($result->num_rows > 0) {
            $result = $connect->query($sql);
            while ($value = $result->fetch_assoc()) {
                $response[$index] = $value;
                $index++;
            }
        }
    }

    function deleteWorkout() {
        global $connect, $response, $wID;

        $sql = "DELETE FROM workout WHERE id='$wID'";

        $result = $connect->query($sql);

        if ($connect->affected_rows > 0) {
            $response['success'] = true;
        } else {
            $response['success'] = false;
        }
    }

    switch ($toggle) {
        case 'getWorkoutsFromUser':
            getWorkoutsFromUser();
            break;
        case 'getAllWorkouts':
            getAllWorkouts();
            break;
        case 'insertWorkout':
            insertWorkout();
            break;
        case 'deleteWorkout':
            deleteWorkout();
            break;
        case 'getWorkoutFromId':
            getWorkoutFromId();
            break;
    }


    echo json_encode($response);
?>