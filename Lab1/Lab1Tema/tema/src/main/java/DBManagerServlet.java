import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import beans.StudentBean;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBManagerServlet extends HttpServlet {
    private Connection m_dbConnection;

    private Logger m_logger;

    private final String m_queryCreateTable = """
            CREATE TABLE IF NOT EXISTS studenti (
            nume TEXT PRIMARY KEY,
            prenume TEXT,
            varsta NUMBER
            );
            """;

    private final String m_queryInsertStudent = """
            INSERT INTO studenti(nume, prenume, varsta) values (?,?,?)
            """;

    private final String m_querySelectStudentByName = """
            SELECT * FROM studenti WHERE studenti.nume = ?
            """;

    private final String m_querySelectAll = """
            SELECT * FROM studenti
            """;

    private final String m_queryUpdateStudentByName = """
            UPDATE studenti
            SET nume = ?, prenume = ?, varsta = ?
            WHERE nume = ?;
            """;

    private final String m_queryDeleteStudentByName = """
            DELETE FROM studenti
            WHERE nume = ?
            """;

    @Override
    public void init() throws ServletException {
        super.init();

        m_logger = Logger.getLogger(DBManagerServlet.class.getName());
        m_logger.log(Level.INFO, "In init");

        try {
            Class.forName("org.sqlite.JDBC");

            m_dbConnection = DriverManager.getConnection(Defines.dbUrl);
            var stmt = m_dbConnection.createStatement();
            stmt.executeUpdate(m_queryCreateTable);

        } catch (Exception e) {
            throw new ServletException("Failed to initialize Database", e);
        }
    }

    private void CreateStudent(StudentBean student) throws ServletException {
        try {
            var query = m_dbConnection.prepareStatement(m_queryInsertStudent);
            query.setString(1, student.getNume());
            query.setString(2, student.getPrenume());
            query.setInt(3, student.getVarsta());

            query.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Error in CreateStudent", e);
        }
    }

    private StudentBean ReadStudent(String studentToSearch) throws ServletException {
        StudentBean bean = new StudentBean();
        try {
            var query = m_dbConnection.prepareStatement(m_querySelectStudentByName);
            query.setString(1, studentToSearch);
            var resultSet = query.executeQuery();
            while (resultSet.next()) {
                bean.setNume(resultSet.getString("nume"));
                bean.setPrenume(resultSet.getString("prenume"));
                bean.setVarsta(resultSet.getInt("varsta"));
                bean.setPopulated(true);
            }
        } catch (SQLException e) {
            throw new ServletException("Error in ReadStudent", e);
        }
        return bean;
    }

    private ArrayList<StudentBean> ReadAll() throws ServletException {
        var resultFinal = new ArrayList<StudentBean>();
        try {
            var query = m_dbConnection.prepareStatement(m_querySelectAll);
            var resultSet = query.executeQuery();
            while (resultSet.next()) {
                var bean = new StudentBean();
                bean.setNume(resultSet.getString("nume"));
                bean.setPrenume(resultSet.getString("prenume"));
                bean.setVarsta(resultSet.getInt("varsta"));
                resultFinal.add(bean);
            }
        } catch (SQLException e) {
            throw new ServletException("Error in ReadAll", e);
        }
        return resultFinal;
    }

    private void UpdateStudent(String studentToUpdate, StudentBean student) throws ServletException {
        try {
            var query = m_dbConnection.prepareStatement(m_queryUpdateStudentByName);
            query.setString(1, student.getNume());
            query.setString(2, student.getPrenume());
            query.setInt(3, student.getVarsta());
            query.setString(4, studentToUpdate);

            query.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Error in UpdateStudent", e);
        }
    }

    private void DeleteStudent(String studentToDelete) throws ServletException {
        try {
            var query = m_dbConnection.prepareStatement(m_queryDeleteStudentByName);
            query.setString(1, studentToDelete);

            query.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Error in DeleteStudent", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String actionParameter = request.getParameter(Defines.PARAM_ACTION);
        Object actionAttribute = request.getAttribute(Defines.PARAM_ACTION);
        String action;

        if (actionParameter == null) {
            if (actionAttribute == null) {
                action = null;
            } else {
                action = actionAttribute.toString();
            }
        } else {
            action = actionParameter;
        }

        ArrayList<StudentBean> students;
        try {
            if (action == null) {
                response.sendError(Defines.BAD_REQUEST, "Request to DB manager must have an action");
                return;
            }
            switch (action) {
                case Defines.READ:
                    var bean = this.ReadStudent(request.getParameter("nume"));
                    if(!bean.getPopulated()){
                        request.setAttribute("error","Searched Student not found!");
                        request.getRequestDispatcher("/generic-error.jsp").forward(request, response);
                        break;
                    }

                    var updating = request.getParameter("updating");
                    if (updating == null) {
                        request.setAttribute("prenume", bean.getPrenume());
                        request.setAttribute("varsta", bean.getVarsta());
                        students = new ArrayList<>();
                        students.add(bean);
                        request.setAttribute("studenti", students);
                        request.getRequestDispatcher("/result-student.jsp").forward(request, response);
                    } else {
                        request.setAttribute("nume", bean.getNume());
                        request.setAttribute("prenume", bean.getPrenume());
                        request.setAttribute("varsta", bean.getVarsta());
                        request.getRequestDispatcher("/update-form-student.jsp").forward(request, response);
                    }

                    break;
                case Defines.READ_ALL:
                    students = ReadAll();
                    request.setAttribute("studenti", students);

                    response.setHeader(Defines.OPERATION_STATUS, Defines.OPERATION_SUCCESS);
                    request.getRequestDispatcher("/result-student.jsp").forward(request, response);

                    break;

                case Defines.DUMP_JSON:
                    students = ReadAll();
                    JSONArray finalJson = new JSONArray(); 
                    for(var student : students){
                        finalJson.put(student.toJson());
                    }
                    response.getWriter().write(finalJson.toString(5));
                    break;
                default:
                    response.sendError(Defines.BAD_REQUEST, "GET request to DB manager must be of type READ");
                    break;

            }
        } catch (Exception e) {
            throw new ServletException("Error in doGet", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter(Defines.PARAM_ACTION);
        m_logger.log(Level.INFO, "Action in doPost: " + action);
        StudentBean bean = new StudentBean();
        try {
            if (action == null) {
                response.sendError(Defines.BAD_REQUEST, "Request to DB manager must have an action");
                return;
            }
            switch (action) {
                case Defines.CREATE:
                    m_logger.log(Level.INFO, "In doPost CREATE branch");

                    bean.setNume(request.getParameter("nume"));
                    bean.setPrenume(request.getParameter("prenume"));
                    bean.setVarsta(Integer.parseInt(request.getParameter("varsta")));

                    m_logger.log(Level.INFO, "Student is" + bean.toString());
                    CreateStudent(bean);

                    response.setHeader(Defines.OPERATION_STATUS, Defines.OPERATION_SUCCESS);
                    response.sendRedirect(request.getContextPath() + "/");

                    break;
                case Defines.UPDATE:
                    m_logger.log(Level.INFO,"In UPDATE branch");

                    bean.setNume(request.getParameter("nume"));
                    bean.setPrenume(request.getParameter("prenume"));
                    bean.setVarsta(Integer.parseInt(request.getParameter("varsta")));

                    UpdateStudent(request.getParameter("nume"), bean);
                    response.setHeader(Defines.OPERATION_STATUS, Defines.OPERATION_SUCCESS);
                    response.sendRedirect(request.getContextPath() + "/");
                    
                    break;

                case Defines.DELETE:
                    DeleteStudent(request.getParameter("nume"));

                    response.setHeader(Defines.OPERATION_STATUS, Defines.OPERATION_SUCCESS);
                    response.sendRedirect(request.getContextPath() + "/");
                    break;

                default:
                    response.sendError(Defines.BAD_REQUEST, "Invalid action in POST method to DB manager");
                    break;
            }

        } catch (Exception e) {
            throw new ServletException("Error in doPost", e);
        }
    }
}
