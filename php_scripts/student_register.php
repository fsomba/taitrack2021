<?php
require "connection.php";

$REGNO=$_POST["REGNO"];
$FIRSTNAME=$_POST["FIRSTNAME"];
$SECONDNAME=$_POST["SECONDNAME"];
$EMAIL=$_POST["EMAIL"];
$GENDER=$_POST["GENDER"];
$FACULTY=$_POST["FACULTY"];
$COURSE=$_POST["COURSE"];
$PASSCODE=$_POST["PASSCODE"];

$sql_query = "insert into students values('$REGNO','$FIRSTNAME','$SECONDNAME','$EMAIL','$GENDER','$FACULTY','$COURSE','$PASSCODE');";

if(mysqli_query($connection,$sql_query))
{
echo "Registration Success";
}
else
{
echo "Registration failed!".mysqli_error($connection);
}

?>