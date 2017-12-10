/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author janto
 */
public class CheckUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DataSource datasource;
    Statement statement = null;
    Connection connection = null;

    @Override
    public void init() {

        try {
            InitialContext initialContext = new InitialContext();
            datasource = (DataSource) initialContext.lookup("jdbc/sudoku2");
        } catch (NamingException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
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

        ServletContext contexto = request.getServletContext();
        HttpSession cliente = request.getSession();
        String user = (String) cliente.getAttribute("user");

        try {

            String query = null;
            System.out.println(user + "segundo paso<<<<<<<<<<<<<<<<<");
            query = "SELECT * FROM login WHERE user like '" + user + "'";
            System.out.println(user + "tercer paso<<<<<<<<<<<<<<<<<");
            ResultSet resulSet = null;
            connection = datasource.getConnection();
            statement = connection.createStatement();
            resulSet = statement.executeQuery(query);

            //con el while si el user y passwd estan en la bbdd lo ejecuta
            while (resulSet.next()) {

                RequestDispatcher paginaError
                        = contexto.getRequestDispatcher("/errorPassword.html");

                paginaError.forward(request, response);
            }
            RequestDispatcher paginaError
                    = contexto.getRequestDispatcher("/errorUser.html");

            paginaError.forward(request, response);

        } catch (SQLException ex) {
            System.out.println(ex);

        }

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

        String user = request.getParameter("user");
        String password = request.getParameter("password");
        HttpSession cliente = request.getSession();
        cliente.setAttribute("user", user);
        cliente.setAttribute("password", password);
        System.out.println("user" + user + "password" + password);

        ServletContext contexto = request.getServletContext();

        try {

            String query = null;
            query = "SELECT * FROM login WHERE user like '" + user + "'";
            ResultSet resulSet = null;
            connection = datasource.getConnection();
            statement = connection.createStatement();
            resulSet = statement.executeQuery(query);

            //con el while si el user 
            while (resulSet.next()) {

                RequestDispatcher paginaError
                        = contexto.getRequestDispatcher("/esteUsuarioYaExiste.html");

                paginaError.forward(request, response);
            }
            if (!(user.length() > 3) || password.equals("")) {
                response.sendRedirect("registroIncorrecto.html");
            } else {

                response.sendRedirect("ServletHash");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
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
