package Matrizsudokuversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author janto
 */
public class Database {

    DataSource datasource;

    //Abre conexion con la base de datos
    public void inicio() {
        try {
            InitialContext initialContext = new InitialContext();

            datasource = (DataSource) initialContext.lookup("jdbc/sudoku2");
        } catch (NamingException ex) {
            System.out.println(ex);
        }

    }

    //Guarda el estado intermedio de un usuario para un numero de sudoku en la tabla intermedia
    public void guardar(String user, int[][] intermedia, int numSudoku) {
        inicio();

        int i = 0;
        int j = 0;
        int fila;
        int col;
        int valor;
        borrar(user, numSudoku);
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
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                j++;

            }
            i++;

        }

    }

    //Borra un estado intermedio para un usuario y sudoku de la tabla intermedia
    public void borrar(String user, int numSudoku) {
        inicio();

        String query = null;
        query = "DELETE FROM intermedia WHERE user = '"
                + user + "' AND NumSudoku = " + numSudoku;

        Statement statement = null;
        Connection connection = null;
        try {
            connection = datasource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    //Lee un estadio intermedio de Sudoku de la tabla intermedia
    public int[][] plantillaIntermedia(String user, int numSudoku) {
        inicio();
        int[][] i = new int[9][9];
        Statement statement = null;
        Connection connection = null;
        try {
            String query = null;
            query = "SELECT * FROM intermedia WHERE user = '"
                    + user + "' AND NumSudoku = " + numSudoku;
            System.out.println(query);
            ResultSet resulSet = null;
            connection = datasource.getConnection();
            statement = connection.createStatement();
            resulSet = statement.executeQuery(query);
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
            statement.close();
            connection.close();

        } catch (SQLException ex) {
            System.out.println(ex);

        }
        return i;

    }

    //Lee el contenido de un sudoku de la tabla inicial o final, según se indique en la variable plantilla
    public int[][] plantilla(String plantilla, int numSudoku) {
        inicio();
        int[][] i = new int[9][9];
        Statement statement = null;
        Connection connection = null;
        try {
            String query = null;
            query = "SELECT * FROM " + plantilla + " where NumSudoku = " + numSudoku;
            System.out.println(query);
            ResultSet resulSet = null;
            connection = datasource.getConnection();
            statement = connection.createStatement();
            resulSet = statement.executeQuery(query);
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
            statement.close();
            connection.close();

            return i;

        } catch (SQLException ex) {
            System.out.println(ex);

        }
        return i;

    }

}
