package transconnect.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiarie
 */
public class Vehicle {
    private IntegerProperty vehicleID;
    private IntegerProperty count;
    private StringProperty registration;
    private StringProperty model;
    private StringProperty status;
    private IntegerProperty numSeats;
    private StringProperty entryDate;

    public Vehicle(int id,int val,String registration,String model,String status,int numSeats,Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM,yyyy");
        setVehicleID(id);
        setCount(val);
        setRegistration(registration);
        setModel(model);
        setStatus(status);
        setEntryDate(dateFormat.format(date));
        setNumSeats(numSeats);
    }
    public IntegerProperty getVehicleIDProperty() {
        if(vehicleID == null){
            vehicleID = new SimpleIntegerProperty();
        }
        return vehicleID;
    }
    public IntegerProperty getCountProperty() {
        if(count == null){
            count = new SimpleIntegerProperty();
        }
        return count;
    }
    public StringProperty getRegistrationProperty() {
        if(registration == null){
            registration=new SimpleStringProperty();
        }
        return registration;
    }
    public StringProperty getModelProperty() {
        if(model == null){
            model = new SimpleStringProperty();
        }
        return model;
    }
    public StringProperty getStatusProperty() {
        if(status == null){
            status = new SimpleStringProperty();
        }
        return status;
    }
    public IntegerProperty getNumSeatsProperty() {
        if(numSeats == null){
            numSeats = new SimpleIntegerProperty();
        }
        return numSeats;
    }
    public StringProperty getEntryDateProperty() {
        if(entryDate == null){
            entryDate = new SimpleStringProperty();
        }
        return entryDate;
    }
    public final void setVehicleID(int id){
       getVehicleIDProperty().setValue(id);
    }
    public final void setCount(int val){
       getCountProperty().setValue(val);
    }
    public final void setRegistration(String name){
       getRegistrationProperty().setValue(name);
    }
    public final void setModel(String type){
        getModelProperty().setValue(type);
    }
    public final void setStatus(String username){
        getStatusProperty().setValue(username);
    }
    public final void setNumSeats(int num){
       getNumSeatsProperty().setValue(num);
    }
    public final void setEntryDate(String str){
        getEntryDateProperty().setValue(str);
    }
    public int getVehicleID(){
        return getVehicleIDProperty().getValue();
    }
    public int getCount(){
        return getCountProperty().getValue();
    }
    public String getRegistration(){
        return getRegistrationProperty().getValueSafe();
    }
    public String getModel(){
        return getModelProperty().getValueSafe();
    }
    public String getStatus(){
        return getStatusProperty().getValueSafe();
    }
    public int getNumSeats(){
       return getNumSeatsProperty().getValue();
    }
    public String getEntryDate(){
        return getEntryDateProperty().getValueSafe();
    }
}
