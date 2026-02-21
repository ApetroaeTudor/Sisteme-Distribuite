import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Year;
public class ProcessStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
// se citesc parametrii din cererea de tip POST
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));/*
procesarea datelor - calcularea anului nasterii
*/
        int anCurent = Year.now().getValue();
        int anNastere = anCurent - varsta;
// se trimit datele primite si anul nasterii catre o altapagina JSP pentru afisare
        request.setAttribute("nume", nume);
        request.setAttribute("prenume", prenume);
        request.setAttribute("varsta", varsta);
        request.setAttribute("anNastere", anNastere);

        StudentBean bean = new StudentBean();
        bean.setNume(nume);
        bean.setPrenume(prenume);
        bean.setVarsta(varsta);
        XmlMapper mapper = new XmlMapper();
        mapper.writeValue(new File("/home/tudor/AN3/SD/Lab1/Storage/StudentEx1.xml"), bean);
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}