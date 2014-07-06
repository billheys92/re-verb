<?php
$link = mysqli_connect('localhost', 'root', '', 'trying');
if (!$link) {
    echo 'Could not connect to mysql';
    exit;
}


mysqli_query($link,"INSERT INTO Student (NAME, ROLLNO)
VALUES ('James', 10)");


mysqli_close($link);
?>