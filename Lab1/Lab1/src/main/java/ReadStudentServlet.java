import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class ReadStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{// deserializare student din fisierul XML de pe disc
        File file = new File("/home/tudor/AN3/SD/Lab1/Storage/StudentEx1.xml");
// se returneaza un raspuns HTTP de tip 404 in cazul incarenu se gaseste fisierul cu date
        if (!file.exists()) {
            response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            return;
        }
        XmlMapper xmlMapper = new XmlMapper();
        StudentBean bean = xmlMapper.readValue(file,
                StudentBean.class);
        request.setAttribute("nume", bean.getNume());
        request.setAttribute("prenume", bean.getPrenume());
        request.setAttribute("varsta", bean.getVarsta());

        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}