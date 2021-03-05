<?php
require "connection.php";

$REGNO=$_POST["REGNO"];
$UNIT_CODE=$_POST["UNIT_CODE"];
//get date e.g 4TH-SEPTEMBER-2020
$TODAYS_DATE = strtoupper(date('jS\-F\-Y'));
//echo $TODAYS_DATE;

//split the unit code to form the table name for attendance
$unit_code_parts = explode(" ",$UNIT_CODE);	
$table_name= $unit_code_parts[0]."_".$unit_code_parts[1]."_ATTENDANCE";

$sql_query = "UPDATE $table_name SET `$TODAYS_DATE` = 'PRESENT' WHERE REGNO = '$REGNO';";

$result= mysqli_query($connection,$sql_query);

//check if query affected any rows
if(mysqli_affected_rows($connection)>0)
{
	echo "Attendance Signed Successfuly";
}
else if(mysqli_affected_rows($connection)==0)
{
	echo "Attendance Already Signed!";
}
else
{
	echo "An Error Occured! ".mysqli_error($connection);
} 
?>