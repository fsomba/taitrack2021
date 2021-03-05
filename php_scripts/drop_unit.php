<?php
require "connection.php";

$REGNO=$_POST["REGNO"];
$UNIT_CODE=$_POST["UNIT_CODE"];

$sql_query="DELETE from student_units WHERE REGNO='$REGNO' AND UNIT_CODE='$UNIT_CODE';";

$result= mysqli_query($connection,$sql_query);

//mysqli function returns true even if no record is found for deleting
//Hence, check if query affected any rows
if(mysqli_affected_rows($connection)>0)
{
	echo "Unit Dropped";
}
else if(mysqli_affected_rows($connection)==0)
{
	echo "Unit Not Found In DB";
}
else
{
	echo "Delete failed! ".mysqli_error($connection);
} 
?>