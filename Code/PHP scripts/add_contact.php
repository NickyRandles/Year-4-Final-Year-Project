<?php
require "connect.php";

$profileId = $_POST["profileId"];
$loggedInUsername = $_POST["loggedInUsername"];

$sql = "SELECT id FROM Patients WHERE username = '$loggedInUsername' LIMIT 1";
$query = mysqli_query($con, $sql);
$row = mysqli_fetch_assoc($query);
$loggedInId = $row["id"];

$sql2 = "SELECT id FROM Contacts WHERE Doctors_id = $profileId AND Patients_id = $loggedInId LIMIT 1";
$query2 = mysqli_query($con, $sql2);
$num = mysqli_num_rows($query2);
if($num > 0){
	echo "Contact already added";
}
else{
	$sql3 = "INSERT INTO Contacts(Doctors_id, Patients_id) VALUES($profileId, $loggedInId)";
	if(mysqli_query($con, $sql3)){
		echo "Contact added";
	}
	else{
		echo "Unable to add contact";
		echo mysqli_error($con);
	}
}



?>