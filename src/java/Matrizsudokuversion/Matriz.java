/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matrizsudokuversion;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author janto
 */
public class Matriz extends HttpServlet {

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

        //Leer usuario de sesion
        HttpSession cliente = request.getSession();
        String user = (String) cliente.getAttribute("user");

        //Leo de la base de datos el sudoku inicial y el final
        Database db = new Database();

        int[][] matriz;
        int[][] solucion;
        //Lee parametro de inicilizacion con titulo de la pagina y ServletConfig tiene un metodo llamado getinitparameter
        ServletConfig sc = this.getServletConfig();
        String saludo = sc.getInitParameter("saludo");

        //Leer numero de sudoku seleccionado
        String stringNumSudoku = request.getParameter("numSudoku");
        int numSudoku;

        //Si es la primera ejecucion del Servlet, numSudoku es distinto de null y lo subo a la sesion
        if (stringNumSudoku != null) {
            System.out.println("primera ejecucion por numsudoku");
            numSudoku = Integer.parseInt(request.getParameter("numSudoku"));
            cliente.setAttribute("numSudoku", numSudoku);
            matriz = db.plantilla("Inicial", numSudoku);
            System.out.println("leo inicial y final");
            solucion = db.plantilla("Final", numSudoku);
            cliente.setAttribute("matriz", matriz);
            cliente.setAttribute("solucion", solucion);
            //Si no es la primera ejecucion, se lee numSudoku de la Sesion           
        } else {
            System.out.println("No primera ejecucion por numSUDoku");
            numSudoku = (Integer) cliente.getAttribute("numSudoku");

            matriz = (int[][]) cliente.getAttribute("matriz");
            solucion = (int[][]) cliente.getAttribute("solucion");
        }

        //Comprobar dice si se ha pulsado ya el boton comprobar
        String comprobar = request.getParameter("comprobar");
        System.out.println("imprimo comprobar" + comprobar);

        //Si numeroYposicion no es nulo, no es la primera entrada al servlet
        if (cliente.getAttribute("numeroYposicion") != null) {
            System.out.println("no primera ejecucion por numero y posicion");
            for (int m = 0; m < 9; m++) { //Movernos por fila 
                for (int k = 0; k < 9; k++) { //Movernos por columna
                    //Si en una cuadricula el usuario ha escrito un numero, sobreescribimos en numero y posicion
                    if (comprobar(request.getParameter("numero" + m + k))) {
                        int[][] numeroYposicion = (int[][]) cliente.getAttribute("numeroYposicion");
                        numeroYposicion[m][k] = Integer.parseInt(request.getParameter("numero" + m + k));
                        request.setAttribute("numeroYposicion", numeroYposicion);
                    }

                }
            }

            //Si no existe numeroYposcion lo inicilazimos
        } else {
            System.out.println("Primera ejecucion por numeroYposicion");
            int[][] numeroYposicion = new int[9][9];
            //Se leen datos del estado intermedio del sudoku y se suben a numeroYposicion para pintarlo despues
            numeroYposicion = db.plantillaIntermedia(user, numSudoku);
            cliente.setAttribute("numeroYposicion", numeroYposicion);

        }

        //Pintar Sudoku con datos de numeroYposicion
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Matriz</title>");
            out.println("<link rel=\"stylesheet\" href=\"./resources/css/w3.css\">");
            out.println("<link rel=\"stylesheet\" href=\"./resources/css/cssTabla.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<h4>" + saludo + "</h4>");
            //Se vuelve a ejecutat el Servlet Matriz (action)

            out.println(" <form method=\"post\" action=\"/sudokuVersion/Matriz\"><table id=\"grid\">");

