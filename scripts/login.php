<?php
    require("password.php");
    require("dbConnect.php");
    session_start();
    
    $username = $_POST["username"];
    $password = $_POST["password"];

    $response = array();
    $response["success"] = false; 

    $sql = "SELECT * FROM users WHERE userName='$username'";
    $result = $connect->query($sql);
    if ($result->num_rows > 0){
        $row = $result->fetch_assoc();
        $pass = $row['password'];
        $first = $row['firstname'];
        $last = $row['lastname'];

        if (password_verify($password, $pass)) {
            $response["success"] = true;
            $response["first"] = $first;
            $response["last"] = $last;
            $_SESSION['username'] = $username;
        }
    }

    echo json_encode($response);
?>