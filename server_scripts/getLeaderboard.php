<?php
require "connection.php";
$mysql_query = "SELECT t.username, t.highscore FROM Users t ORDER BY t.highscore DESC LIMIT 10";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
  while($row = $result->fetch_assoc()) {
      echo $row["username"]."#". $row["highscore"]. "#";
    }
}
else {
  echo "No Q found: " . $mysql_query . "<br>" . $connect->error;
}
$connect->close();
?>
