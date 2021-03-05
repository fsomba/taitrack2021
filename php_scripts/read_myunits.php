<?php

require "connection.php";

#variable to hold student regno
$REGNO=$_POST["REGNO"];

$sql_query="SELECT REGNO,UNIT_CODE,UNIT_NAME from student_units WHERE REGNO = '$REGNO';";

$result= mysqli_query($connection,$sql_query);

$response = array();

while($row=mysqli_fetch_array($result))
{	
	array_push($response,array("REGNO"=>$row[0],"UNIT_CODE"=>$row[1],"UNIT_NAME"=>$row[2]));
}
echo json_encode(array("server_response"=>$response));

mysqli_close($connection);
?>