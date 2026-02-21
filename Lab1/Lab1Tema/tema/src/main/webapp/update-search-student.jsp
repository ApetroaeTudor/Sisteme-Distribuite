<html xmlns:jsp="http://java.sun.com/JSP/Page">

<head>
    <title>Formular student</title>
    <meta charset="UTF-8">
</head>

<body>
    <h3>Formular student</h3>
    Ce student sa fie actualizat?
    <form action="./hit/db" method="get">
        Nume: <input type="text" name="nume" />
        <br />
        <input type="hidden" name="action" value="read">
        <input type="hidden" name="updating" value="true">
        <button type="submit" name="submit">Trimite</button>
    </form>
</body>

</html>