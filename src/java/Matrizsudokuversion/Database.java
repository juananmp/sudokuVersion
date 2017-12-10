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

    }

    public void guardar(String user, int[][] intermedia) {
        init();

        int i = 0;
        int j = 0;
        int fila;
        int col;
        int valor;
        int numSudoku = 1;
        borrar(user);
        while (i <= 8) {
            j = 0;
            while (j <= 8) {

                fila = i + 1;
                col = j + 1;
                valor = intermedia[i][j];

                String query = null;
                query = "insert into intermedia values('"
                        + user + "'," + numSudoku + "," + fila + "," + col + "," + valor + ");";

                Statement statement = null;
                Connection connection = null;
                try {
                    connection = datasource.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                j++;

            }
            i++;

        }

    }

    public void borrar(String user) {
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
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public int[][] plantillaIntermedia(String user) {
        init();
        int[][] i = new int[9][9];
        try {
            String query = null;
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

        } catch (SQLException ex) {
            System.out.println("No existe el usuario");

        }
        return i;

    }

    //cuando el usuario pide una plantilla de sudoku le redireccionamos a ella
    public int[][] plantilla(String plantilla) {
        init();
        int[][] i = new int[9][9];
        try {
            String query = null;
            query = "SELECT * FROM " + plantilla;
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

            return i;

        } catch (SQLException ex) {
            System.out.println("No existe el usuario");

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
