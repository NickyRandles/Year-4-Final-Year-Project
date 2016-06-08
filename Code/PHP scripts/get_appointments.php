<?php 
require "connect.php";

$date = $_POST["date"];
$doctorId = $_POST["doctorId"];
$sql = "SELECT * FROM Appointments WHERE date = '$date' AND Doctors_id = $doctorId AND Patients_id IS NULL";
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