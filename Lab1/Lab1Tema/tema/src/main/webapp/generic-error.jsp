<html xmlns:jsp="http://java.sun.com/JSP/Page">

<body>
    <li>Error: 
        <%
        var errorText = request.getAttribute("error");
        if(errorText == null){
            out.print("Unknown");
        }
        else{
            out.print(errorText);
        }
        %>
    </li>

    <br />
    <a href="/tema">Return</a>
    <br />
</body>

</html>