<?php
$link = mysqli_connect('localhost', 'root', '', 'trying');
if (!$link) {
    echo 'Could not connect to mysql';
    exit;
}


$sql    = "SELECT * FROM Student WHERE ROLLNO = 10";
$result = mysqli_query($link, $sql);

if (!$result) {
    echo "DB Error, could not query the database\n";
    echo 'MySQL Error: ' . mysql_error();
    exit;
}

while ($row = mysqli_fetch_assoc($result)) {
    echo $row['NAME'];
    echo "\n";
}

mysqli_free_result($result);

mysqli_close($link);
?>