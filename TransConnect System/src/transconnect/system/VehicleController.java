package transconnect.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
public class VehicleController implements Initializable {
    
    @FXML private TextField txtReg;
    @FXML private TextField txtModel;
    @FXML private TextField txtNumSeats;
    @FXML private TextArea taComment;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private AnchorPane root;
    @FXML private Rectangle rect;
    @FXML private Line line;
    @FXML private TabPane tabPane;
    @FXML private Tab newVehicleTab;
    @FXML private RadioButton rdActive;
    @FXML private RadioButton rdRetired;
    @FXML private Label lblDate;
    @FXML private TableView<Vehicle> tv;
    @FXML private TableColumn<Vehicle,Integer> colCount;
    @FXML private TableColumn<Vehicle,String> colRegistration;
    @FXML private TableColumn<Vehicle,String> colModel;
    @FXML private TableColumn<Vehicle,String> colStatus;
    @FXML private TableColumn<Vehicle,Integer> colNumSeats;
    @FXML private TableColumn<Vehicle,String> colEntryDate;
    
    private Connection con;
    private int num;
    private Date date;
    private int vehicleID;
    private String reg;
    private SimpleDateFormat dateFormat;
    private ObservableList<TextField> txtFields = FXCollections.observableArrayList();
    private ObservableList<Vehicle>data = FXCollections.observableArrayList();
    private Text tableMessage= new Text("No records Found");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            con= new DBUtility().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        root.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setWidth((double)newValue);
                line.setEndX((double)newValue);
            }
        });
        
        dateFormat= new SimpleDateFormat("d MMM,yyyy");
        Calendar cal= Calendar.getInstance();
        date=cal.getTime();
        lblDate.setText(dateFormat.format(date));
        txtFields.addAll(txtReg,txtModel,txtNumSeats);
        
        //configure table columns
        colCount.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("count"));
        colRegistration.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("registration"));
        colModel.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("model"));
        colStatus.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("status"));
        colNumSeats.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("numSeats"));
        colEntryDate.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("entryDate"));
        tv.setItems(data);
        tv.setPlaceholder(tableMessage);
        
        loadData();
    }
    @FXML private void addNew(ActionEvent event){
       String status="Active";
       if(rdRetired.isSelected()){
           status="Retired";
       }
       if(validateInputFields()){
           java.sql.Date entryDate= new java.sql.Date(date.getTime());
           String sql="insert into vehicles (Registration,Model,NumSeats,EntryDate,Status,Comment) values(?,?,?,?,?,?)";
           try(PreparedStatement prstmt=con.prepareStatement(sql)) {
               prstmt.setString(1, txtReg.getText());
               prstmt.setString(2, txtModel.getText());
               prstmt.setInt(3, num);
               prstmt.setDate(4, entryDate);
               prstmt.setString(5, status);
               prstmt.setString(6, taComment.getText());
               prstmt.execute();
               Dialog.showMessageDialog(root.getScene().getWindow(), "New Vehicle Added", "Details Saved", DialogIcon.INFORMATION);
               btnAdd.setDisable(true);
               loadData();
           } catch (SQLException ex) {
               Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }
    private boolean validateInputFields(){
        Window owner=root.getScene().getWindow();
        for (TextField textField : txtFields) {
            if(textField.getText().isEmpty()){
                Dialog.showMessageDialog(owner, "Missing Details", "Invalid Input", DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        }
        if(txtReg.getText().length()>8){
            Dialog.showMessageDialog(owner, "Registration Number can not exceed 8 characters", "Invalid input", DialogIcon.WARNING);
            txtReg.requestFocus();
            return false;
        }
        txtReg.setText(txtReg.getText().toUpperCase());
        if(txtModel.getText().length()>15){
            Dialog.showMessageDialog(owner, "Model name can not exceed 15 characters", "Invalid input", DialogIcon.WARNING);
            txtModel.requestFocus();
            return false;
        }
        if(taComment.getText().length()>300){
            Dialog.showMessageDialog(owner, "Comment can not exceed 300 characters", "Invalid input", DialogIcon.WARNING);
            taComment.requestFocus();
            return false;
        }
        try {
            num = Integer.parseInt(txtNumSeats.getText());
        } catch (NumberFormatException numberFormatException) {
            Dialog.showMessageDialog(owner, "Invalid Number of Seats", "Inavlid Input", DialogIcon.WARNING);
            txtNumSeats.requestFocus();
            return false;
        }
        if(num>65){
            Dialog.showMessageDialog(owner, "Number of Seats can not exceed 65 seats", "Invalid Input", DialogIcon.WARNING);
            txtNumSeats.requestFocus();
            return false;
        }
        if (btnUpdate.isDisabled()) {//perform this check if we are adding new vehicle
            return checkRegistration();
        }
        if(!btnUpdate.isDisabled() && !txtReg.getText().equals(reg)){//perform this check if we are updating and registration has been altered
            return checkRegistration();
        }
        return true;
    }
    private boolean checkRegistration(){
        String sql = "select Registration from vehicles where Registration=?";
            try (PreparedStatement prstmt = con.prepareStatement(sql)) {
                prstmt.setString(1, txtReg.getText());
                ResultSet rs = prstmt.executeQuery();
                if (rs.next()) {
                    Dialog.showMessageDialog(root.getScene().getWindow(), "Sorry the Registration entered already Exist", "Registration Exists", DialogIcon.INFORMATION);
                    txtReg.requestFocus();
                    return false;
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        return true;
    }
    @FXML private void clear(ActionEvent event){
        for (TextField textField : txtFields) {
            textField.setText("");
        }
        taComment.setText("");
        lblDate.setText(dateFormat.format(date));
        rdActive.setSelected(true);
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        txtReg.requestFocus();
    }

    @FXML private void updateInformation(ActionEvent event){
        if(tv.getSelectionModel().isEmpty()){
            Dialog.showMessageDialog(root.getScene().getWindow(), "Please select a Vehicle to View or Update Information", "None Selected", DialogIcon.ERROR);
            return;
        }
       Vehicle v=tv.getSelectionModel().getSelectedItem();
       vehicleID=v.getVehicleID();
       btnUpdate.setDisable(false);
       btnAdd.setDisable(true);
       tabPane.getSelectionModel().select(newVehicleTab);
       String sql="select * from vehicles where VehicleID=?";
        try (PreparedStatement prstmt=con.prepareStatement(sql)){
            prstmt.setInt(1, vehicleID);
            ResultSet rs=prstmt.executeQuery();
            if(rs.next()){
                reg=rs.getString("Registration");
                txtReg.setText(reg);
                txtModel.setText(rs.getString("Model"));
                txtNumSeats.setText(Integer.toString(rs.getInt("NumSeats")));
                lblDate.setText(dateFormat.format(rs.getDate("EntryDate")));
                taComment.setText(rs.getString("Comment"));
                if(rs.getString("Status").equals("Active")){
                    rdActive.setSelected(true);
                }else{
                    rdRetired.setSelected(true);
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML private void update(ActionEvent event){
        String status="Active";
       if(rdRetired.isSelected()){
           status="Retired";
       }
       if(validateInputFields()){
           String sql="update vehicles set Registration=?,Model=?,NumSeats=?,Status=?,Comment=? where VehicleID=?";
           try(PreparedStatement prstmt=con.prepareStatement(sql)) {
               prstmt.setString(1, txtReg.getText());
               prstmt.setString(2, txtModel.getText());
               prstmt.setInt(3, num);
               prstmt.setString(4, status);
               prstmt.setString(5, taComment.getText());
               prstmt.setInt(6, vehicleID);
               prstmt.execute();
               Dialog.showMessageDialog(root.getScene().getWindow(), "Details Updated", "Updated", DialogIcon.INFORMATION);
               loadData();
           } catch (SQLException ex) {
               Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }
    private void loadData(){
        data.clear();
        String sql="select * from vehicles";
        try (PreparedStatement prstmt=con.prepareStatement(sql)){
            ResultSet rs= prstmt.executeQuery();
            int count=1;
            while(rs.next()){
                data.add(new Vehicle(rs.getInt("VehicleID"),count, rs.getString("Registration"), rs.getString("Model"), rs.getString("Status"), rs.getInt("NumSeats"), rs.getDate("EntryDate")));
                count++;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
