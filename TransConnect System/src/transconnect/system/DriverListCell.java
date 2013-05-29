package transconnect.system;

import javafx.scene.control.ListCell;

/**
 * Format how details are displayed in a Driver ListView
 * @author Kiarie
 */
public class DriverListCell extends ListCell<Driver>{

    @Override
    protected void updateItem(Driver item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(item == null){
            setText("");
        }else{
            setText(item.getName());
        }
    }
    
}
