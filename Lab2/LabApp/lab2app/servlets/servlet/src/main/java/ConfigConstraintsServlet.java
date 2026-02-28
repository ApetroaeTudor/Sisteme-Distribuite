import ejb.StudentEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ConfigConstraintsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer minAge = Integer.parseInt(request.getParameter("minAge"));
        Integer maxAge = Integer.parseInt(request.getParameter("maxAge"));
        String forbiddenSequence = request.getParameter("forbiddenSequence");
        
        MonitoringManager.forbiddenSequence = forbiddenSequence;
        MonitoringManager.minAge = minAge;
        MonitoringManager.maxAge = maxAge;

        response.sendRedirect("/servlet/");
    }
}