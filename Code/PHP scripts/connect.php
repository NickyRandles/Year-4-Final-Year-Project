<?php

$db_name = "medica12_database";
$db_user = "medica12_Nicky";
$db_pass = "datapass1234";
$db_server = "localhost";

$con = mysqli_connect($db_server, $db_user, $db_pass, $db_name);

if(!$con){
	//echo "Connection error";
	//echo mysqli_connect_error();
}
else{
	//echo "Connection successful";
}

?>