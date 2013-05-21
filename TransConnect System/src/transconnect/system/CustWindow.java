package transconnect.system;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Create a Custom Window
 * @author Kiarie
 */
public class CustWindow {
    /**
     * Create and return a JavaFX Stage resources
     * @param title String to be used to in the title bar
     * @param fxmlFile String Name of fxml file
     * @param owner Window owner of this stage
     * @return Stage
     */
    public Stage getStage(String title,String fxmlFile, Window owner, boolean iconified, boolean resizable){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
        } catch (IOException ex) {
            Logger.getLogger(CustWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene= new Scene(root);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(resizable);
        stage.setIconified(iconified);
        stage.getIcons().add(new Image(CustWindow.class.getResourceAsStream("/resources/appicon.png")));
        if (owner!= null) {
            stage.initOwner(owner);
        }
        return stage;
    }
}