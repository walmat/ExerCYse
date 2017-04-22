<?php
    require("dbConnect.php");
    session_start();

    $toggle = $_POST['toggle'];
    $userID = $_POST['uID'];
    $username = $_POST['username'];

    $response = array();
    $required = array('type','username', 'firstname', 'lastname', 'password', 'email','age','imgPath','bio','weight','height');

    function getIdAndName() {
        global $connect, $response;

        $sql = "SELECT id, username, firstname, lastname, email FROM users";

        $result = $connect->query($sql);
        $index = 0;
        while ($value = $result->fetch_assoc()) {
            $response[$index] = $value;
            $index++;
        }
    }

    function getMyProfile() {
        global $connect, $username, $response, $required;

        $sql = "SELECT * FROM users WHERE username='$username'";
        $result = $connect->query($sql);
        if ($result->num_rows > 0){
            $row = $result->fetch_assoc();

            foreach($required as $field) {
                $response[$field] = $row[$field];
            }
        }
    }

    function getUserProfile() {
        global $connect, $userID, $response, $required;

        $sql = "SELECT * FROM users WHERE id='$userID'";
        $result = $connect->$query($sql);
        if ($result->num_rows > 0){
            $row = $result->fetch_assoc();

            foreach($required as $field) {
                $response[$field] = $row[$field];
            }
        }

    }

    switch ($toggle) {
        case 'getIdAndName':
            getIdAndName();
            break;
        case 'getMyProfile':
            getMyProfile();
            break;
        case 'getUserProfile':
            getUserProfile();
            break;
    }

    echo json_encode($response);
?>