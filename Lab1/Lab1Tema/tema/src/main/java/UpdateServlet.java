import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateServlet extends HttpServlet{
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            request.getRequestDispatcher("/update-search-student.jsp").forward(request, response);
        }
        catch(Exception e){
            throw new ServletException("Error in UpdateServlet doGet",e);
        }
    }
    // in update-student.jsp face un read, adauga un parametru de tip "updating"
    // in dbmanager se verifica parametrul, daca exista se trimite catre un jsp cu formular care completeaza din bean
    // formularul trimite un post la dbmanager care salveaza update si dupa se intoarce la index
}
