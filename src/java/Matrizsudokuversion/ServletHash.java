package Matrizsudokuversion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author janto
 */
public class ServletHash extends HttpServlet {

    DataSource datasource;
//    Statement statement = null;
//    Connection connection = null;

    //Se abre conexion con la base de datos
    @Override
    public void init() {

        try {
            InitialContext initialContext = new InitialContext();
            datasource = (DataSource) initialContext.lookup("jdbc/sudoku2");
        } catch (NamingException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        

        ServletContext context = getServletConfig().getServletContext();

        //Recuperacion de parametros para hash
        int primo1 = Integer.parseInt(context.getInitParameter("primo1"));
        int primo2 = Integer.parseInt(context.getInitParameter("primo2"));

        //Leer usuario y password desde sesion
        HttpSession cliente = request.getSession();
        String user = (String) cliente.getAttribute("user");
        String password = (String) cliente.getAttribute("password");

        ServletContext contexto = request.getServletContext();
        //Encriptar password con hash
        int result = primo1;
        result = primo2 * result + password.hashCode();

        //Insertar nuevo usuario y password encriptadda en la tabla de login
        String query = null;

        query = "INSERT INTO login VALUES ('" + user + "', '" + result + "')";
        Statement statement = null;
        Connection connection = null;
        try {
            connection = datasource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);

            request.setAttribute("nextPage", this.getServletContext().getContextPath() + "/ServletHash");

//            RequestDispatcher altaUser
//                    = contexto.getRequestDispatcher("/paginaExitoRegistro.html");
//            altaUser.forward(request, response);

            response.sendRedirect("/sudokuVersion/faces/paginaExitoRegistro.xhtml");

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    //Cierra conexion con la Base de datos
    @Override
    public void destroy() {
         Statement statement = null;
    Connection connection = null;
        try {

            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
