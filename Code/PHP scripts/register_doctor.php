<?php
require "connect.php";

$first_name = $_POST["firstName"];
$last_name = $_POST["lastName"];
$username = $_POST["username"];
$password = $_POST["password"];
$background = $_POST["background"];
$qualifications = $_POST["qualifications"];
$experience = $_POST["experience"];
$address_line = $_POST["addressLine"];
$city = $_POST["city"];
$county = $_POST["county"];
$country = $_POST["country"];

$sql = "SELECT username FROM Doctors WHERE username = '$username' LIMIT 1";
$query = mysqli_query($con, $sql);
$num = mysqli_num_rows($query);
if($num > 0){
	echo "Username already taken";
}
else{
	$sql = "INSERT INTO Doctors(first_name, last_name, username, password, background, qualifications, experience, address_line_1_2, city, county, country) VALUES('$first_name', '$last_name', '$username', '$password', '$background', '$qualifications', '$experience', '$address_line', '$city', '$county', '$country')";

	if(mysqli_query($con, $sql)){
		echo "Registration Successful";
	}
	else{
		echo "Register failed";
		//echo mysqli_error($con);
	}
}
?>