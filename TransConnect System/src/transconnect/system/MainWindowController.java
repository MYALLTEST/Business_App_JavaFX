/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transconnect.system;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class MainWindowController implements Initializable {
    
    @FXML private BorderPane root;
    @FXML private Label lblUser;
    @FXML private ImageView iv;
    @FXML private ImageView close;
    @FXML private ImageView messageIcon;
    @FXML private Group notification;
    @FXML private Label lblTitle;
    @FXML private Label lblMsg;
    @FXML private ToolBar taskBar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Image image = null;
        lblUser.setText("User: "+UserProfile.getName()+" : "+UserProfile.getAccountType());
        String accountType = UserProfile.getAccountType();
        switch (accountType) {
            case "System User":
                image= new Image(MainWindowController.class.getResourceAsStream("/resources/system_user.png"));
                break;   
            case "System Administrator":
                image= new Image(MainWindowController.class.getResourceAsStream("/resources/system_administrator.png"));
                break;
        }
        iv.setImage(image);
        grantAccess(accountType);
        if(UserProfile.getNote().equals("using default")){
            lblTitle.setText("Security Alert");
            lblMsg.setText("Please note you are still using the Default Username or Passsword. "
                    + "Please change your Username and Password to improve security.\n"
                    + "Go to: Security>My Profile");
            messageIcon.setImage(new Image(MainWindowController.class.getResourceAsStream("/resources/warning.png")));
            showNotification();
            
        }
    }
    @FXML private void logout(ActionEvent event){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.getIcons().add(new Image(LoginController.class.getResourceAsStream("/resources/appicon.png")));
            int option=Dialog.showConfirmDialog(root.getScene().getWindow(), "Are you sure you want to Log out?", "Log out?", DialogIcon.QUESTION);
            if(option == DialogOption.OPTION_OK.value){
                root.getScene().getWindow().hide();
                stage.show(); 
            }
        } catch (IOException iOException) {
        }
    }
    @FXML private void showUsersProfile(ActionEvent event){
        Stage stage= new CustWindow().getStage("Users Profile", "usersProfile.fxml", root.getScene().getWindow(),false,false);
        stage.show();
    }
    @FXML private void showMyProfile(ActionEvent event){
        Stage stage= new CustWindow().getStage("My Profile", "myProfile.fxml", root.getScene().getWindow(),false,false);
        stage.show();
    }
    @FXML private void showVehicles(ActionEvent event){
        Stage stage= new CustWindow().getStage("Vehicles", "vehicle.fxml", root.getScene().getWindow(),false,false);
        stage.show();
    }
    @FXML private void showDrivers(ActionEvent event){
        Stage stage= new CustWindow().getStage("Drivers", "driver.fxml", root.getScene().getWindow(),false,false);
        stage.show();
    }
    @FXML private void showNewBooking(ActionEvent event){
        Stage stage= new CustWindow().getStage("New Booking", "newBooking.fxml", root.getScene().getWindow(),false,false);
        stage.show();
    }
    @FXML private void showCalendar(ActionEvent event){
        Stage stage= new CustWindow().getStage("Calendar", "calendarPrev.fxml", root.getScene().getWindow(),false,false);
        stage.show();
    }
    /**
     * Grant Action access depending on account type
     * @param accountType String
     */
    private void grantAccess(String accountType){
        switch (accountType) {
            case "System User":
               // grant access privileges to system users 
                break;   
            case "System Administrator":
                //grant access privileges to system administrators
                break;
        }
    }
    /**
     * Show Notification
     */
    private void showNotification(){
        FadeTransition ft= FadeTransitionBuilder.create()
                .node(notification)
                .fromValue(0)
                .toValue(1)
                .duration(Duration.seconds(4))
                .build();
        ft.play();
    }
    /**
     * Hide Notification
     * @param event MouseEvent
     */
   @FXML private void hideNotification(MouseEvent event){
       if (event.getButton()== MouseButton.PRIMARY) {
           FadeTransition ft = FadeTransitionBuilder.create()
                   .node(notification)
                   .fromValue(1)
                   .toValue(0)
                   .duration(Duration.seconds(1.5))
                   .build();
           ft.play();
       }
   }
   /**
    * Change mouse icon in notification close icon
    * @param event MouseEvent
    */
   @FXML private void closeMouseEnter(MouseEvent event){
        close.setCursor(Cursor.HAND);   
   }
   /**
    * Change mouse icon in notification close icon
    * @param event MouseEvent
    */
   @FXML private void closeMouseExit(MouseEvent event){
        close.setCursor(Cursor.DEFAULT); 
   }
   /**
    * Show My Profile from mouse click from user name from 
    * task bar
    * @param event MouseEvent 
    */
   @FXML private void userShowProfile(MouseEvent event){
       if (event.getButton()== MouseButton.PRIMARY) {
           Stage stage= new CustWindow().getStage("My Profile", "myProfile.fxml", root.getScene().getWindow(),false,false);
            stage.show();
       }
   }
   /**
    * Change mouse icon in notification close icon
    * @param event MouseEvent
    */
   @FXML private void taskBarMouseEnter(MouseEvent event){
        taskBar.setCursor(Cursor.HAND);   
   }
   /**
    * Change mouse icon in notification close icon
    * @param event MouseEvent
    */
   @FXML private void taskBarMouseExit(MouseEvent event){
        taskBar.setCursor(Cursor.DEFAULT); 
   }
}
