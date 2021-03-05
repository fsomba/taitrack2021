<?php
$db_name="kasukupro";
$mysql_user="kasukupro";
$mysql_pass="K-frankie2518";
$server_name="localhost";

$connection=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

if(!$connection)
{
//echo"connection Error....".mysqli_connect_error();
}
else{
//echo"<h3>Database connection Success....</h3>";
}
?>