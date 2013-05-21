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
public class Driver {
    private IntegerProperty driverID;
    private StringProperty name;
    private StringProperty mobile;
    private StringProperty status;
    private StringProperty entryDate;

    public Driver(int id,String name,String mobile,String status,Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM,yyyy");
        setDriverID(id);
        setName(name);
        setModel(mobile);
        setStatus(status);
        setEntryDate(dateFormat.format(date));
    }
    public IntegerProperty getDriverIDProperty() {
        if(driverID == null){
            driverID = new SimpleIntegerProperty();
        }
        return driverID;
    }
    public StringProperty getNameProperty() {
        if(name == null){
            name=new SimpleStringProperty();
        }
        return name;
    }
    public StringProperty getMobilelProperty() {
        if(mobile == null){
            mobile = new SimpleStringProperty();
        }
        return mobile;
    }
    public StringProperty getStatusProperty() {
        if(status == null){
            status = new SimpleStringProperty();
        }
        return status;
    }
    public StringProperty getEntryDateProperty() {
        if(entryDate == null){
            entryDate = new SimpleStringProperty();
        }
        return entryDate;
    }
    public final void setDriverID(int id){
       getDriverIDProperty().setValue(id);
    }
    public final void setName(String name){
       getNameProperty().setValue(name);
    }
    public final void setModel(String type){
        getMobilelProperty().setValue(type);
    }
    public final void setStatus(String username){
        getStatusProperty().setValue(username);
    }
    public final void setEntryDate(String str){
        getEntryDateProperty().setValue(str);
    }
    public int getDriverID(){
        return getDriverIDProperty().getValue();
    }
    public String getName(){
        return getNameProperty().getValueSafe();
    }
    public String getMobile(){
        return getMobilelProperty().getValueSafe();
    }
    public String getStatus(){
        return getStatusProperty().getValueSafe();
    }
    public String getEntryDate(){
        return getEntryDateProperty().getValueSafe();
    }
}
