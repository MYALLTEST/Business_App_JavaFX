package transconnect.system;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiarie
 */
public class UserInfo {
    private IntegerProperty userID;
    private StringProperty name;
    private StringProperty userType;
    private StringProperty username;

    public UserInfo(int id,String name,String type,String username) {
        setUserID(id);
        setName(name);
        setUserType(type);
        setUsername(username);
    }
    public IntegerProperty getUserIDProperty() {
        if(userID == null){
            userID = new SimpleIntegerProperty();
        }
        return userID;
    }
    public StringProperty getNameProperty() {
        if(name == null){
            name=new SimpleStringProperty();
        }
        return name;
    }
    public StringProperty getUserTypeProperty() {
        if(userType == null){
            userType = new SimpleStringProperty();
        }
        return userType;
    }
    public StringProperty getUsernameProperty() {
        if(username == null){
            username = new SimpleStringProperty();
        }
        return username;
    }
    public final void setUserID(int id){
       getUserIDProperty().setValue(id);
    }
    public final void setName(String name){
       getNameProperty().setValue(name);
    }
    public final void setUserType(String type){
        getUserTypeProperty().setValue(type);
    }
    public final void setUsername(String username){
        getUsernameProperty().setValue(username);
    }
    public int getUserID(){
        return getUserIDProperty().getValue();
    }
    public String getName(){
        return getNameProperty().getValueSafe();
    }
    public String getUserType(){
        return getUserTypeProperty().getValueSafe();
    }
    public String getUsername(){
        return getUsernameProperty().getValueSafe();
    }
}
