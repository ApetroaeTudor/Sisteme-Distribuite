import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            request.getRequestDispatcher("/search-student.jsp").forward(request, response);
        }
        catch(Exception e){
            throw new ServletException("Error in SearchServlet doGet");
        }
    }
}