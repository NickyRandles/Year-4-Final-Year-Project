<?php 
require "connect.php";

$loggedInUsername = $_POST["loggedInUsername"];
$userType = $_POST["userType"];

if($userType == "patient"){
	$sql = "SELECT id FROM Patients WHERE username = '$loggedInUsername' LIMIT 1";
	$query = mysqli_query($con, $sql);
	$row = mysqli_fetch_assoc($query);
	$loggedInId = $row["id"];
	$sql = "SELECT Doctors.first_name, Doctors.last_name FROM Doctors, Contacts WHERE Doctors.id = Contacts.Doctors_id AND Contacts.Patients_id = $loggedInId";
	$query = mysqli_query($con, $sql);
	$num = mysqli_num_rows($query);
	
}
else if($userType == "doctor"){
	$sql = "SELECT id FROM Doctors WHERE username = '$loggedInUsername' LIMIT 1";
	$query = mysqli_query($con, $sql);
	$row = mysqli_fetch_assoc($query);
	$loggedInId = $row["id"];
	$sql = "SELECT Patients.first_name, Patients.last_name FROM Patients, Contacts WHERE Patients.id = Contacts.Patients_id AND Contacts.Doctors_id = $loggedInId";
	$query = mysqli_query($con, $sql);
	$num = mysqli_num_rows($query);
}

$tempArray = array();

if($num > 0){
	while($row = mysqli_fetch_assoc($query)){
		$tempArray[] = $row;
	}
	
	header('Content-Type: application/json');
	echo json_encode(array("result"=>$tempArray));
}

?>