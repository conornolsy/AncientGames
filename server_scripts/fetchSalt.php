<?php
require "connection.php";
$user_name= $_POST["username"];
$mysql_query = "select salt from Users where username = '$user_name'";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
  while($row = $result->fetch_assoc()) {
        echo $row["salt"];
    }

}
else {
  echo "Username not found:" . $user_name;
}
$connect->close();
?>
