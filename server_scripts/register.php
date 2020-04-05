<?php
require "connection.php";
$email = $_POST["email"];
$username = $_POST["username"];
$password = $_POST["password"];
$salt = $_POST["salt"];
$mysql_query = "insert into Users (username, password, salt, email, highscore) values ('$username', '$password','$salt', '$email', 0)";
if($connect->query($mysql_query) === TRUE)
{
  echo "Registration Successful";
}
else {
  echo "Registration unsuccessful: " . $mysql_query . "<br>" . $connect->error;
}
$connect->close();
?>
