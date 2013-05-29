package transconnect.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    @FXML private ListView<Driver> driverList;
    @FXML private Label lblVehicle;
    @FXML private Label lblDriver;
    
    private Connection con;
    private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();
    private ObservableList<Driver> driverData = FXCollections.observableArrayList();
    private ArrayList<Integer> vehicleIDList = new ArrayList<>();
    private ArrayList<Integer> driverIDList = new ArrayList<>();
    private Date fromDate,toDate;
    private int vehicleIndex,driverIndex;
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
        vehicleList.setItems(vehicleData);
        vehicleList.setCellFactory(new Callback<ListView<Vehicle>, ListCell<Vehicle>>() {

            @Override
            public ListCell<Vehicle> call(ListView<Vehicle> param) {
                return new VehicleListCell();
            }
        });
        driverList.setItems(driverData);
        driverList.setCellFactory(new Callback<ListView<Driver>, ListCell<Driver>>() {

            @Override
            public ListCell<Driver> call(ListView<Driver> param) {
                return new DriverListCell();
            }
        });
    }
    /**
     * Load ChoiceBox with the options available
     */
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
    /**
     * Search to identify the available vehicles and drivers during the duration set
     * @param event ActionEvent
     */
    @FXML private void search(ActionEvent event){
        if (validatedInputFields()) {
            loadVehicles();
            loadDrivers();
        }
    }
    /**
     * Search and list vehicles that are available in the duration set 
     */
    private void loadVehicles(){
        vehicleData.clear();
        vehicleIDList.clear();
        String selected = cbSeater.getSelectionModel().getSelectedItem();
        int end = selected.indexOf(" ");
        selected=selected.substring(0, end);
        int seats=Integer.parseInt(selected);
        String sql="select * from vehicles where NumSeats=?";
        try (PreparedStatement prstmt= con.prepareStatement(sql)){
            prstmt.setInt(1, seats);
            ResultSet rs= prstmt.executeQuery();
            while (rs.next()) {
                vehicleIDList.add(rs.getInt("VehicleID"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!vehicleIDList.isEmpty()){
            for (int i = 0; i < vehicleIDList.size(); i++) {
                Integer id = vehicleIDList.get(i);
                vehicleIndex=id;
                sql = "select FromDate,ToDate from vehicle_schedule where VehicleID=? order by ToDate desc";
                try (PreparedStatement prstmt=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY)){
                    prstmt.setInt(1, id);
                    ResultSet rs=prstmt.executeQuery();
                    CompareDates cd= new CompareDates();
                    if(rs.next()){
                        if((cd.greater(fromDate, rs.getDate("ToDate")))){
                            //allow and exit
                            addVehicle();
                            continue;
                        }else if(cd.less(fromDate, rs.getDate("ToDate")) && cd.less(fromDate, rs.getDate("FromDate"))){
                            searchVehicle(rs, rs.getRow(), rs.getDate("FromDate"));
                        }else if(cd.less(fromDate, rs.getDate("ToDate")) && cd.greater(fromDate, rs.getDate("FromDate"))){
                            //do not allow
                        }
                    }else{
                        addVehicle();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    /**
     * A recursive search to identify whether a vehicle is available in the prescribed duration
     * @param rs ResultSet
     * @param index Integer ResultSet row to check
     * @param prevDate Date FromDate - from Vehicle_Schedule table
     * @throws SQLException 
     */
    private void searchVehicle(ResultSet rs,int index,Date prevDate) throws SQLException{
        if(rs.absolute(index)){
            CompareDates cd= new CompareDates();
            if(cd.greater(fromDate, rs.getDate("ToDate")) && cd.less(toDate, prevDate)){
                //allow and exit
                addVehicle();
            }else if(cd.less(toDate, rs.getDate("FromDate"))){
                searchVehicle(rs, index+1, rs.getDate("FromDate"));
            }
        }
    }
    /**
     * Add new vehicle to available vehicles ListView
     */
    private void addVehicle(){
        String sql="select * from vehicles where VehicleID=?";
            try (PreparedStatement prstmt= con.prepareStatement(sql)){
                prstmt.setInt(1, vehicleIndex);
            try (ResultSet rs = prstmt.executeQuery()) {
                int count=1;
                while (rs.next()) {                    
                    vehicleData.add(new Vehicle(rs.getInt("VehicleID"),count, rs.getString("Registration"), rs.getString("Model"), rs.getString("Status"), rs.getInt("NumSeats"), rs.getDate("EntryDate")));
                    count++;
                }
            }
            } catch (SQLException ex) {
                Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    /**
     * Search and list drivers that are available in the duration set 
     */
    private void loadDrivers(){
        driverData.clear();
        driverIDList.clear();
            
        String sql="select DriverID from Drivers";
        try (PreparedStatement prstmt= con.prepareStatement(sql);
                ResultSet rs= prstmt.executeQuery()){
            while (rs.next()) {
                driverIDList.add(rs.getInt("DriverID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!driverIDList.isEmpty()){
            for (int i = 0; i < driverIDList.size(); i++) {
                Integer id = driverIDList.get(i);
                driverIndex=id;
                sql = "select FromDate,ToDate from driver_schedule where DriverID=? order by ToDate desc";
                try (PreparedStatement prstmt=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY)){
                    prstmt.setInt(1, id);
                    ResultSet rs=prstmt.executeQuery();
                    CompareDates cd= new CompareDates();
                    if(rs.next()){
                        if((cd.greater(fromDate, rs.getDate("ToDate")))){
                            //allow and exit
                            addDriver();
                            continue;
                        }else if(cd.less(fromDate, rs.getDate("ToDate")) && cd.less(fromDate, rs.getDate("FromDate"))){
                            searchDriver(rs, rs.getRow(), rs.getDate("FromDate"));
                        }else if(cd.less(fromDate, rs.getDate("ToDate")) && cd.greater(fromDate, rs.getDate("FromDate"))){
                            //do not allow
                        }
                    }else{
                        addDriver();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    /**
     * A recursive search to identify whether a driver is available in the prescribed duration
     * @param rs ResultSet
     * @param index Integer ResultSet row to check
     * @param prevDate Date FromDate - from Driver_Schedule table
     * @throws SQLException 
     */
    private void searchDriver(ResultSet rs,int index,Date prevDate) throws SQLException{
        if(rs.absolute(index)){
            CompareDates cd= new CompareDates();
            if(cd.greater(fromDate, rs.getDate("ToDate")) && cd.less(toDate, prevDate)){
                //allow and exit
                addDriver();
            }else if(cd.less(toDate, rs.getDate("FromDate"))){
                searchDriver(rs, index+1, rs.getDate("FromDate"));
            }
        }
    }
    /**
     * Add new driver to drivers ListView
     */
    private void addDriver(){
        String sql="select * from drivers where DriverID=?";
            try (PreparedStatement prstmt= con.prepareStatement(sql)){
                prstmt.setInt(1, driverIndex);
            try (ResultSet rs = prstmt.executeQuery()) {
                String name;
                while (rs.next()) {
                    name=rs.getString("FirstName")+" "+rs.getString("LastName")+" "+rs.getString("Surname");
                    driverData.add(new Driver(rs.getInt("DriverID"), name, rs.getString("Mobile"), rs.getString("Status"), rs.getDate("EntryDate")));
                }
            }
            } catch (SQLException ex) {
                Logger.getLogger(NewBookingController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    /**
     * Validate the String Dates entered during input process
     * @param textField TextField
     * @return True if everything is correct
     */
    private boolean validateDate(TextField textField){
        SimpleDateFormat dateFormat= new SimpleDateFormat("d/M/yyyy");
        try {
            Date date=dateFormat.parse(textField.getText());
            Calendar currentCal=Calendar.getInstance();//current Date
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
            currentCal.add(Calendar.MONTH, 6);
            if(enteredCal.after(currentCal)){
                Dialog.showMessageDialog(root.getScene().getWindow(), "Sorry you can not set a Date more than 6 months into the future", "Invalid Date", DialogIcon.WARNING);
                textField.requestFocus();
                return false;
            }
        } catch (ParseException ex) {
            Dialog.showMessageDialog(root.getScene().getWindow(), "Invalid Date", "Invalid Date", DialogIcon.WARNING);
            textField.requestFocus();
            return false;
        }
        return true;
    }
    /**
     * Validate all input fields continuing
     * @return True if everything is correct
     */
    private boolean validatedInputFields(){
        Window owner=root.getScene().getWindow();
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
                        if (toDate.before(fromDate)) {//To date comes before From date
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
                calFrom.add(Calendar.DAY_OF_MONTH, numDays-1);
                txtTo.setText(dateFormat.format(calFrom.getTime()));
                toDate=calFrom.getTime();
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
    
    @FXML private void selectVehicle(MouseEvent event){
        if(!vehicleList.getSelectionModel().isEmpty()){
            Vehicle v= vehicleList.getSelectionModel().getSelectedItem();
            lblVehicle.setText(v.getModel()+" "+v.getRegistration());
        }
    }
    @FXML private void selectDriver(MouseEvent event){
        if(!driverList.getSelectionModel().isEmpty()){
            Driver d= driverList.getSelectionModel().getSelectedItem();
            lblDriver.setText(d.getName());
        }
    }
    @FXML private void cancle(ActionEvent event){
        txtFrom.setText("");
        txtTo.setText("");
        txtNum.setText("");
        vehicleData.clear();
        driverData.clear();
        lblVehicle.setText("");
        lblDriver.setText("");
        txtFrom.requestFocus();
    }
    @FXML private void next(ActionEvent event){
        if(lblVehicle.getText().isEmpty() || lblVehicle.getText().isEmpty()){
            Dialog.showMessageDialog(root.getScene().getWindow(), "Please select one Vehicle and Driver before continuing", "Missing Details", DialogIcon.INFORMATION);
            return;
        }
        //show next window
        System.out.println("we good");
    }
}
