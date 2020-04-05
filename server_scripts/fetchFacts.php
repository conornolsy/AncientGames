<?php
require "connection.php";
$question_number= $_POST["q_number"];
$mysql_query = "SELECT fact FROM question_facts WHERE question_number = '$question_number'";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
  while($row = $result->fetch_assoc()) {
        echo $row["fact"];
    }
}
else {
  echo "No Fact found: " . $mysql_query . "<br>" . $connect->error;
}
$connect->close();
?>
