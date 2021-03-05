<?php
require "connection.php";

$STAFFID=$_POST["STAFFID"];
$FIRSTNAME=$_POST["FIRSTNAME"];
$SECONDNAME=$_POST["SECONDNAME"];
$TITLE=$_POST["TITLE"];
$EMAIL=$_POST["EMAIL"];
$PASSCODE=$_POST["PASSCODE"];

$sql_query = "insert into lecturers values('$STAFFID','$FIRSTNAME','$SECONDNAME','$TITLE','$EMAIL','$PASSCODE');";

if(mysqli_query($connection,$sql_query))
{
echo "Registration Success";
}
else
{
echo "Registration failed!".mysqli_error($connection);
}

?>