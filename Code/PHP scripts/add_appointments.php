<?php
require "connect.php";

$username = $_POST["username"];
$name = $_POST["name"];
$startDate = $_POST["startDate"];
$startTime = $_POST["startTime"];
$minutesDuration = $_POST["minutesDuration"];
$amount = $_POST["amount"];
$daysDuration = $_POST["daysDuration"];

$sql = "SELECT id FROM Doctors WHERE username = '$username' LIMIT 1";
$query = mysqli_query($con, $sql);
$row = mysqli_fetch_assoc($query);
$id = $row["id"];

$i = 0;
while($i < $daysDuration){
	$tempDate = date('Y-m-d', strtotime($startDate . " + $i days"));
	$j = 0;
	while($j < $amount){
		$increase = $j * $minutesDuration;
		$tempTime =  date('h:i:s', strtotime($startTime . " + $increase minutes"));
		$sql2 = "INSERT INTO Appointments(name, date, time, Doctors_id) VALUES('$name', '$tempDate', '$tempTime', $id)";
		$j++;
	}
	$i++;
}
echo "Schedule Succesfully Added";
?>