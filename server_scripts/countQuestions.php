<?php
require "connection.php";
$mysql_query = "SELECT * FROM quiz_questions";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
     $row = mysqli_num_rows($result);
    echo $row;
}
else {
  echo "error" . $mysql_query . "<br>" . $connect->error;
}
$connect->close();
?>
