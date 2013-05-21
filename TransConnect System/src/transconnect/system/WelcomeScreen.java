package transconnect.system;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Kiarie
 */
public class WelcomeScreen implements Initializable {
    
    @FXML private Rectangle rect;
    @FXML private AnchorPane root;
    @FXML private Label lblMsg;
    @FXML private Button btnNext;
    @FXML private Line line;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert btnNext != null : "fx:id=\"btnNext\" was not injected: check your FXML file 'Sample.fxml'.";
        assert lblMsg != null : "fx:id=\"lblMsg\" was not injected: check your FXML file 'Sample.fxml'.";
        assert rect != null : "fx:id=\"rect\" was not injected: check your FXML file 'Sample.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Sample.fxml'.";
        
        root.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setWidth((double)newValue);
                line.setEndX((double)newValue);
            }
        });
        createTables();
    }
    @FXML private void next(ActionEvent event){
        try {
            Parent parent= FXMLLoader.load(getClass().getResource("firstAccount.fxml"));
            Scene scene= new Scene(parent);
            Stage stage= (Stage) root.getScene().getWindow();
            stage.setResizable(false);
            stage.setIconified(false);
            stage.setScene(scene);
            stage.setTitle("Users Profile");
        } catch (IOException ex) {
            Logger.getLogger(WelcomeScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void createTables(){
        try {
            DBTables tables= new DBTables();
            btnNext.setDisable(false);
        } catch (SQLException ex) {
            lblMsg.setText(ex.getMessage());
        }
    }
}
