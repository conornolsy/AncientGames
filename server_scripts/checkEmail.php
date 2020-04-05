<?php
require "connection.php";
$email = $_POST["email"];
$mysql_query = "select email from Users where email = '$email'";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
  echo "Sorry email already taken!";
}
else {
  echo "email accepted";
}
$connect->close();
?>

