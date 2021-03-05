<?php
//use the Admin Account For UPDATE rights
$db_name="kasukupro";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";

$connection=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

$UNIT_CODE= $_POST["UNIT_CODE"];
$LATITUDE = $_POST["LATITUDE"];
$LONGITUDE = $_POST["LONGITUDE"];

//sql query
$sql_query = "UPDATE units SET LATITUDE = '$LATITUDE', LONGITUDE = '$LONGITUDE' WHERE CODE = '$UNIT_CODE';";

$result= mysqli_query($connection,$sql_query);

//check if query affected any rows
if(mysqli_affected_rows($connection)>0)
{
	echo "Location Updated Successfuly";
}
else if(mysqli_affected_rows($connection)==0)
{
	echo "Location Update Failed!";
}
else
{
	echo "An Error Occured! ".mysqli_error($connection);
}

?>