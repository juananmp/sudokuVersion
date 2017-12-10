/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
//int primo;
//String password;
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
        
        PrintWriter out = response.getWriter();
       

        System.out.println("getServletContext():"+ getServletContext());
       ServletContext context = getServletConfig().getServletContext();
        int primo = Integer.parseInt(context.getInitParameter("primo"));
        System.out.println("primo"+primo);
        
        HttpSession cliente = request.getSession();
       String user= (String)cliente.getAttribute("user");
        String password = (String)cliente.getAttribute("password");
        
       
         ServletContext contexto = request.getServletContext();
         int result=17;
         result= primo*result+password.hashCode();
     
         System.out.println("valores hashcode user password"+user+password+result);
      
//       int i = hashCode();
        String query = null;
//      
//        System.out.println(user + password+ i+"-----------------------------------------<");
        query = "INSERT INTO login VALUES ('"+ user + "', '"+ result +"')";
         Statement statement = null;
        Connection connection = null;
        try {
            connection = datasource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);

             request.setAttribute("nextPage", this.getServletContext().getContextPath() + "/ServletHash");
             
               RequestDispatcher altaUser
                    = contexto.getRequestDispatcher("/index.html");
            altaUser.forward(request, response);
           
//             RequestDispatcher altaUser
//                    = contexto.getRequestDispatcher("/index.html");
//            altaUser.forward(request, response);
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } 
       
        
    }
//@Override
//public int hashCode(){
//    int result=17;
//    result=primo*result+password.hashCode();
//    return result;
//}
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
