<?php
require "connect.php";

$username = $_POST["username"];
$password = $_POST["password"];

$sql = "SELECT * FROM Doctors, Patients WHERE Doctors.username = '$username' AND Doctors.password = '$password' OR Patients.username = '$username' AND Patients.password = '$password' LIMIT 1";
$query = mysqli_query($con, $sql);
$num = mysqli_num_rows($query);

if($num > 0){
	//$row = mysqli_fetch_assoc($query);
	//$username = $row["username"];
	echo "Login successful";
}
else{
	echo "Incorrect username or password";
}
?>