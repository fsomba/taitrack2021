<?php
require "connection.php";
$server_response="";

$STAFFID=$_POST["STAFFID"];
$UNIT_CODE=$_POST["UNIT_CODE"];
$UNIT_NAME=$_POST["UNIT_NAME"];
$COURSE_NAME = $_POST["COURSE_NAME"];

//verify that the lecturer is not having that unit already
$AlreadyInDB = false;
//
$sql_query2="SELECT UNIT_CODE from lec_units where STAFFID = '$STAFFID';";
$result2= mysqli_query($connection,$sql_query2);
while($row=mysqli_fetch_array($result2))
{
	if($row["UNIT_CODE"]==$UNIT_CODE){
		$AlreadyInDB = true;
	}
}

//
if($AlreadyInDB==true){
	$server_response ="Addition Failed! The Unit Is Already In Your Selected Units!";
}else{
	//get lecturer full names and title, using the STAFFID
	$sql_query1="SELECT FIRSTNAME, SECONDNAME, TITLE from lecturers where STAFFID = '$STAFFID';";

	$FIRSTNAME = "";
	$SECONDNAME = "";
	$TITLE = "";

	$result1= mysqli_query($connection,$sql_query1);

	if(mysqli_num_rows($result1)>0)
	{
		$row=mysqli_fetch_assoc($result1);
		$FIRSTNAME = $row["FIRSTNAME"];
		$SECONDNAME = $row["SECONDNAME"];
		$TITLE = $row["TITLE"];
	}

	//merge names with title
	$FULLNAME = $TITLE." ".$FIRSTNAME." ".$SECONDNAME;

	$sql_query = "INSERT INTO lec_units (NUMBER, STAFFID, FULLNAME, UNIT_CODE, UNIT_NAME, COURSE_NAME)
								VALUES (NULL,'$STAFFID','$FULLNAME','$UNIT_CODE','$UNIT_NAME','$COURSE_NAME');";

	if(mysqli_query($connection,$sql_query))
	{
	$server_response = "Unit Added";
	}
	else
	{
	$server_response = "Addition failed! ".mysqli_error($connection);
	}
}

echo $server_response;

?>