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
  // int hitCount;
   
   @Override
    public void init() {
     //    hitCount=0;
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
         String user = request.getParameter("user");
        String password = request.getParameter("password");
        
        System.out.println("getServletContext():"+ getServletContext());
       ServletContext context = getServletConfig().getServletContext();
        int primo = Integer.parseInt(context.getInitParameter("primo"));
        System.out.println("primo"+primo);
        
        
       
         ServletContext contexto = request.getServletContext();
         int result=17;
         result= primo*result+password.hashCode();
     
         System.out.println("valores hashcode user password"+user+password+result);
       
   
         try {
            
          String query=null;
           query = "SELECT * FROM login WHERE user like '"+user+"' AND password='"+result+"'";
           ResultSet resulSet = null;
           connection = datasource.getConnection();
           statement = connection.createStatement();
           resulSet = statement.executeQuery(query);
          
           //con el while si el user y passwd estan en la bbdd lo ejecuta
           if(resulSet.next()){
               //crearse sesion de cliente y guardar en la sesion el atributo user
               
                HttpSession cliente = request.getSession();
                cliente.setAttribute("user",user);
               // contexto = request.getSession().getServletContext();
                 RequestDispatcher paginaError
                        = contexto.getRequestDispatcher("/./faces/templateWelcome.xhtml");

                paginaError.forward(request, response);
//                response.sendRedirect("headerWelcome.xhtml");
                
//                hitCount=0;
//                System.out.println(hitCount);
//                RequestDispatcher anhadirServlet =
//                    contexto.getNamedDispatcher("Matriz");
//                 anhadirServlet.forward(request, response);
           } else{
                HttpSession cliente = request.getSession();
                cliente.setAttribute("user",user);
        
               response.sendRedirect("CheckUser");
//                    RequestDispatcher paginaError
//                        = contexto.getRequestDispatcher("/CheckUser");
//                paginaError.forward(request, response);

           }
       } catch (SQLException ex) {
             System.out.println(ex);
          
       }
       processRequest(request, response);
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
