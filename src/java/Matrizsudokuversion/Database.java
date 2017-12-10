/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matrizsudokuversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javax.sql.DataSource;

/**
 *
 * @author janto
 */
public class Database extends HttpServlet {
 
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
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        System.out.println("getServletContext():"+ getServletContext());
//       ServletContext context = getServletConfig().getServletContext();
//        numSudoku = Integer.parseInt(context.getInitParameter("numSudoku"));
//        System.out.println("numSudoku"+numSudoku);
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ServletHash</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ServletHash at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
public boolean existeCuenta(String user, String password) {
       init();
       
       try {
          String query=null;
           query = "SELECT * FROM login WHERE user like '"+user+"' AND password='"+password+"'";
           ResultSet resulSet = null;
           connection = datasource.getConnection();
           statement = connection.createStatement();
           resulSet = statement.executeQuery(query);
           while(resulSet.next()){
           return true;
           }
           return false;
       } catch (SQLException ex) {
           System.out.println("No existe el usuario");
           return false;
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

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Listar Personas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Lista de las personas:</h1>");
            out.println("<ul>");

            String query = null;
            query = "select *" + "from login";
            ResultSet resultSet = null;
            Statement statement = null;
            Connection connection = null;
            try {

                connection = datasource.getConnection();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    out.println("<li>" + " user: "+ resultSet.getString("user")
                            + " password: " + resultSet.getString("password") + "</li>");
                }

            } catch (SQLException ex) {
                gestionarErrorEnConsultaSQL(ex, request, response);
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Database.class.getName()).log(Level.SEVERE,
                                "No se pudo cerrar el Resulset", ex);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
//        processRequest(request, response);
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
        
        
//        String user = request.getParameter("user");
//        String password = request.getParameter("password");
//       
//        String query = null;
//
//        query = "insert into login values('"
//                + user + "'," + password + ")";
//       
//ServletContext contexto = request.getServletContext();
////        String numSudoku = (String)contexto.getAttribute("numSudoku");
////        System.out.println("en le isertar post numsudoku vale"+ numSudoku);
//        Statement statement = null;
//        Connection connection = null;
//        try {
//            connection = datasource.getConnection();
//            statement = connection.createStatement();
//            statement.executeUpdate(query);
//
//            request.setAttribute("nextPage", this.getServletContext().getContextPath() + "/Database");
//            RequestDispatcher paginaAltas
//                    = contexto.getRequestDispatcher("/Ejemplo10/amigoInsertado.jsp");
//            paginaAltas.forward(request, response);
//        } catch (SQLException ex) {
//            gestionarErrorEnConsultaSQL(ex, request, response);
//        } finally {
//
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException ex) {
//                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException ex) {
//                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }

    }

    private void gestionarErrorEnConsultaSQL(SQLException ex, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        ServletContext contexto = request.getServletContext();
        Logger
                .getLogger(Database.class
                        .getName()).log(Level.SEVERE, "No se pudo ejecutar la consulta contra la base de datos", ex);
        request.setAttribute("nextPage", this.getServletContext().getContextPath() + "/Ejemplo10/crearPersona.html");
        request.setAttribute("error", ex);
        request.setAttribute("errorMessage", ex.getMessage());
        Logger
                .getLogger(Database.class
                        .getName()).log(Level.INFO, "Set " + request.getAttribute("errorMessage"));

        RequestDispatcher paginaError = contexto.getRequestDispatcher("/Ejemplo10/errorSQL.jsp");
        paginaError.forward(request, response);
    }
    public void guardar(String user,  int[][] intermedia){
     init();
        
        
        int i=0;
        int j=0;
        int fila;
        int col;
        int valor;
         int numSudoku=1;
        borrar(user);
        while(i<=8){
                    j=0;
                    while(j<=8){
                       
                        fila = i+1;
                        col=j+1;
                        valor = intermedia[i][j];
                     
        String query = null;
         query = "insert into intermedia values('"
                + user + "'," + numSudoku + "," + fila +  "," + col + ","+ valor +");";
         
       Statement statement = null;
       Connection connection = null;
        try {
            connection = datasource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
                      connection.close();
        }catch(SQLException ex) { 
            System.out.println(ex);
                } 
        j++;
        
        }
                    i++;
                    
                }
   
       
                
   }
    public void borrar(String user){
        init();
        
         String query = null;
         query = "DELETE FROM intermedia WHERE user = '"
                + user + "'";
               
       Statement statement = null;
       Connection connection = null;
        try {
            connection = datasource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
                      connection.close();
        }catch(SQLException ex) { 
            System.out.println(ex);
                } 
        
    }
   public int[][] plantillaIntermedia(String user){
      init();
    int[][] i = new int[9][9];
       try {
          String query=null;
           query = "SELECT * FROM intermedia WHERE user = '"
                + user + "'";
           ResultSet resulSet = null;
           connection = datasource.getConnection();
           statement = connection.createStatement();
           resulSet = statement.executeQuery(query);
           
           //necesitamos un puntero que es x, hace un bucle y recorre por fila
           //x++ cambias de columna
           int x = 0;
           int y;
           
           while (x <= 8) {
               y = 0;
               while (y <= 8) {
                   resulSet.next();
                   i[x][y] = resulSet.getInt("Valor");
                   y++;
                   
               }
               x++;

           }
           connection.close();
           //destroy();
          
           //return false;
       } catch (SQLException ex) {
           System.out.println("No existe el usuario");
           //return false;
       }
       return i;
       
}
    
    //cuando el usuario pide una plantilla de sudoku le redireccionamos a ella
public int[][] plantilla(String plantilla){
    init();
    int[][] i = new int[9][9];
       try {
          String query=null;
           query = "SELECT * FROM "+plantilla;
           ResultSet resulSet = null;
           connection = datasource.getConnection();
           statement = connection.createStatement();
           resulSet = statement.executeQuery(query);
           
           //necesitamos un puntero que es x, hace un bucle y recorre por fila
           //x++ cambias de columna
           int x = 0;
           int y;
           
           while (x <= 8) {
               y = 0;
               while (y <= 8) {
                   resulSet.next();
                   i[x][y] = resulSet.getInt("Valor");
                   
                   y++;
                   
               }
               x++;

           }
           connection.close();
           //destroy();
           return i;
           //return false;
       } catch (SQLException ex) {
           System.out.println("No existe el usuario");
           //return false;
       }
       return i;
       
}
   @Override
   public void destroy() {
        try {
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
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
