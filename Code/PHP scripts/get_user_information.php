<?php 
require "connect.php";

$username = $_POST["username"];
$userType = "doctor";
$sql = "SELECT * FROM Doctors WHERE username = '$username' LIMIT 1";
$query = mysqli_query($con, $sql);
$num = mysqli_num_rows($query);

if($num < 1){
	$userType = "patient";
	$sql = "SELECT * FROM Patients WHERE username = '$username' LIMIT 1";
	$query = mysqli_query($con, $sql);
	$num = mysqli_num_rows($query);
}

$tempArray = array();

if($num > 0){
	while($row = mysqli_fetch_assoc($query)){
		$tempArray[] = array_merge($row, array("userType" => "$userType"));		
	}
	
	header('Content-Type: application/json');
	echo json_encode(array("result"=>$tempArray));
}

?>