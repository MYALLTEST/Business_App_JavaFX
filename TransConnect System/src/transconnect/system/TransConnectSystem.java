package transconnect.system;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Kiarie
 */
public class TransConnectSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        if(tablesExists()){
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setTitle("Login");
            stage.initStyle(StageStyle.UNDECORATED);
        }else{
           root = FXMLLoader.load(getClass().getResource("welcomeScreen.fxml"));
           stage.setTitle("Welcome Screen");
        }
       
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(TransConnectSystem.class.getResourceAsStream("/resources/appicon.png")));
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Checks whether database tables exists
     * @return True if tables exists else False if they do not exists
     * @throws SQLException 
     */
    private boolean tablesExists() throws SQLException{
        Connection conn = new DBUtility().getConnection();
        boolean status=true;
        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "USERS", null);
            if (!rs.next()) {
              //Table not Exist, let's create it 
               status=false;
            }
            rs.close();
        }finally{
            if(conn!= null){
                conn.close();
            }
        }
        return status;
    }
}