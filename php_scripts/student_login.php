<?php
require"connection.php";

$REGNO=$_POST["REGNO"];
$PASSCODE=$_POST["PASSCODE"];

$sql_query="SELECT FIRSTNAME from students where REGNO LIKE '$REGNO' AND PASSCODE LIKE '$PASSCODE';";

$result= mysqli_query($connection,$sql_query);

if(mysqli_num_rows($result)>0)
{
$row=mysqli_fetch_assoc($result);
$name=$row["FIRSTNAME"];
echo "Login success ".$name;
}
else{
echo "Login failed! Try again";
} 
?>
