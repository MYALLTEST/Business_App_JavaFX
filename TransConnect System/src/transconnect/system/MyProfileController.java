package transconnect.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class MyProfileController implements Initializable {

    @FXML private AnchorPane root;
    @FXML private Label lblName;
    @FXML private Label lblType;
    @FXML private TextField txtUsername;
    @FXML private PasswordField pfCurrentPass;
    @FXML private PasswordField pfNewPass;
    @FXML private PasswordField pfRetypePass;
    
    private Connection con;
    private String pass;
    private String fname;
    private int userID;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con= new DBUtility().getConnection();
            loadData();
        } catch (SQLException ex) {
        }
    }
    @FXML private void update(ActionEvent event){
        String note="not using default";
        if(fname.equals(txtUsername.getText()) || pfNewPass.getText().equals("1234")){
            note="using default";
        }
        if(validateInputFields()){
            String sql="update users set Username=?,Password=?,Note=? where UserID=?";
            try (PreparedStatement prstmt=con.prepareStatement(sql)){
                prstmt.setString(1, txtUsername.getText());
                prstmt.setString(2, pfNewPass.getText());
                prstmt.setString(3, note);
                prstmt.setInt(4, userID);
                prstmt.execute();
                Dialog.showMessageDialog(root.getScene().getWindow(), "Detials Updated", "Update", DialogIcon.INFORMATION);
            } catch (SQLException ex) {
                Logger.getLogger(MyProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void loadData()throws SQLException{
        userID=UserProfile.getUserID();
        lblName.setText(UserProfile.getName());
        lblType.setText(UserProfile.getAccountType());
       
        String sql="select Username,Password,FirstName from users where UserID=?";
        try (PreparedStatement prstmt=con.prepareStatement(sql)){
            prstmt.setInt(1, userID);
            ResultSet rs=prstmt.executeQuery();
            if(rs.next()){
                txtUsername.setText(rs.getString("Username"));
                pass=rs.getString("Password");
                fname=rs.getString("FirstName");
            }
            rs.close();
        }
    }
    private boolean validateInputFields(){
        Window owner=root.getScene().getWindow();
        if(txtUsername.getText().isEmpty()){
                Dialog.showMessageDialog(owner, "Please Username", "Missing details", DialogIcon.WARNING);
                txtUsername.requestFocus();
                return false;
            }
        ObservableList<PasswordField> passFields= FXCollections.observableArrayList();
        passFields.addAll(pfCurrentPass,pfNewPass,pfRetypePass);
        for (PasswordField passwordField : passFields) {
            if(passwordField.getText().isEmpty()){
                Dialog.showMessageDialog(owner, "Please enter required information", "Missing details", DialogIcon.WARNING);
                passwordField.requestFocus();
                return false;
            }
        }
        if(txtUsername.getText().length()>20 || txtUsername.getText().length()<4){
            Dialog.showMessageDialog(owner, "Sorry your Username has to be More than 3 Characters and can not Exceed 20 Charchaters", "Invalid input",DialogIcon.WARNING);
            txtUsername.requestFocus();
            return false;
        }
        for (PasswordField passwordField : passFields) {
            if(passwordField.getText().length()>20 || passwordField.getText().length()<4){
                Dialog.showMessageDialog(owner, "Sorry your Password has to be More than 3 Characters and can not Exceed 20 Charchaters", "Invalid Input",DialogIcon.WARNING);
                passwordField.requestFocus();
                return false;
            }
        }
        if(!pfCurrentPass.getText().equals(pass)){
            Dialog.showMessageDialog(owner, "Sorry the Current Password is incorrect", "Invalid Password", DialogIcon.WARNING);
            pfCurrentPass.requestFocus();
            return false;
        }
        if(!pfNewPass.getText().equals(pfRetypePass.getText())){
            Dialog.showMessageDialog(owner, "Your Retype Password does not match New Password", "No Match", DialogIcon.WARNING);
            pfRetypePass.requestFocus();
            return false;
        }
        return true;
    }
}
