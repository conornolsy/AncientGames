<?php
require "connection.php";
$user_name= $_POST["username"];
$new_score= $_POST["highScore"];
$mysql_query = "update Users set highscore = '$new_score' where username = '$user_name'";
$result = mysqli_query($connect,$mysql_query);
if($connect->query($mysql_query) === TRUE)
{
  echo "New Highscore! You achieved "  . $new_score .  " points!";   
}
else {
  echo "Error! Highscore not recordered";
}
?>
