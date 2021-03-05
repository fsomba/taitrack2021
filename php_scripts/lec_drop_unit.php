<?php
require "connection.php";

#variable to hold lec STAFFID
$STAFFID=$_POST["STAFFID"];
$UNIT_CODE=$_POST["UNIT_CODE"];

$sql_query="DELETE from lec_units WHERE STAFFID='$STAFFID' AND UNIT_CODE='$UNIT_CODE';";

$result= mysqli_query($connection,$sql_query);

//mysqli delete function returns true even if no record is found for deleting
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