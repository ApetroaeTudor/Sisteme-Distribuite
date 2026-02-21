import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DumpJsonServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute(Defines.PARAM_ACTION, Defines.DUMP_JSON);
        try{
            request.getRequestDispatcher("/hit/db").forward(request, response);
        }
        catch(Exception e){
            throw new ServletException("Error in DumpJsonServlet doGet", e);
        }
    }
}
