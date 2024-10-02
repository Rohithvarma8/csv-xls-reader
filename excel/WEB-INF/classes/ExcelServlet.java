import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

@WebServlet("*.xls")
public class ExcelServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("version 2 at 12:47");
        try {
            // Path to your Excel file
            String filePath = "/Users/rohithvarmadatla/Documents/ESD/apache-tomcat-9.0.94/webapps/excel/WEB-INF/xls-files/store.xls";

            // Load the Excel file
            FileInputStream fis = new FileInputStream(filePath);
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet sheet = wb.getSheetAt(0);

            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<html><body>");
            out.println("<h2>Store Excel Sheet is Displayed</h2>");
            out.println("<table border='1'>");

            for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                HSSFRow row = sheet.getRow(r);
                out.println("<tr>");
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    HSSFCell cell = row.getCell(c);
                    out.println("<td>" + (cell != null ? cell.toString() : "") + "</td>");
                }
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

            // Close resources
            wb.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
