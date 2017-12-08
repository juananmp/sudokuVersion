/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matriz;

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
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckUserPassword</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckUserPassword at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        
        PrintWriter out = response.getWriter();
        out.println("LLEGASTE");
        
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        ServletContext contexto = request.getServletContext();
         try {
            
          String query=null;
           query = "SELECT * FROM login WHERE user like '"+user+"' AND password='"+password+"'";
           ResultSet resulSet = null;
           connection = datasource.getConnection();
           statement = connection.createStatement();
           resulSet = statement.executeQuery(query);
          
           //con el while si el user y passwd estan en la bbdd lo ejecuta
           while(resulSet.next()){
       out.println(user+password);;
           } 
           //out.println("no he enocntrado valores");
           //out.println("<form method=\"post\" action=\"/sudokuVersion/CheckUser\"><input type=\"hidden\" name=\"\" value=\"algo\"><button>Comprobar</button></form>");
            // response.sendRedirect(request.getContextPath() + "/CheckUser");
                    RequestDispatcher paginaError
                        = contexto.getRequestDispatcher("/CheckUser");
                paginaError.forward(request, response);
       } catch (SQLException ex) {
             System.out.println(ex);
          
       }
        processRequest(request, response);
    }

    

//        if (login.existeCuenta(user, password)) {
//            System.out.println("Entro");
//            if (!name.equals("")) {
//
//                System.out.println("vacio");
//
////                RequestDispatcher anhadirServlet =
////                    //contexto.getNamedDispatcher("Matriz");
////                 //contexto.getRequestDispatcher("/login.html");
////                contexto.getNamedDispatcher("ServletCookie2");
////                  anhadirServlet.forward(request, response);
//                Cookie ck = new Cookie("name", name);
//
//                response.addCookie(ck);
//
//                // out.println("<a href='./ServletCookie2'> ServletCookie2</a>"); 
//                //mejor redirect que forwars porque con forward me daba error
//                response.sendRedirect(request.getContextPath() + "/ServletCookie2");
//
//            } else {
//
//                System.out.println("campo nulo");
//
//                RequestDispatcher paginaError
//                        = contexto.getRequestDispatcher("/errorCookie.html");
//
//                paginaError.forward(request, response);
//
//            }
//
//        } else {
//
//            System.out.println("No Entro");
//
//            RequestDispatcher paginaError
//                    = contexto.getRequestDispatcher("/error.html");
//
//            paginaError.forward(request, response);
//
//        }
//
//        processRequest(request, response);
//
//    }
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
