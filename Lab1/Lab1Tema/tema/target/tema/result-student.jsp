<%@ page import="beans.StudentBean" %>
<%@ page import="java.util.ArrayList" %>

<html xmlns:jsp="http://java.sun.com/JSP/Page">

<body>
    <li>Studenti: 
        <%
        Object obj=request.getAttribute("studenti"); 
        if (obj !=null) {
            ArrayList<StudentBean> studenti = (ArrayList<StudentBean>) obj;
            for(StudentBean b : studenti){
                out.print("<br /> <br />" + b.toString());
            }
        } else { 
            out.print("\nNu sunt studenti inregistrati"); 
        } 
        %>
    </li>

    <br />
    <a href="/tema">Return</a>
    <br />
    <a href="/tema/create">Create</a>
    <br />
    <a href="/tema/delete">Delete</a>
    <br />
    <a href="/tema/update">Update</a>
</body>

</html>