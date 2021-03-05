<?php

require "connection.php";

#variable to hold lec STAFFID
$STAFFID=$_POST["STAFFID"];

$sql_query="SELECT UNIT_CODE,UNIT_NAME from lec_units WHERE STAFFID = '$STAFFID';";

$result= mysqli_query($connection,$sql_query);

$response = array();

while($row=mysqli_fetch_array($result))
{	
	array_push($response,array("UNIT_CODE"=>$row[0],"UNIT_NAME"=>$row[1]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($connection);
?>