            int[][] contenido = (int[][]) cliente.getAttribute("numeroYposicion");
            System.out.println("Asignado contenido");
            //Recorremos las cuadriculas pintando
            for (int i = 0; i < matriz.length; i++) {//matriz es matriz plantilla
                //tr fila
                out.println("  <tr>");
                for (int j = 0; j < matriz.length; j++) {

                    if (matriz[i][j] != 0) { //Se bloquean las cifras predefinidas
                        out.println("<td  class=\"cell\"> <input type=\"text\" value=\"" + matriz[i][j] + "\" disabled></td>");

                        // Cifras en las que si se puede escribir
                    } else {//si es 0 es que ahi deemos escribir
                        // Si la celda no es vacia verificamos mediante el metodo comprobamos que sea un numero entre 1-9 y si se
                        //ha apretado el boton comprobar ponemos el fondo a verde 
                        //o rojo mediante el metodo de resolver
                        if (contenido != null && contenido[i][j] != 0) {
                            if (comprobar(Integer.toString(contenido[i][j])) && contenido[i][j] > 0 && contenido[i][j] < 10) {
                                if (comprobar != null) {//Si nos han pedido comprobar pinto
                                    out.println("<td  class=\"cell\"><input style=\"background:" + resolver(i, j, contenido[i][j], solucion)
                                            + ";\" type=\"text\" name=\"numero" + i + "" + j + "\" value=\"" + contenido[i][j] + "\"></td>");
                                } else {//Si no nos piden comprobar NO pinto
                                    out.println("<td  class=\"cell\"><input type=\"text\" name=\"numero" + i + "" + j + "\" value=\"" + contenido[i][j] + "\"></td>");
                                }
                            } else {
                                out.println("<td  class=\"cell\"><input type=\"text\" name=\"numero" + i + "" + j + "\"></td>");
                            }
                        } else { // si no imprime la caja de texto con el 0
                            out.println("<td  class=\"cell\"><input type=\"text\" name=\"numero" + i + "" + j + "\"></td>");
                        }
                    }
                }
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<div class=\"almacenarclass\">");
            out.println("<h6>Panel De Control</h6>");
            out.println("<button>Almacenar</button>");
            out.println("</form>");
            int[][] numeroYposicion = (int[][]) cliente.getAttribute("numeroYposicion");
            System.out.println("fin form almacenar, guardando sudoku");
            //Se guarda estado del sudoku en tabla intermedia
            db.guardar(user, numeroYposicion, numSudoku);

            out.println("<form method=\"post\" action=\"/sudokuVersion/Matriz\" name=\"datos\"><input type=\"hidden\" name=\"comprobar\" value=\"algo\"><button>Comprobar</button>");
            out.println("</form>");
            System.out.println("fin form comprobar");
            out.println("</div>");

            System.out.println("verificar acabado");
            //Verifica si el sudoku esta correctamente acabado
            boolean acabado = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (matriz[i][j] != solucion[i][j] && numeroYposicion[i][j] != solucion[i][j]) {
                        acabado = false;
                    }

                }

            }
            if (acabado) {
                System.out.println("Si esta acabado");
                response.sendRedirect("/sudokuVersion/faces/sudokuAcabado.xhtml");
            }

            //Estadistica de usuarios concurrentes y usuarios conectados
            ServletContext ctx = getServletContext();
            int totalUsers = (Integer) ctx.getAttribute("totalusers");
            int currentUsers = (Integer) ctx.getAttribute("currentusers");
            out.println("<br>");
            out.print("<h4>" + "total users=" + totalUsers + "</h4>");
            out.print("<br>");
            out.println("<h4>" + "current users= " + currentUsers + "</h4>");
            out.print("<br>");
            out.print("<br>");
            out.println("<h1>Servlet Matriz at " + request.getContextPath() + "</h1>");
            out.println(user);
            out.println("</body>");
            out.println("</html>");
        }
    }

    //Si la solucion de la casilla es correcta se pone fondo verde; sino, se pone fondo rojo
    public String resolver(int x, int y, int resp, int[][] solucion) {
        if (solucion[x][y] == resp) {
            return "green";
        } else {
            return "red";
        }
    }

    //Verifica si la casilla es un numero
    public boolean comprobar(String celda) {
        System.out.println("compruebo el valor de celda" + celda);
        try {
            Integer.parseInt(celda);
            return true;
        } catch (NumberFormatException nfe) {
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
