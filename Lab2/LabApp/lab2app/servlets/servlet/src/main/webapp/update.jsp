<html xmlns:jsp="http://java.sun.com/JSP/Page">

<head>
    <title>Actualizeaza student</title>
    <meta charset="UTF-8" />
</head>

<body>
    <form action="./update-student" method="post">
        Nume: <input type="text" name="nume" />
        Prenume: <input type="text" name="prenume" />
        <br />
        Varsta: <input type="number" name="varsta" />
        <br />
        <br />
        <input type="hidden" name="action" value="update">
        <button type="submit" name="submit">Trimite</button>
    </form>
</body>

</html>