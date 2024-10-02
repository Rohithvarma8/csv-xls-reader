import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/readFile.xls")
public class CsvReader extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("fileName");
        // Append .csv if not present
        if (!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("Reading file: " + fileName);

            if (fileName == null) {
                throw new IllegalArgumentException("Invalid file name. Please provide a valid CSV file name.");
            }
            String directoryPath = "/Users/rohithvarmadatla/Documents/ESD/apache-tomcat-9.0.94/webapps/secure-csv-handle/";
            System.out.println("Connecting to directory: " + directoryPath);
            conn = DriverManager.getConnection("jdbc:relique:csv:" + directoryPath);
            stmt = conn.createStatement();

            String tableName = fileName.replace(".csv", "");
            System.out.println("Querying table: " + tableName);
            
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>CSV Data:</h1>");
            out.println("<table border='1'>");

            // Print table 
            int columnCount = rs.getMetaData().getColumnCount();
            out.println("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                out.println("<th>" + escapeHtml(rs.getMetaData().getColumnName(i)) + "</th>");
            }
            out.println("</tr>");

            // Print table rows
            while (rs.next()) {
                out.println("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<td>" + escapeHtml(rs.getString(i)) + "</td>");
                }
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String escapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
