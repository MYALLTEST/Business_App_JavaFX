package transconnect.system;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class UsersProfileController implements Initializable {
    
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
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Rectangle rect;
    @FXML private Line line; 
    @FXML private AnchorPane root;
    @FXML private RadioButton rdSysUser;
    @FXML private RadioButton rdSysAdmin;
    @FXML private TableView<UserInfo> tv;
    @FXML private TableColumn<UserInfo,Integer> colUserID; 
    @FXML private TableColumn<UserInfo,String> colName;
    @FXML private TableColumn<UserInfo,String> colUserType;
    @FXML private TableColumn<UserInfo,String> colUsername;
    @FXML private Tab addUserTab;
    @FXML private TabPane tabPane;
    
    private Connection con;
    private Date date;
    private ObservableList<TextField> inputFileds= FXCollections.observableArrayList();
    private ObservableList<UserInfo> data=FXCollections.observableArrayList();
    private int userID;
    private Text tableMessage= new Text("No records Found");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            con= new DBUtility().getConnection();
        } catch (SQLException ex) {
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
                    if (newValue!=null && newValue.equals(txtfn.getText())) {
                        lblUsername.setText("Using default Username");
                    } else {
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
        
        //configure table columns
        colUserID.setCellValueFactory(new PropertyValueFactory<UserInfo,Integer>("userID"));
        colName.setCellValueFactory(new PropertyValueFactory<UserInfo,String>("name"));
        colUserType.setCellValueFactory(new PropertyValueFactory<UserInfo,String>("userType"));
        colUsername.setCellValueFactory(new PropertyValueFactory<UserInfo,String>("username"));
        
        tv.setItems(data);
        tv.setPlaceholder(tableMessage);
        
        loadData();
    }
    /**
     * Save user details
     * @param event ActionEvent
     */
    @FXML private void add(ActionEvent event){
        String note="not using default";
        String sysAdmin;
        if(username.getText().equals(txtfn.getText()) || pass.getText().equals("1234")){
            note="using default";
        }
        if(UserProfile.getName() != null){
            sysAdmin=UserProfile.getName();
        }else{
            sysAdmin=txtfn.getText();
        }
        String type="System User";
        if(rdSysAdmin.isSelected()){
            type="System Administrator";
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
                prstmt.setString(7, type);
                prstmt.setString(8, txtID.getText());
                prstmt.setString(9, username.getText());
                prstmt.setString(10, pass.getText());
                prstmt.setString(11, note);
                prstmt.setString(12, sysAdmin);
                prstmt.execute();
                Dialog.showDialog(root.getScene().getWindow(), "New "+type+" Account Created", "New User Added", DialogIcon.INFORMATION);
                btnAdd.setDisable(true);
            } catch (SQLException ex) {
                Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadData();
        }
    }
    /**
     * Clear fields
     * @param event ActionEvent 
     */
    @FXML private void clear(ActionEvent event){
        inputFileds.clear();
        inputFileds.addAll(txtfn,txtln,txtsurname,txtDOB,txtResidence,txtMobile,txtID);
        for (TextField textField : inputFileds) {
            textField.setText("");
        }
        pass.setText("1234");
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        txtfn.requestFocus();
    }
    /**
     * Update user details 
     * @param event ActionEvent
     */
    @FXML private void update(ActionEvent event){
        String note="not using default";
        if(username.getText().equals(txtfn.getText()) || pass.getText().equals("1234")){
            note="using default";
        }
        String type="System User";
        if(rdSysAdmin.isSelected()){
            type="System Administrator";
        }
        if (validateInput()) {
            java.sql.Date dob= new java.sql.Date(date.getTime());
            String sql="update users set FirstName=?,LastName=?,Surname=?,DOB=?,Residence=?,Mobile=?,Type=?,NationalID=?,Username=?,Password=?,Note=? where UserID=?";
            try(PreparedStatement prstmt=con.prepareStatement(sql)) {
                prstmt.setString(1, txtfn.getText());
                prstmt.setString(2, txtln.getText());
                prstmt.setString(3, txtsurname.getText());
                prstmt.setDate(4, dob);
                prstmt.setString(5, txtResidence.getText());
                prstmt.setString(6, txtMobile.getText());
                prstmt.setString(7, type);
                prstmt.setString(8, txtID.getText());
                prstmt.setString(9, username.getText());
                prstmt.setString(10, pass.getText());
                prstmt.setString(11, note);
                prstmt.setInt(12, userID);
                prstmt.execute();
                Dialog.showDialog(root.getScene().getWindow(), "Details Updated", "Updated", DialogIcon.INFORMATION);
            } catch (SQLException ex) {
                Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadData();
        }
    }
    /**
     * Fill fields with user details that match UserID
     * @param event ActionEvent
     */
    @FXML private void moreDetails(ActionEvent event){
        if(tv.getSelectionModel().isEmpty()){
            Dialog.showDialog(root.getScene().getWindow(), "Please select one User to View more Information about them", "None Selected", DialogIcon.ERROR);
            return;
        }
        tabPane.getSelectionModel().select(addUserTab);
        UserInfo info= tv.getSelectionModel().getSelectedItem();
        userID = info.getUserID();
        btnUpdate.setDisable(false);
        btnAdd.setDisable(true);
        String sql="select * from users where UserID=?";
        try(PreparedStatement prstmt=con.prepareStatement(sql)) {
            prstmt.setInt(1, userID);
            ResultSet rs=prstmt.executeQuery();
            if(rs.next()){
                SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date sqlDate=rs.getDate("DOB");

                txtfn.setText(rs.getString("FirstName"));
                txtln.setText(rs.getString("LastName"));
                txtsurname.setText(rs.getString("Surname"));
                txtDOB.setText(dateFormat.format(sqlDate));
                txtResidence.setText(rs.getString("Residence"));
                txtMobile.setText(rs.getString("Mobile"));
                txtID.setText(rs.getString("NationalID"));
                username.setText(rs.getString("Username"));
                pass.setText(rs.getString("Password"));

                if(rs.getString("Type").equals("System User")){
                    rdSysUser.setSelected(true);
                }else{
                    rdSysAdmin.setSelected(true);
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Delete selected user details
     * @param event 
     */
    @FXML private void delete(ActionEvent event){
        if(tv.getSelectionModel().isEmpty()){
            Dialog.showDialog(root.getScene().getWindow(), "Please select one User to delete", "None Selected", DialogIcon.ERROR);
            return;
        }
        int option=Dialog.showConfirmDialog(root.getScene().getWindow(), "Are you sure you want to Delete", "Delete?", DialogIcon.QUESTION);
        if(option==DialogOption.OPTION_OK.value){
            UserInfo info= tv.getSelectionModel().getSelectedItem();
            userID = info.getUserID();
            data.remove(info);
            String sql="delete from users where UserID=?";
            try (PreparedStatement prstmt=con.prepareStatement(sql)){
                prstmt.setInt(1, userID);
                prstmt.execute();
            } catch (SQLException ex) {
                Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadData();
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
                Dialog.showDialog(owner,"Missing Details","Invalid Input",DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        inputFileds.removeAll(txtDOB,txtMobile,username);
        for (TextField textField : inputFileds) {
            if(textField.getText().length()>30){
                //set css stroke
                Dialog.showDialog(owner,"Sorry the text you entered can not be greater that 30 characters","Text too Long",DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        if(username.getText().length()>20 || username.getText().length()<4){
            Dialog.showDialog(owner, "Sorry your Username has to be More than 3 Characters and can not Exceed 20 Charchaters", "Invalid input",DialogIcon.WARNING);
            username.requestFocus();
            return false;
        }
        if(pass.getText().isEmpty()){
            Dialog.showDialog(owner,"Missing Details","Invalid Input",DialogIcon.WARNING);
            pass.requestFocus();
            return false;
        }
        if(pass.getText().length()>20 || pass.getText().length()<4){
            Dialog.showDialog(owner, "Sorry your Password has to be More than 3 Characters and can not Exceed 20 Charchaters", "Invalid Input",DialogIcon.WARNING);
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
                Dialog.showDialog(owner,"Invalid Date of Birth", "Invalid Input",DialogIcon.WARNING);
                txtDOB.requestFocus();
                return false;
            }
            String initialEntry=txtDOB.getText();
            String parsedValue=dateFormat.format(date);
            if(!initialEntry.equals(parsedValue)){
               Dialog.showDialog(owner, "Note your Date of Birth has been corrected", "Date Corrected",DialogIcon.INFORMATION);
            }
            txtDOB.setText(dateFormat.format(date));
        } catch (ParseException  ex) {
            Dialog.showDialog(owner,"Invalid Date of Birth", "Invalid Input",DialogIcon.WARNING);
            txtDOB.requestFocus();
            return false;
        }
        try {
            int mobile=Integer.parseInt(txtMobile.getText());
        } catch (NumberFormatException e) {
            Dialog.showDialog(owner, "Invalid Mobile Number", "Invalid data",DialogIcon.WARNING);
            txtMobile.requestFocus();
            return false;
        }
        if(txtMobile.getText().length()!= 10){
            Dialog.showDialog(owner, "Sorry your Mobile Number is invalid", "Invalid input",DialogIcon.WARNING);
            txtMobile.requestFocus();
            return false;
        }
        if (btnUpdate.isDisabled()) {//if action is update don't check whether username password alreday exists
            String sql = "select Username,Password from users where Username=?";
            try (PreparedStatement prstmt = con.prepareStatement(sql)) {
                prstmt.setString(1, username.getText());
                ResultSet set = prstmt.executeQuery();
                if (set.next()) {
                    Dialog.showDialog(owner, "Sorry Username already exists\nPlease change to different Username", "Username Exists", DialogIcon.WARNING);
                    username.requestFocus();
                    return false;
                }
                set.close();
            } catch (SQLException e) {
            }
        }
        return true;
    }
    /**
     * Load Users details to table
     */
    private void loadData(){
        data.clear();
        String sql="select UserID,FirstName,LastName,Surname,Type,Username from users";
        try (PreparedStatement prstmt=con.prepareStatement(sql);
                ResultSet rs=prstmt.executeQuery()){
            String name;
            while (rs.next()) {
                name=rs.getString("FirstName")+" "+rs.getString("LastName")+" "+rs.getString("Surname");
                data.add(new UserInfo(rs.getInt("UserID"), name ,rs.getString("Type"), rs.getString("Username")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
