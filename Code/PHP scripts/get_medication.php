<?php 
require "connect.php";

$username = $_POST["username"];
$day = $_POST["day"];
$month = $_POST["month"];
$year = $_POST["year"];

$sql = "SELECT * FROM Medications WHERE days = '$day' AND months = '$month' AND years = '$year' AND username = '$username'";
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