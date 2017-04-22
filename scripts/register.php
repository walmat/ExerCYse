<?php
    require("password.php");
    require("dbConnect.php");

    /********* POSTED VARIABLES ********/
    /* **********------------********* */
    /* these are the list of variables */
    /* that are posted from register   */
    /* request. Any value you post you */
    /* you will want to store in a     */
    /* local variable so you can       */
    /* reference it later              */
    /* ******************************* */
    $first = trim($_POST['firstname']);
    $last = trim($_POST['lastname']);
    $username = trim($_POST['username']);
    $email = trim($_POST['email']);
    $password = trim($_POST['password']);
    /******* END POSTED VARIABLES ******/


    /*these are considered 'flag' variables. I used them
    to check different things throughout the script. I'll
    explain these more when they're used. */
    $email_valid = false;
    $nameError = false;
    $usernameError = false;
    $available = false;
    $provided = true;

    /* 'required[]' is an array that I store all the
    posted variables' names in. I then use this array 
    following this delcaration. */
    $required = array('firstname', 'lastname', 'username', 'email', 'password');

    /* Here's where the required array comes in handy.
    This would be the same as checking if(empty($_POST('firstname'))) 
    where we'd have to do that for each variable in the required array. Instead I generalized it to check each field. Hopefully
    that makes sense. */
    $empty = false;
    foreach($required as $field) {
      if (empty($_POST[$field])) {
        $empty = true;
      }
    }

    /* This is just a basic ReGeX function. If you're familiar with
    this, I won't need to explain it much, but if you want to know
    more about this, or have questions, ask how it works by all means. */
    function isValidEmail($address) {
        return preg_match('/^[a-z0-9](\.?[a-z0-9_-]){0,}@iastate.edu/', $address);
    }

    /* ctype_alnum() is a built in PHP function that checks
    to see if the string given only contains alphanumeric values
    strlen() is self-explanatory. It returns the length of a string. 
    Now my flag variable is set to TRUE if ctype_alnum is false */
    if (!ctype_alnum($username)) {
        $usernameError = true;
    }

    /* Here's where I call the isValidEmail() function and set my
    flag variable email_valid to true if the email provided passes
    the regex I had specified in my isValidEmail() function. */
    if (isValidEmail($email)){
        $email_valid = true;
    }

    /* preg_match() to verify the name given is alphabetic. Otherwise,
    set my flag variable to true. */
    if (!preg_match("/^[a-zA-Z ]*$/",$first) && !preg_match("/^[a-zA-Z ]*$/",$last)) {
        $nameError = true;
    }

    /* OKAY. THIS IS IMPORTANT!!!*/
    /* so my global variables at the top I have to reference inside
    of this function, similarly in java: if we have a global int i
    and we want to set that to a local variable in a function, we have
    to do something like: int x = i; where x is a local variable inside
    of a function. */
    function registerUser() {
        global $connect, $first, $last, $username, $email, $password;

        //password encrypting. nothing exciting here.
        $passwordHash = password_hash($password, PASSWORD_DEFAULT);

        /* sql is my statement that I'm going to be executing. The syntax
        of this statement is VERY crucial and finicky, so pay attention 
        to that as well. */

        $type = 1; //student
        $sql = "INSERT INTO users (type, username, firstname, lastname, password, email) VALUES ('$type', '$username', '$first', '$last', '$passwordHash', '$email')";

        //execute my statement on the connected database
        if($connect->query($sql)) {
            return true; //if it's successfully executed, true
        } else {
            return false; //otherwise, false
        }
    }

    function userAvailable() {
        global $connect, $username, $email;

        //I could have done the required array here again, but I got lazy
        if (((!isset($username)) || trim($username) === '') ||
            ((!isset($email)) || trim($email) === ''))
        {
            $provided = false;
        }

        //here i'm selecting all records from my users table where the username matches the username they provide
        $sql = "SELECT * FROM users WHERE username = '$username'";

        //get the results, notice it can be none! look at the difference here and the executing in registerUser(). Notice how we store the results in a variable so we can reference it this time. 
        $result = $connect->query($sql);

        //check to see if the number of results from the statement
        if ($result->num_rows > 0) {
            return false; //if any user is already registered with that username, return false.
        } else {
            //here i'm selecting all records from the users table where the email is the given email. 
            $sql = "SELECT * FROM users WHERE email = '$email'";

            //store the results of the statement.
            $result = $connect->query($sql);

            //check the number of results.
            if ($result->num_rows > 0) {
                return false; //if more than none, return false.
            } else {
                return true; //finally, we can conclude no user exists with the given email AND username, so we can return true.
            }
        }
    }

    $available = userAvailable(); //store the result of userAvailable().

    $response = array(); //create an empty array

    /* specify the name of each field in $response, and name them something that you'll reference in Android Studio later. */
    $response["success"] = false; 
    $response["empty"] = false;
    $response["email"] = false;
    $response["username"] = false;
    $response["name"] = false;
    $response["available"] = true;

    //these are all checks from the flag variables that I have.
    if ($empty) {
        $response["empty"] = true;
    }

    if (!$available && $provided) {
        $response["available"] = false;
    }

    if (!$email_valid && !$empty) {
        $response["email"] = true;
    }

    if ($usernameError && !$empty) {
        $response["username"] = true;
    }

    if ($nameError && !$empty) {
        $response["name"] = true;
    }

    //finally, the execution of registerUser() if all the flag variables are meeting what they need to. 
    if ($available && $email_valid && !$usernameError
        && !$nameError && !$empty){
        if (registerUser()) {
            $response["success"] = true; //store success in the array.
        }
    }
    
    //echo is the same as "return x" from a function, but it's returning an array in this case. You can echo whatever you would like, but keep in mind that you only want to echo what is useful to the application. So for debuggin purposes, you can write echo statements, or vardump() throughout, but remember to comment them out in the end.

    //anyway, I'm returning a json_encoded() array here so that I can access the variables on the "client" side. 

    //If you go to: 'http://proj-309-yt-6.cs.iastate.edu/ExerCYse/register.php' you can see the json array being echoed.
    echo json_encode($response);
?>