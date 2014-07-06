<?php
$link = mysqli_connect('localhost', 'root', '', 'trying');
if (!$link) {
    echo 'Could not connect to mysql';
    exit;
}

$command = $_GET["command"]; 

mysqli_query($link,$command);


mysqli_close($link);
?>