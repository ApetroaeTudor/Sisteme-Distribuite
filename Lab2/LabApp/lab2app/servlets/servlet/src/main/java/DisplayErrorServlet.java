import ejb.StudentEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class DisplayErrorServlet extends HttpServlet {
    private Logger LOGGER = Logger.getLogger(UpdateStudentServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        synchronized(MonitoringManager.notifications){
            if(MonitoringManager.notifications.isEmpty()){
                response.sendRedirect("/servlet/");
            }
        }

        response.setContentType("text/html");
        synchronized(MonitoringManager.notifications){
            for(String notif : MonitoringManager.notifications){
                response.getWriter().println(notif+"<br />");
            }
            MonitoringManager.notifications.clear();
        }

        response.getWriter().println("<br /><br /><a href='./'>Inapoi la meniul principal</a>");
    }
}