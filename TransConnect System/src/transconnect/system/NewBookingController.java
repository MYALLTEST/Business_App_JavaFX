package transconnect.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 * FXML Controller class
 *schedule
 * @author Kiarie
 */
public class NewBookingController implements Initializable {
    
    @FXML private TextField txtFrom;
    @FXML private TextField txtTo;
    @FXML private TextField txtNum;
    @FXML private ChoiceBox<String>cbSeater;
    @FXML private Rectangle rect;
    @FXML private Line line;
    @FXML private AnchorPane root;
    @FXML private ListView<Vehicle> vehicleList;
    
    private Connection con;
    private ObservableList<Vehicle> data = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            con= new DBUtility().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }root.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setWidth((double)newValue);
                line.setEndX((double)newValue);
            }
        });
        loadChoiceBox();
        vehicleList.setItems(data);
        vehicleList.setCellFactory(new Callback<ListView<Vehicle>, ListCell<Vehicle>>() {

            @Override
            public ListCell<Vehicle> call(ListView<Vehicle> param) {
                return new VehicleListCell();
            }
        });
    }
    private void loadChoiceBox(){
        cbSeater.getItems().clear();
        String sql="select distinct NumSeats from vehicles";
        try(Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(sql)) {
            while (rs.next()) {                
                String seats= Integer.toString(rs.getInt("NumSeats"))+" Seater";
                cbSeater.getItems().add(seats);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cbSeater.getSelectionModel().clearAndSelect(0);
    }
    @FXML private void loadVehicles(ActionEvent event){
        if (validatedInputFields()) {
            System.out.println("we are good");
            String selected = cbSeater.getSelectionModel().getSelectedItem();
            selected=selected.substring(0, 1);
            int seats=Integer.parseInt(selected);
            
            String sql="select * from vehicles where NumSeats=?";
            try (PreparedStatement prstmt= con.prepareStatement(sql)){
                prstmt.setInt(1, seats);
                ResultSet rs= prstmt.executeQuery();
                int count=1;
                while (rs.next()) {                    
                    data.add(new Vehicle(rs.getInt("VehicleID"),count, rs.getString("Registration"), rs.getString("Model"), rs.getString("Status"), rs.getInt("NumSeats"), rs.getDate("EntryDate")));
                    count++;
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private boolean validateDate(TextField textField){
        SimpleDateFormat dateFormat= new SimpleDateFormat("d/M/yyyy");
        try {
            Date date=dateFormat.parse(textField.getText());
            Calendar currentCal=Calendar.getInstance();
            Calendar enteredCal=Calendar.getInstance();
            enteredCal.setTime(date);
            textField.setText(dateFormat.format(date));
            String enteredDate=dateFormat.format(date);
            String currentDate=dateFormat.format(currentCal.getTime());
            if(!enteredDate.equals(currentDate)){
                if(enteredCal.before(currentCal)){
                    Dialog.showMessageDialog(root.getScene().getWindow(), "Your Date cannot be in the past", "Invalid Date", DialogIcon.WARNING);
                    textField.requestFocus();
                    return false;
                }
            }
        } catch (ParseException ex) {
            Dialog.showMessageDialog(root.getScene().getWindow(), "Invalid Date", "Invalid Date", DialogIcon.WARNING);
            textField.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatedInputFields(){
        Window owner=root.getScene().getWindow();
        Date fromDate,toDate;
        int numDays;
        if(txtFrom.getText().isEmpty()){
            Dialog.showMessageDialog(owner, "Please enter required Date", "Missing Details", DialogIcon.WARNING);
            txtFrom.requestFocus();
            return false;
        }
        if(!validateDate(txtFrom)){
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        if(!txtTo.getText().isEmpty()){//if To Date is not empty peform this check and use this to calculate number of days
            if (validateDate(txtTo)) {
                try {
                    String strFromDate=txtFrom.getText();
                    String strToDate=txtTo.getText();
                    fromDate = dateFormat.parse(strFromDate);
                    toDate = dateFormat.parse(strToDate);
                    if (!strFromDate.equals(strToDate)) {
                        if (toDate.before(fromDate)) {//To date comes befor From date
                            Dialog.showMessageDialog(owner, "Your To date can not come before From date", "Invalid Dates", DialogIcon.WARNING);
                            txtTo.requestFocus();
                            return false;
                        } else {
                            calFrom.setTime(fromDate);
                            calTo.setTime(toDate);
                            long diff = (toDate.getTime() - fromDate.getTime());
                            diff=(diff/(1000*60*60*24));//convert time in milliseconds to days
                            if(diff>5){
                                calFrom.add(Calendar.DAY_OF_MONTH, 5);
                                calTo.setTime(calFrom.getTime());
                                txtTo.setText(dateFormat.format(calTo.getTime()));
                                Dialog.showMessageDialog(owner, "Please note duration can not go past 5 Days hence your Dates have been corrected", "Dates Corrected", DialogIcon.INFORMATION);
                                diff=5;
                            }
                            txtNum.setText(Long.toString(diff));
                        }
                    }else{
                        txtNum.setText("1");
                    }
                } catch (ParseException ex) {
                    Dialog.showMessageDialog(root.getScene().getWindow(), "Invalid Dates", "Invalid Dates", DialogIcon.WARNING);
                    return false;
                }
            }
        }
        if(txtTo.getText().isEmpty() && !txtNum.getText().isEmpty()) {//if To duration is empty use Number of days to calculate the To duration 
            try {
                numDays = Integer.parseInt(txtNum.getText());
                if (numDays > 5 || numDays < 0) {
                    Dialog.showMessageDialog(owner, "Sorry accpeted values are 1 to 5", "Range 1 - 5", DialogIcon.INFORMATION);
                    txtNum.requestFocus();
                    return false;
                }
                fromDate = dateFormat.parse(txtFrom.getText());
                calFrom.setTime(fromDate);
                calFrom.add(Calendar.DAY_OF_MONTH, numDays);
                txtTo.setText(dateFormat.format(calFrom.getTime()));
            } catch (NumberFormatException numberFormatException) {
                Dialog.showMessageDialog(owner, "Only Numeric values are accpected", "Invalid Input", DialogIcon.WARNING);
                txtNum.requestFocus();
                return false;
            } catch (ParseException ex) {
                Dialog.showMessageDialog(root.getScene().getWindow(), "Invalid Date", "Invalid Date", DialogIcon.WARNING);
                txtFrom.requestFocus();
                return false;
            }
        }
        if(txtTo.getText().isEmpty() && txtNum.getText().isEmpty()){
            Dialog.showMessageDialog(owner, "Please fill required details.\nUse either To date or Number of Days", "Missing Details", DialogIcon.INFORMATION);
            txtNum.requestFocus();
            return false;
        }
        return true;
    }

}
