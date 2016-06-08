<?php
require "connect.php";

$username = $_POST["username"];
$name = $_POST["name"];
$startDate = $_POST["startDate"];
$time = $_POST["time"];
$duration = $_POST["duration"];

$sql = "SELECT id FROM Patients WHERE username = '$username' LIMIT 1";
$query = mysqli_query($con, $sql);
$row = mysqli_fetch_assoc($query);
$id = $row["id"];

$i = 0;
while($i < $duration){
	$tempDate = date('Y-m-d', strtotime($startDate . " + $i days"));
	$sql = "INSERT INTO Medications(name, date, time, Patients_id) VALUES('$name', '$tempDate', '$time', $id)";
	if(mysqli_query($con, $sql)){
		echo "Schedule successfully added";
	}
	else{
		echo mysqli_error($con);
	}
	$i++;
}


?>
