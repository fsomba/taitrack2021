<?php

require "connection.php";

#course name
$COURSE_NAME = $_POST["COURSE_NAME"];

###proceed to get the course units 
$sql_query2="SELECT CODE,NAME from units WHERE COURSE_NAME = '$COURSE_NAME';";

$result2= mysqli_query($connection,$sql_query2);

$response2 = array();

while($row=mysqli_fetch_array($result2))
{	
	array_push($response2,array("UNIT_CODE"=>$row[0],"UNIT_NAME"=>$row[1]));
}
echo json_encode(array("server_response"=>$response2));

mysqli_close($connection);
?>