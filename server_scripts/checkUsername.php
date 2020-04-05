<?php
require "connection.php";
$username=$_POST["username"];
$mysql_query = "select username from Users where username = '$username'";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
  echo "Sorry username already taken!";
}
else {
  echo "username accepted";
}
$connect->close();
?>
