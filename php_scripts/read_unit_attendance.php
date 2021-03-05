<?php
require "connection.php";

$REGNO=$_POST["REGNO"];
$UNIT_CODE=$_POST["UNIT_CODE"];

//split the unit code to form the table name for attendance
$unit_code_parts = explode(" ",$UNIT_CODE);	
$table_name= $unit_code_parts[0]."_".$unit_code_parts[1]."_ATTENDANCE";

//read the student attendance for the unit
$sql_query = "SELECT * FROM $table_name WHERE REGNO='$REGNO' ";
$result = mysqli_query($connection,$sql_query);

//save attendance as json string
$json = '';

while($r = mysqli_fetch_array($result)) {
    //encode the row and add to the json string
	$json = $json.json_encode($r);
  }

echo $json;

?>