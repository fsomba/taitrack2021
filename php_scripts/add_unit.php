<?php

require "connection.php";

#variable to hold student regno
$REGNO=$_POST["REGNO"];
#course name
$COURSE_NAME = '';

#use student regno to get their course name
$sql_query1="SELECT COURSE from students where REGNO = '$REGNO';";

$result1= mysqli_query($connection,$sql_query1);

if(mysqli_num_rows($result1)>0)
{
	$row=mysqli_fetch_assoc($result1);
	$COURSE_NAME = $row["COURSE"];
}

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