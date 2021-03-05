<?php
require "connection.php";

$REGNO=$_POST["REGNO"];
$UNIT_CODE=$_POST["UNIT_CODE"];
$UNIT_NAME=$_POST["UNIT_NAME"];

//get student full names and title, using the REGNO
$sql_query1="SELECT FIRSTNAME, SECONDNAME from students where REGNO = '$REGNO';";

$FIRSTNAME = "";
$SECONDNAME = "";

$result1= mysqli_query($connection,$sql_query1);

if(mysqli_num_rows($result1)>0)
{
	$row=mysqli_fetch_assoc($result1);
	$FIRSTNAME = $row["FIRSTNAME"];
	$SECONDNAME = $row["SECONDNAME"];
}

//merge names
$STUDENT_NAME = $FIRSTNAME." ".$SECONDNAME;

//register student on attendance table for that unit
$unit_code_parts = explode(" ",$UNIT_CODE);	
$table_name= $unit_code_parts[0]."_".$unit_code_parts[1]."_ATTENDANCE";
// `{$mysql_tb}` to specify table name
$sql_query2 = "INSERT INTO $table_name (REGNO, STUDENT_NAME) VALUES ('$REGNO', '$STUDENT_NAME');";
//execute the query
$result2= mysqli_query($connection,$sql_query2);

//save values to student chosen units table
$sql_query3 = "insert into student_units values(NULL,'$REGNO','$STUDENT_NAME','$UNIT_CODE','$UNIT_NAME');";

if(mysqli_query($connection,$sql_query3))
{
echo "Unit Added";
}
else
{
echo "Addition failed! ".mysqli_error($connection);
}

?>