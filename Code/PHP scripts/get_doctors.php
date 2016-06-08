<?php 
require "connect.php";

$sql = "SELECT * FROM Doctors";
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