package transconnect.system;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FillTransition;
import javafx.animation.FillTransitionBuilder;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class LoginController implements Initializable {
    
    @FXML private Label lblTitle;
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private Rectangle rect;
    @FXML private AnchorPane root;
    
    private Connection con;
    int count=1;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            con= new DBUtility().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Color c= Color.web("#4CCADF");
        Color c1=Color.web("#E9FF55");
        FillTransition fillTransition= FillTransitionBuilder.create()
                .fromValue(c)
                .toValue(c1)
                .autoReverse(true)
                .duration(Duration.seconds(4))
                .cycleCount(Timeline.INDEFINITE)
                .shape(rect)
                .build();
        fillTransition.play();
    }    
    @FXML private void login(ActionEvent event){
        if(count>3){
            Dialog.showMessageDialog(root.getScene().getWindow(), "Sorry you have exceed Number of trials\nSystem will exit now", "Number of Trials Exceed",DialogIcon.INFORMATION);
            Platform.exit();
        }
        if(validateInput()){
            String sql="select FirstName,LastName,Note,Type,UserID from users where username=? and password=?";
            try (PreparedStatement prstmt=con.prepareStatement(sql)){
                prstmt.setString(1, txtUser.getText());
                prstmt.setString(2, txtPass.getText());
                ResultSet rs= prstmt.executeQuery();
                if(rs.next()){
                    UserProfile.setName(rs.getString("FirstName")+" "+rs.getString("LastName"));
                    UserProfile.setNote(rs.getString("Note"));
                    UserProfile.setAccountType(rs.getString("Type"));
                    UserProfile.setUserID(rs.getInt("UserID"));
                    
                    Parent parent= FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                    Scene scene= new Scene(parent);
                    Stage stage= new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Main Window");
                    stage.getIcons().add(new Image(LoginController.class.getResourceAsStream("/resources/appicon.png")));
                    Dimension screenSize= java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                    stage.setWidth(screenSize.getWidth());
                    stage.setHeight(screenSize.getHeight());
                    Dialog.showMessageDialog(root.getScene().getWindow(), "Welcome "+rs.getString("FirstName")+" "+rs.getString("LastName"), "Welcome",DialogIcon.INFORMATION);
                    root.getScene().getWindow().hide();
                    stage.show();
                }else{
                    Dialog.showMessageDialog(root.getScene().getWindow(), "Wrong Username or Password", "Wrong Details",DialogIcon.WARNING);
                    txtUser.setText("");
                    txtPass.setText("");
                    lblTitle.requestFocus();
                    count++;
                }
                rs.close(); 
            } catch (SQLException | IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @FXML private void cancel(ActionEvent event){
        Platform.exit();
    }
    private boolean validateInput(){
        Window owner=root.getScene().getWindow();
        if(txtUser.getText().isEmpty()){
            Dialog.showMessageDialog(owner, "Missing Details", "Missing Username",DialogIcon.WARNING);
            txtUser.requestFocus();
            return false;
        }
        if(txtPass.getText().isEmpty()){
            Dialog.showMessageDialog(owner, "Missing Details", "Missing Password",DialogIcon.WARNING);
            txtPass.requestFocus();
            return false;
        }
        if(txtUser.getText().length()>20){
            Dialog.showMessageDialog(owner, "Invalid Input", "Username can not exceed 20 Characters",DialogIcon.WARNING);
            txtUser.requestFocus();
            return false;
        }
        if(txtPass.getText().length()>20){
            Dialog.showMessageDialog(owner, "Invalid Input", "Username can not exceed 20 Characters",DialogIcon.WARNING);
            txtPass.requestFocus();
            return false;
        }
        return true;
    }
}
