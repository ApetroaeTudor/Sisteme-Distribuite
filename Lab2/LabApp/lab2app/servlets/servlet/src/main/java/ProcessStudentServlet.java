import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessStudentServlet extends HttpServlet {

    private MonitoringManager m_monitor;
    private EntityManagerFactory m_factory;
    private EntityManager m_em;

    @Override
    public void init() {
        m_monitor = new MonitoringManager();
        m_factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        m_em = m_factory.createEntityManager();
        m_monitor.start();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // se citesc parametrii din cererea de tip POST
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));

        // creare entitate JPA si populare cu datele primite din formular
        StudentEntity student = new StudentEntity();
        student.setNume(nume);
        student.setPrenume(prenume);
        student.setVarsta(varsta);

        // adaugare entitate in baza de date (operatiune de persistenta)
        // se face intr-o tranzactie
        EntityTransaction transaction = m_em.getTransaction();
        transaction.begin();
        m_em.persist(student);

        m_monitor.dbChecked = false;
        transaction.commit();

        while(!m_monitor.dbChecked){

        }

        response.sendRedirect("/servlet/display-error");
    }

    @Override
    public void destroy() {
        m_em.close();
        m_factory.close();
        if (m_monitor != null) {
            m_monitor.running = false;
            try {
                m_monitor.join();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}