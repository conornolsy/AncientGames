<?php
require "connection.php";
$user_name= $_POST["username"];
$user_password= $_POST["password"];
$mysql_query = "select * from Users where username = '$user_name' and password = '$user_password'";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
   while($row = $result->fetch_assoc()) {
         echo "Login Successful" ."#" . $row["highscore"];
    }
 
}
else {
  echo "Login Unsuccessful";
}
?>
