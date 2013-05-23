package transconnect.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Kiarie
 */
public class DBUtility {

    private String username="francis";
    private String password="pwd";
    private String dburl;
    private Connection con;
    private String path;
    
    public DBUtility() throws SQLException {
        path=System.getProperty("user.home");
        path=path.replaceAll("\\\\", "/");
        path=path.concat("/TransConnect/Database");
        
        dburl="jdbc:derby:"+path+";create=true";
        con= DriverManager.getConnection(dburl, username, password);
    }
    public Connection getConnection(){
        return con;
    }
}
