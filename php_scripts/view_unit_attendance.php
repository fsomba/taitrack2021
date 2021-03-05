<?php
require "connection.php";

$UNIT_CODE=$_POST["UNIT_CODE"];

//split the unit code to form the table name for attendance
$unit_code_parts = explode(" ",$UNIT_CODE);	
$table_name= $unit_code_parts[0]."_".$unit_code_parts[1]."_ATTENDANCE";

//read the student attendance for the unit
$sql_query = "SELECT * FROM $table_name;";

$result = mysqli_query($connection,$sql_query);

$response = array();

while($row=mysqli_fetch_array($result))
{	
	//save attendance as json string
    //encode the row and add to the json string
	//$json = json_encode($row);
	array_push($response,array($row));
}
echo json_encode(array("server_response"=>$response));

?>