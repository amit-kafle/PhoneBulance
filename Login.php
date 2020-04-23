<?php
require "init.php";
$phone= $_POST["phone"]; 
$password= $_POST["password"];
/*
$phone= "9842139568"; 
$password= "12345678"; 
*/  
$isValidPhone=filter_var($phone);
if($conn){
if($isValidPhone===false)
{
echo"This Phone is not valid";
}else{
$sqlCheckPhone="SELECT * from `userlogin` WHERE `phone` LIKE '$phone'";
$PhoneQuery=mysqli_query($conn,$sqlCheckPhone);
if(mysqli_num_rows($PhoneQuery)>0){
$sqlLogin="SELECT * from `userlogin` WHERE `phone` LIKE '$phone' AND `password` LIKE '$password'";
$loginQuery=mysqli_query($conn,$sqlLogin);
if(mysqli_num_rows($loginQuery)>0){
echo"Login Success";
}else
{
echo"Wrong password";
}
}else{
echo"Phone is already used";
}
}
}
else{
echo"Connection Error";
}
?>