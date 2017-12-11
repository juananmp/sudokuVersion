/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PruebasSudokuNoAccesoCliente;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author janto
 */
public class ReadSudoku {

    private int numSoduku = 1;
    private int fila;
    private int col;
    private int valor;
    private int i = 1;
    private int j = 1;

    public void readData() {
        try (Scanner input = new Scanner(new File("src/java/Matriz/inicialsudoku1.txt"))) {

            String line;
            line = input.nextLine();

            try (Scanner data = new Scanner(line)) {

                while (i <= 9) {
                    j = 1;
                    while (j <= 9) {

                        fila = i;
                        col = j;
                        valor = data.nextInt();
                        System.out.println(fila + "\t" + col + "\t" + valor);
                        saveData();
                        j++;
                    }
                    i++;
                }

            }

        } catch (IOException e) {

            System.out.println(e);
        }
    }


    private void saveData() {
        try (Connection conn = connection();
                PreparedStatement pstat = conn.prepareStatement("INSERT INTO inicial VALUES (?,?,?,?)")) {

            //siempre se empieza desde 1
            pstat.setInt(1, numSoduku);
            pstat.setInt(2, fila);
            pstat.setInt(3, col);
            pstat.setInt(4, valor);

            pstat.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);

        }

    }

    //ahora creo un método que recupera los datos de plantilla de la BBDD
    public static void displayData() {
        //using a try with resources statement we open a connection to the database by calling
        //the connect method that i created earlier we also create a statement object for executing sql statements
        try (Connection conn = connection();
                Statement stat = conn.createStatement()) {
            //the execute method of the statement object will return a boolean value after executing the query string
            boolean hasResulSet = stat.execute("SELECT * FROM inicial");
            //we will check for a boolean true before saving the query rsults in a result set object 
            if (hasResulSet) {
                //if the condition is true then we create a result set object to store the results  of the query by calling

                //en el result guardamos la query
                ResultSet result = stat.getResultSet();

                //display data
                while (result.next()) {
                    //printf OJO no pritln para formata a los datos de salida ahora comentaremos el ssaveData
                    //primer % en esa posición se va escribir un valor el valor se encuentra entre las comillas la s:string d:caracter entero nº indica los decimales
                    //%n salto de línea
                    //la composción fila-column1-... saltamos la linea (linea anterior) y continuamos con el .next y asi imprimimos todo el sudoku
                    System.out.printf("%4d%4d%4d%4d%n", result.getInt("NumSudoku"), result.getInt("Fila"),
                            result.getInt("Col"), result.getInt("Valor"));

                }

            }

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    // create a connection to the database
    private static Connection connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku2?verifyServerCertificate=false&useSSL=true", "root", "root");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
            return null;
        }
    }

}

class FDemo {//esta clase va a crear un objeto

    public static void main(String[] args) {
        ReadSudoku rsu = new ReadSudoku();

        try {
            rsu.readData();
            rsu.displayData();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
