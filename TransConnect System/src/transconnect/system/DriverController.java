package transconnect.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class DriverController implements Initializable {
    
    @FXML private TextField txtfn;
    @FXML private TextField txtln;
    @FXML private TextField txtsurname;
    @FXML private TextField txtResidence;
    @FXML private TextField txtMobile;
    @FXML private Label lblEntryDate;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private CheckBox cbActive;
    @FXML private Tab addNewTab;
    @FXML private TabPane tabPane;
    @FXML private AnchorPane root;
    @FXML private Rectangle rect;
    @FXML private Line line;
    @FXML private TableView<Driver> tv;
    @FXML private TableColumn<Driver,Integer> colID;
    @FXML private TableColumn<Driver,String> colName;
    @FXML private TableColumn<Driver,String> colMobile;
    @FXML private TableColumn<Driver,String> colStatus;
    @FXML private TableColumn<Driver,String> colEntryDate;
    
    private Connection con;
    private SimpleDateFormat dateFormat;
    Calendar cal= Calendar.getInstance();
    private int driverID;
    private ObservableList<TextField> textFields = FXCollections.observableArrayList();
    private ObservableList<Driver> data = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            con= new DBUtility().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        root.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setWidth((double)newValue);
                line.setEndX((double)newValue);
            }
        });
        dateFormat= new SimpleDateFormat("d MMMM, yyyy");
        lblEntryDate.setText(dateFormat.format(cal.getTime()));
        cbActive.setAllowIndeterminate(false);
        
        //configure table
        colID.setCellValueFactory(new PropertyValueFactory<Driver,Integer>("driverID"));
        colName.setCellValueFactory(new PropertyValueFactory<Driver,String>("name"));
        colMobile.setCellValueFactory(new PropertyValueFactory<Driver,String>("mobile"));
        colStatus.setCellValueFactory(new PropertyValueFactory<Driver,String>("status"));
        colEntryDate.setCellValueFactory(new PropertyValueFactory<Driver,String>("entryDate"));
        tv.setItems(data);
        
        
    }
    @FXML private void add(ActionEvent event){
        String status="deactive";
        if(cbActive.isSelected()){
            status="active";
        }
        java.sql.Date entryDate= new java.sql.Date(cal.getTimeInMillis());
        if(validateInputFields()){
            String sql="insert into drivers (FirstName,LastName,Surname,Residence,Mobile,EntryDate,Status) values(?,?,?,?,?,?,?)";
            try (PreparedStatement prstmt=con.prepareStatement(sql)){
                prstmt.setString(1, txtfn.getText());
                prstmt.setString(2, txtln.getText());
                prstmt.setString(3, txtsurname.getText());
                prstmt.setString(4, txtResidence.getText());
                prstmt.setString(5, txtMobile.getText());
                prstmt.setDate(6, entryDate);
                prstmt.setString(7, status);
                prstmt.execute();
                Dialog.showMessageDialog(root.getScene().getWindow(), "New Driver Added", "Details Saved", DialogIcon.INFORMATION);
                btnAdd.setDisable(true);
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @FXML private void clear(ActionEvent event){
        textFields.clear();
        textFields.addAll(txtfn,txtln,txtsurname,txtResidence,txtMobile);
        for (TextField textField : textFields) {
            textField.setText("");
        }
        cbActive.setSelected(true);
        btnUpdate.setDisable(true);
        btnAdd.setDisable(false);
        txtfn.requestFocus();
    }
    @FXML private void update(ActionEvent event){
        String status="deactive";
        if(cbActive.isSelected()){
            status="active";
        }
        if(validateInputFields()){
            String sql="update drivers set FirstName=?,LastName=?,Surname=?,Residence=?,Mobile=?,Status=? where DriverID=?";
            try (PreparedStatement prstmt=con.prepareStatement(sql)){
                prstmt.setString(1, txtfn.getText());
                prstmt.setString(2, txtln.getText());
                prstmt.setString(3, txtsurname.getText());
                prstmt.setString(4, txtResidence.getText());
                prstmt.setString(5, txtMobile.getText());
                prstmt.setString(6, status);
                prstmt.setInt(7, driverID);
                prstmt.execute();
                Dialog.showMessageDialog(root.getScene().getWindow(), "Details Updated", "Update", DialogIcon.INFORMATION);
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @FXML private void updateInformation(ActionEvent event){
        if(tv.getSelectionModel().isEmpty()){
            Dialog.showMessageDialog(root.getScene().getWindow(), "Please select a Driver to View or Update Information", "None Selected", DialogIcon.ERROR);
            return;
        }
        Driver driver=tv.getSelectionModel().getSelectedItem();
        driverID=driver.getDriverID();
        tabPane.getSelectionModel().select(addNewTab);
        btnUpdate.setDisable(false);
        String sql="select * from drivers where DriverID=?";
        try (PreparedStatement prstmt=con.prepareStatement(sql)){
            prstmt.setInt(1, driverID);
            ResultSet rs=prstmt.executeQuery();
            if(rs.next()){
              txtfn.setText(rs.getString("FirstName"));
              txtln.setText(rs.getString("LastName"));
              txtsurname.setText(rs.getString("Surname"));
              txtResidence.setText(rs.getString("Residence"));
              txtMobile.setText(rs.getString("Mobile"));
              lblEntryDate.setText(dateFormat.format(rs.getDate("EntryDate")));
              if(rs.getString("Status").equals("active")){
                  cbActive.setSelected(true);
              }else{
                  cbActive.setSelected(false);
              }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void loadData(){
        data.clear();
        String sql="select * from drivers";
        try(PreparedStatement prstmt=con.prepareStatement(sql)) {
            ResultSet rs=prstmt.executeQuery();
            String name;
            while (rs.next()) {                
                name=rs.getString("FirstName")+" "+rs.getString("LastName")+" "+rs.getString("Surname");
                data.add(new Driver(rs.getInt("DriverID"), name, rs.getString("Mobile"), rs.getString("Status"), rs.getDate("EntryDate")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean validateInputFields(){
        textFields.clear();
        textFields.addAll(txtfn,txtln,txtsurname,txtResidence,txtMobile);
        Window owner=root.getScene().getWindow();
        for (TextField textField : textFields) {
            if(textField.getText().isEmpty()){
                Dialog.showMessageDialog(owner, "Please enter required information", "Missing details", DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        textFields.remove(txtMobile);
        for (TextField textField : textFields) {
            if(textField.getText().length()>30){
                Dialog.showMessageDialog(owner, "Sorry the text you entered can not be greater that 30 characters","Text too Long",DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        if(txtMobile.getText().length()!=10){
            Dialog.showMessageDialog(owner, "Please ensure Mobile Number does not Exceed 10 digits", "Invalid Mobile Number", DialogIcon.WARNING);
            txtMobile.requestFocus();
            return false;
        }
        try {
            int mobile = Integer.parseInt(txtMobile.getText());
        } catch (NumberFormatException numberFormatException) {
            Dialog.showMessageDialog(owner, "Mobile Number can only contain digits", "Invalid Mobile Number", DialogIcon.WARNING);
            txtMobile.requestFocus();
            return false;
        }
        return true;
    }
}
