<?php

require "connection.php";

#variables
$UNIT_CODE= $_POST["UNIT_CODE"];
$LATITUDE = 0;
$LONGITUDE = 0;

$sql_query="SELECT LATITUDE, LONGITUDE from units WHERE CODE = '$UNIT_CODE';";

$result= mysqli_query($connection,$sql_query);

while($row=mysqli_fetch_array($result))
{	
	$LATITUDE = $row[0];
	$LONGITUDE = $row[1];
}
//output the latitude, longitude to android
echo $LATITUDE.'@'.$LONGITUDE;

mysqli_close($connection);
?>