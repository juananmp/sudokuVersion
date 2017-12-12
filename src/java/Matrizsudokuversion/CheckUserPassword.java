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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author janto
 */
public class CheckUserPassword extends HttpServlet {

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

        //si en el login se pudiera elegir el numero de sudoku (o la dificultad), aqui recogeriamos el valor con un getParameter
        int numSudoku = 1;

        //creo sesion y guardo los datos user, password y numSudoku para su uso en los servlets posteriores
        HttpSession cliente = request.getSession();
        cliente.setAttribute("user", user);
        cliente.setAttribute("numSudoku", numSudoku);
        ServletContext context = getServletConfig().getServletContext();
        int primo2 = Integer.parseInt(context.getInitParameter("primo2"));

        int primo1 = Integer.parseInt(context.getInitParameter("primo1"));

        ServletContext contexto = request.getServletContext();
        int result = primo1;
        result = primo2 * result + password.hashCode();

        try {

            String query = null;
            query = "SELECT * FROM login WHERE user like '" + user + "' AND password='" + result + "'";
            ResultSet resulSet = null;
            connection = datasource.getConnection();
            statement = connection.createStatement();
            resulSet = statement.executeQuery(query);

            //con el while si el user y passwd estan en la bbdd lo ejecuta
            if (resulSet.next()) {
                String recordar = request.getParameter("recordar");
                if(recordar!= null){
                 Cookie ck = new Cookie(user, password);
                 ck.setMaxAge(60 * 60 * 24 * 7);
                 response.addCookie(ck);
                }
                
                RequestDispatcher facelet
                        = contexto.getRequestDispatcher("/./faces/faceletInicio.xhtml");

                facelet.forward(request, response);

            } else {

                response.sendRedirect("CheckUser");

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
