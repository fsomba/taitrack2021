<?php
//Use the Admin Account For GRANT/REVOKE rights
$db_name="kasukupro";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";

$connection=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

$UNIT_CODE=$_POST["UNIT_CODE"];
//$STAFFID=$_POST["STAFFID"];

//split the unit code to form the table name for attendance
$unit_code_parts = explode(" ",$UNIT_CODE);	
$table_name= $unit_code_parts[0]."_".$unit_code_parts[1]."_ATTENDANCE";

//revoke user UPDATE rights
$sql_query = "REVOKE UPDATE ON $table_name FROM 'kasukupro'@'localhost';";

//check if query was executed
if(mysqli_query($connection,$sql_query))
{
	echo "Signing Turned OFF";
}
else
{
	echo "An Error Occured! ".mysqli_error($connection);
} 
?>