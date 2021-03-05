<?php
//use the Admin Account For GRANT/REVOKE rights
$db_name="kasukupro";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";

$connection=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

$UNIT_CODE=$_POST["UNIT_CODE"];
$LATITUDE = $_POST["LATITUDE"];
$LONGITUDE = $_POST["LONGITUDE"];

//get date e.g 4TH-SEPTEMBER-2020
$TODAYS_DATE = strtoupper(date('jS\-F\-Y'));
//echo $TODAYS_DATE;

//split the unit code to form the table name for attendance
$unit_code_parts = explode(" ",$UNIT_CODE);	
$table_name= $unit_code_parts[0]."_".$unit_code_parts[1]."_ATTENDANCE";

//create column for signing todays attendance
$sql_query = "ALTER TABLE $table_name ADD `$TODAYS_DATE` VARCHAR(10) NULL DEFAULT 'ABSENT';";
//grant user UPDATE rights
$sql_query2 = "GRANT UPDATE ON $table_name TO 'kasukupro'@'localhost';";

//TRY TO CREATE COLUMN FOR TODAYS_DATE SIGNING
try {
	$result = mysqli_query($connection,$sql_query);
}catch(Exception $e) {
	//do nothing. Let the execution continue
}

//TRY TO TURN ON TABLE UPDATE RIGHTS AND UPDATE LOCATION COORDINATES
$location_query = "UPDATE units SET LATITUDE = '$LATITUDE', LONGITUDE = '$LONGITUDE' WHERE CODE = '$UNIT_CODE';";

try {
	//execute the GRANT UPDATE right query 
	$result = mysqli_query($connection,$sql_query2);
	//
	$result_location = mysqli_query($connection, $location_query);
	//check if query affected any rows
	if(mysqli_affected_rows($connection)>0)
	{
		//Location Updated Successfuly
		echo "Signing Turned ON";
	}
	else if(mysqli_affected_rows($connection)==0)
	{
		//Location Update Failed!
		echo "Signing Already ON";
	}
	
}catch(Exception $error) {
	//print the error
	echo "An Error Occured! ".mysqli_error($connection);
}

?>