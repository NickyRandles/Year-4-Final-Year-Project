<?php 
require "connect.php";

$profileId = $_POST["profileId"];
$sql = "SELECT * FROM Doctors where id = $profileId";
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