<?php 
require "connect.php";

$username = $_POST["username"];
$userType = $_POST["userType"];
$date = $_POST["date"];

if($userType == "doctor"){
	$sql = "SELECT id FROM Doctors WHERE username = '$username' LIMIT 1";
	$query = mysqli_query($con, $sql);
	$row = mysqli_fetch_assoc($query);
	$id = $row["id"];
	$sql = "SELECT * FROM Appointments WHERE date = '$date' AND Doctors_id = $id AND Patients_id NOT NULL";

}
else if($userType == "patient"){
	$sql = "SELECT id FROM Patients WHERE username = '$username' LIMIT 1";
	$query = mysqli_query($con, $sql);
	$row = mysqli_fetch_assoc($query);
	$id = $row["id"];
	$sql = "SELECT name, time FROM Appointments WHERE date = '$date' AND Patients_id = $id UNION ALL SELECT name, time FROM Medications WHERE date = '$date' AND Patients_id = $id";
}
$query = mysqli_query($con, $sql);
$num = mysqli_num_rows($query);

$tempArray = array();
if($num > 0){
	while($row = mysqli_fetch_assoc($query)){
		$tempArray[] = $row;
	}
	
	header('Content-Type: application/json');
	echo json_encode(array("result"=>$tempArray));
}
?>