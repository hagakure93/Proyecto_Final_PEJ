package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionJDBC {
    private static final String USER = "root";
    private static final String PWD = ""; // Tienes que poenr la contraseña
    private static final String URL = "jdbc:MySQL://localhost/";

    public static Connection conectar(String nombreBD) throws SQLException {
        return DriverManager.getConnection(URL+nombreBD, USER, PWD);
    }
}
