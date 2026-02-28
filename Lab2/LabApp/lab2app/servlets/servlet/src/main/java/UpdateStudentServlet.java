import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UpdateStudentServlet extends HttpServlet {
    private Logger LOGGER = Logger.getLogger(UpdateStudentServlet.class.getName());
    private MonitoringManager m_monitor;

    private EntityManagerFactory m_factory;
    private EntityManager m_em;

    @Override
    public void init(){
        m_monitor = new MonitoringManager();
        m_monitor.start();
        // pregatire EntityManager
        m_factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        m_em = m_factory.createEntityManager();
    }   

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String action = request.getParameter("action");

        String prenume = new String();
        Integer varsta = new Integer(0);
        if(action.equals("update")){
            prenume = request.getParameter("prenume");
            varsta = Integer.parseInt(request.getParameter("varsta"));
        }

        // adaugare entitate in baza de date (operatiune de persistenta)
        // se face intr-o tranzactie
        EntityTransaction transaction = m_em.getTransaction();
        transaction.begin();

        TypedQuery<Integer> query = m_em.createQuery(
                "SELECT s.id from StudentEntity s WHERE s.nume = :searchedName", Integer.class)
                .setParameter("searchedName", nume);

        List<Integer> results = query.getResultList();

        for (Integer result : results) {
            if (action.equals("update")) {
                int updateQuery = m_em.createQuery(
                        "UPDATE StudentEntity s SET s.nume = :newNume, s.prenume = :newPrenume, s.varsta = :newVarsta WHERE s.id = :varId")
                        .setParameter("varId", result)
                        .setParameter("newNume", nume)
                        .setParameter("newPrenume", prenume)
                        .setParameter("newVarsta", varsta).executeUpdate();
            } else if (action.equals("delete")) {
                LOGGER.info("In delete branch");
                int deleteQuery = m_em.createQuery(
                    "DELETE FROM StudentEntity s WHERE s.id = :varId")
                    .setParameter("varId", result).executeUpdate();
            }
        }
        m_monitor.dbChecked = false;

        transaction.commit();
        
        while(!m_monitor.dbChecked){

        }

        // inchidere EntityManager


        response.sendRedirect("/servlet/display-error");
    }

    @Override
    public void destroy(){
        m_em.close();
        m_factory.close();
        if(m_monitor != null){
            m_monitor.running = false;
            try{
                m_monitor.join();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}