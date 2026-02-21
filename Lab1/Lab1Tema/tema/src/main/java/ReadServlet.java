import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ReadServlet extends HttpServlet{
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute(Defines.PARAM_ACTION, Defines.READ_ALL);
        try{
            request.getRequestDispatcher("/hit/db").forward(request, response);
        }
        catch(Exception e){
            throw new ServletException("Error in ReadServlet doGet",e);
        }
    }
}
