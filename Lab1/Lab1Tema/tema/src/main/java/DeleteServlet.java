import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

public class DeleteServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            request.getRequestDispatcher("/delete-student.jsp").forward(request, response);
        }
        catch(Exception e){
            throw new ServletException("Error in DeleteServlet doGet",e);
        }
    }
}
