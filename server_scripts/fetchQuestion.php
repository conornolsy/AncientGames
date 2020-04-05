<?php
require "connection.php";
$question_number= $_POST["q_number"];
$mysql_query = "SELECT question, option_A, option_B, option_C, option_D, correct_Answer FROM quiz_questions WHERE question_number = '$question_number'";
$result = mysqli_query($connect,$mysql_query);
if(mysqli_num_rows($result) >0)
{
  while($row = $result->fetch_assoc()) {
        echo $row["question"]."#" .  $row["option_A"]. "#" . $row["option_B"]."#".  $row["option_C"]. "#" . $row["option_D"]."#".  $row["correct_Answer"];
    }
}
else {
  echo "No Q found: " . $mysql_query . "<br>" . $connect->error;
}
$connect->close();
?>
