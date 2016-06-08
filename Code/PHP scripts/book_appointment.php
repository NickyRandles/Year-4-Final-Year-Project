<?php
require "connect.php";

$appointmentId = $_POST["appointmentId"];
$userId = $_POST["userId"];

$sql = "UPDATE Appointments SET Patients_id = $userId WHERE id = $appointmentId";
if(mysqli_query($con, $sql)){
	echo "Appointment booked";
}
else{
	echo "Unable to book appointment";
	echo mysqli_error($con);
}
?>