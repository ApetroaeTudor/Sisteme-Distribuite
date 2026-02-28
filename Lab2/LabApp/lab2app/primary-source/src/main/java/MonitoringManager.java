import java.util.ArrayList;
import java.util.List;
import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class MonitoringManager extends Thread {
    public static List<String> notifications = new ArrayList<>();
    public static volatile Integer minAge = 0;
    public static volatile Integer maxAge = 99;
    public static volatile String forbiddenSequence = "";

    public volatile boolean running = true;
    public volatile boolean dbChecked = false;
    public static long pollingMsec = 1000;

    private EntityManagerFactory m_factory;
    private EntityManager m_em;

    MonitoringManager() {
        m_factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        m_em = m_factory.createEntityManager();
    }

    @Override
    public void run() {
        while (running) {
            {
                EntityTransaction transaction = m_em.getTransaction();
                transaction.begin();

                TypedQuery<StudentEntity> query = m_em.createQuery(
                        "select s from StudentEntity s WHERE s.varsta < :min or s.varsta > :max",
                        StudentEntity.class).setParameter("min", minAge)
                        .setParameter("max", maxAge);

                List<StudentEntity> foundStudents = query.getResultList();
                for (StudentEntity student : foundStudents) {
                    synchronized (notifications) {
                        StringBuilder notif = new StringBuilder(
                                "Student cu varsta invalida: " + student.toString() + "<br />");
                        if (student.getVarsta() < minAge) {
                            notif.append(student.getVarsta() + "<" + Integer.toString(minAge));
                        } else if (student.getVarsta() > maxAge) {
                            notif.append(student.getVarsta() + ">" + Integer.toString(maxAge));
                        }
                        notif.append("<br />");

                        notifications.add(notif.toString());
                    }
                    int id = student.getId();
                    m_em.createQuery(
                            "DELETE FROM StudentEntity s WHERE s.id = :varId")
                            .setParameter("varId", id).executeUpdate();
                }

                transaction.commit();
            }

            {
                EntityTransaction transaction = m_em.getTransaction();
                transaction.begin();
                TypedQuery<StudentEntity> query = m_em.createQuery(
                        "select s from StudentEntity s WHERE s.nume like :secv",
                        StudentEntity.class).setParameter("secv","%"+forbiddenSequence+"%");
                List<StudentEntity> foundStudents = query.getResultList();

                for (StudentEntity student : foundStudents) {
                    synchronized (notifications) {
                        StringBuilder notif = new StringBuilder(
                                "Student cu nume invalid: " + student.toString() + "<br />");
                        notif.append(
                                "Numele " + student.getNume() + " contine secventa interzisa: " + forbiddenSequence);
                        notif.append("<br />");

                        notifications.add(notif.toString());
                    }
                    // int id = student.getId();
                    m_em.remove(student);
                    // m_em.createQuery(
                    //         "DELETE FROM StudentEntity s WHERE s.id = :varId")
                    //         .setParameter("varId", id).executeUpdate();
                }

                transaction.commit();

            }

            dbChecked = true;

            try {
                Thread.sleep(pollingMsec);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
