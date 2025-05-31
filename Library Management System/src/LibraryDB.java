import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryDB {
    private static final String URL = "jdbc:mysql://localhost:3306/Library?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Ghjalqtv@##d01";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
