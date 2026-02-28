<html xmlns:jsp="http://java.sun.com/JSP/Page">

<head>
    <title>Sterge student</title>
    <meta charset="UTF-8" />
</head>

<body>
    <form action="./update-student" method="post">
        Nume: <input type="text" name="nume" />
        <input type="hidden" name="action" value="delete">
        <button type="submit" name="submit">Trimite</button>
    </form>
</body>

</html>