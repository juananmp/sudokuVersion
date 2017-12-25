package Matrizsudokuversion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
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
 *
 *
 * @author janto
 *
 */
public class CheckUser extends HttpServlet {

    DataSource datasource;
    
    //abre conexion con la Base de Datos
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
     *
     * Handles the HTTP <code>GET</code> method.
     *
     *
     *
     * @param request servlet request
     *
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     *
     * @throws IOException if an I/O error occurs
     *
     */
    //Verifica si existe Usuario
    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Statement statement = null;

        Connection connection = null;
        ServletContext contexto = request.getServletContext();

        //Recuperamos usuario de la sesion
        HttpSession cliente = request.getSession();

        String user = (String) cliente.getAttribute("user");

        //verificamos si el usuario existe en la base de datos
        try {

            String query = null;

            query = "SELECT * FROM login WHERE user like '" + user + "'";

            ResultSet resulSet = null;

            connection = datasource.getConnection();

            statement = connection.createStatement();

            resulSet = statement.executeQuery(query);

            //si existe el usuario se indica error en password
            while (resulSet.next()) {

                RequestDispatcher paginaError
                        = contexto.getRequestDispatcher("/./faces/errorPassword.xhtml");

                paginaError.forward(request, response);

            }

            // Si no existe el usuario, se indica error en el usuario
            RequestDispatcher paginaError
                    = contexto.getRequestDispatcher("/./faces/errorUser.xhtml");

            paginaError.forward(request, response);

            statement.close();
            connection.close();

        } catch (SQLException ex) {

            System.out.println(ex);

        }

    }

    /**
     *
     * Handles the HTTP <code>POST</code> method.
     *
     *
     *
     * @param request servlet request
     *
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     *
     * @throws IOException if an I/O error occurs
     *
     */
    //Da de alta un nuevo usuario
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Statement statement = null;

        Connection connection = null;
        String user = request.getParameter("user");

        String password = request.getParameter("password");

        String password2 = request.getParameter("password2");

        HttpSession cliente = request.getSession();

        cliente.setAttribute("user", user);

        cliente.setAttribute("password", password);

        ServletContext contexto = request.getServletContext();

        //Verifica si el usuario ya existe
        try {

            String query = null;

            query = "SELECT * FROM login WHERE user like '" + user + "'";

            ResultSet resulSet = null;

            connection = datasource.getConnection();

            statement = connection.createStatement();

            resulSet = statement.executeQuery(query);

            //Si el usuario ya existe, indica error
            while (resulSet.next()) {

                RequestDispatcher paginaError
                        = contexto.getRequestDispatcher("/faces/esteUsuarioYaExiste.xhtml");

                paginaError.forward(request, response);

            }

            //Si usuario no existe pero su longitud es inferior a 4 o no introduce la password, error
            if (!(user.length() > 3) || !(password.length() > 3) || !(password.equals(password2))) {

                response.sendRedirect("/sudokuVersion/faces/registroIncorrecto.xhtml");

                //en caso contrario, encriptar password y dar de alta usuario
            } else {

                response.sendRedirect("ServletHash");

            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {

            System.out.println(ex);

        }

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
     *
     * Returns a short description of the servlet.
     *
     *
     *
     * @return a String containing servlet description
     *
     */
    @Override

    public String getServletInfo() {

        return "Short description";

    }// </editor-fold>

}
