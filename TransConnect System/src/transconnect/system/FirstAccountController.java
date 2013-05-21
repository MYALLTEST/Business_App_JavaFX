/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transconnect.system;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class FirstAccountController implements Initializable {
    
    @FXML private TextField txtfn;
    @FXML private TextField txtln;
    @FXML private TextField txtsurname;
    @FXML private TextField txtDOB;
    @FXML private TextField txtResidence;
    @FXML private TextField txtMobile;
    @FXML private TextField txtID;
    @FXML private TextField username;
    @FXML private PasswordField pass;
    @FXML private Label lblUsername;
    @FXML private Label lblPass;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    @FXML private Button btnContinue;
    @FXML private Rectangle rect;
    @FXML private Line line; 
    @FXML private AnchorPane root;
    
    private Connection con;
    private Date date;
    private ObservableList<TextField> inputFileds= FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            con= new DBUtility().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FirstAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtfn.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                username.setText(newValue);
            }
        });
        username.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(txtfn.getText())){
                    lblUsername.setText("Using default Username");
                }else{
                    lblUsername.setText("");
                }
            }
        });
        pass.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("1234")){
                    lblPass.setText("Using default Password");
                }else{
                    lblPass.setText("");
                }
            }
        });
        root.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setWidth((double)newValue);
                line.setEndX((double)newValue);
            }
        });
    }
    @FXML private void save(ActionEvent event){
        String note="not using default";
        if(username.getText().equals(txtfn.getText()) || pass.getText().equals("1234")){
            note="using default";
        }
        
        if (validateInput()) {
            java.sql.Date dob= new java.sql.Date(date.getTime());
            String sql = "insert into users(FirstName,LastName,Surname,DOB,Residence,Mobile,Type,NationalID,Username,Password,Note,Created_By) values(?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement prstmt = con.prepareStatement(sql)){
                prstmt.setString(1, txtfn.getText());
                prstmt.setString(2, txtln.getText());
                prstmt.setString(3, txtsurname.getText());
                prstmt.setDate(4, dob);
                prstmt.setString(5, txtResidence.getText());
                prstmt.setString(6, txtMobile.getText());
                prstmt.setString(7, "System Administrator");
                prstmt.setString(8, txtID.getText());
                prstmt.setString(9, username.getText());
                prstmt.setString(10, pass.getText());
                prstmt.setString(11, note);
                prstmt.setString(12, "Self");
                prstmt.execute();
                Dialog.showMessageDialog(root.getScene().getWindow(), "New System Administrator Account Created", "New User Added", DialogIcon.INFORMATION);
                btnSave.setDisable(true);
                btnContinue.setDisable(false);
                UserProfile.setName(txtfn.getText()+" "+txtln.getText());
                UserProfile.setNote(note);
                UserProfile.setAccountType("System Administrator");
                UserProfile.setUserID(1);
            } catch (SQLException ex) {
                Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @FXML private void clear(ActionEvent event){
        inputFileds.clear();
        inputFileds.addAll(txtfn,txtln,txtsurname,txtDOB,txtResidence,txtMobile,txtID);
        for (TextField textField : inputFileds) {
            textField.setText("");
        }
        pass.setText("1234");
        btnSave.setDisable(false);
        txtfn.requestFocus();
    }
    @FXML private void openMainWindow(ActionEvent event){
        try {
                Parent parent= FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                Scene scene= new Scene(parent);
                Stage stage= new Stage();
                stage.setScene(scene);
                stage.setTitle("Main Window");
                Dimension screenSize= java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                stage.setWidth(screenSize.getWidth());
                stage.setHeight(screenSize.getHeight());
                root.getScene().getWindow().hide();
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    /**
     * Validate all input fields for the correct data
     * @return True if all input is valid else False
     */
    private boolean validateInput(){
        Window owner=root.getScene().getWindow();
        inputFileds.addAll(txtfn,txtln,txtsurname,txtDOB,txtResidence,txtMobile,txtID,username);
        for (TextField textField : inputFileds) {
            if(textField.getText().isEmpty()){
                //set css stroke
                Dialog.showMessageDialog(owner,"Missing Details","Invalid Input",DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        inputFileds.removeAll(txtDOB,txtMobile,username);
        for (TextField textField : inputFileds) {
            if(textField.getText().length()>30){
                //set css stroke
                Dialog.showMessageDialog(owner,"Sorry the text you entered can not be greater that 30 characters","Text too Long",DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        if(username.getText().length()>20 || username.getText().length()<4){
            Dialog.showMessageDialog(owner, "Sorry your Username has to be More than 3 Characters and can not Exceed 20 Charchaters", "Invalid input",DialogIcon.WARNING);
            username.requestFocus();
            return false;
        }
        if(pass.getText().isEmpty()){
            Dialog.showMessageDialog(owner,"Missing Details","Invalid Input",DialogIcon.WARNING);
            pass.requestFocus();
            return false;
        }
        if(pass.getText().length()>20 || pass.getText().length()<4){
            Dialog.showMessageDialog(owner, "Sorry your Password has to be More than 3 Characters and can not Exceed 20 Charchaters", "Invalid Input",DialogIcon.WARNING);
            pass.requestFocus();
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        try {
            date = dateFormat.parse(txtDOB.getText());
            Calendar cal=Calendar.getInstance();
            int year=Integer.parseInt(yearFormat.format(date));
            if (year <= 1900 || year>cal.get(Calendar.YEAR)) {
                Dialog.showMessageDialog(owner,"Invalid Date of Birth", "Invalid Input",DialogIcon.WARNING);
                txtDOB.requestFocus();
                return false;
            }
            String initialEntry=txtDOB.getText();
            String parsedValue=dateFormat.format(date);
            if(!initialEntry.equals(parsedValue)){
               Dialog.showMessageDialog(owner, "Note your Date of Birth has been corrected", "Date Corrected",DialogIcon.INFORMATION);
            }
            txtDOB.setText(dateFormat.format(date));
        } catch (ParseException  ex) {
            Dialog.showMessageDialog(owner,"Invalid Date of Birth", "Invalid Input",DialogIcon.WARNING);
            txtDOB.requestFocus();
            return false;
        }
        try {
            int mobile=Integer.parseInt(txtMobile.getText());
        } catch (NumberFormatException e) {
            Dialog.showMessageDialog(owner, "Invalid Mobile Number", "Invalid data",DialogIcon.WARNING);
            txtMobile.requestFocus();
            return false;
        }
        if(txtMobile.getText().length() != 10){
            Dialog.showMessageDialog(owner, "Sorry your Mobile Number is invalid", "Invalid input",DialogIcon.WARNING);
            txtMobile.requestFocus();
            return false;
        }
        String sql="select Username,Password from users where Username=?";
        try(PreparedStatement prstmt=con.prepareStatement(sql)) {
            prstmt.setString(1, username.getText());
            ResultSet rs=prstmt.executeQuery();
            if(rs.next()){
                Dialog.showMessageDialog(owner, "Sorry Username already exists\n Please change to different Username", "Username Exists",DialogIcon.WARNING);
                username.requestFocus();
                return false;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return true;
    }
}
