<html xmlns:jsp="http://java.sun.com/JSP/Page">

<head>
    <title>Configureaza constrangeri</title>
    <meta charset="UTF-8" />
</head>

<body>
    Introduceti datele despre constrangeri:
    <form action="./config" method="post">
        Varsta Minima: <input type="number" name="minAge" />
        <br />
        Varsta Maxima: <input type="number" name="maxAge" />
        <br />
        Secventa interzisa in nume: <input type="text" name="forbiddenSequence" />
        <br />
        <br />
        <button type="submit" name="submit">Trimite</button>
    </form>
</body>

</html>