package datos;

import java.sql.*;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author Boros
 */
public class Conexion {
    private static final String JDBC_URL= "jdbc:mysql://localhost:3306/control_clientes?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "admin";
    
    private static BasicDataSource dataSourse;
    
    public static DataSource getDataSourse(){
        if(dataSourse==null){
            dataSourse = new BasicDataSource();
            dataSourse.setUrl(JDBC_URL);
            dataSourse.setUsername(JDBC_USER);
            dataSourse.setPassword(JDBC_PASSWORD);
            dataSourse.setInitialSize(50);
        }
        
        
        return dataSourse;
    }
    
    public static Connection getConnection() throws SQLException{
        return getDataSourse().getConnection();
    }
    
    public static void close(ResultSet rs){
        try {
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    public static void close(PreparedStatement stmt){
        try {
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    public static void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
