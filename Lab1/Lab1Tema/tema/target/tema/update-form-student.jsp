<html xmlns:jsp="http://java.sun.com/JSP/Page">

<head>
    <title>Informatii student</title>
</head>

<body>
    <h3>Informatii student</h3>
    <form action="/tema/hit/db" method="post">
        Nume:
        <input type="text" name="nume" value="<%= request.getAttribute("nume") %>" />
        <br />

        Prenume:
        <input type="text" name="prenume" value="<%= request.getAttribute("prenume") %>" />
        <br />

        Varsta:
        <input type="number" name="varsta" value="<%= request.getAttribute("varsta") %>" />
        <br /><br />

        <input type="hidden" name="action" value="update">


        <button type="submit" name="submit">Update</button>
    </form>
</body>

</html>