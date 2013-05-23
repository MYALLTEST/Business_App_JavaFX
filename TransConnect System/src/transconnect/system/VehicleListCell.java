package transconnect.system;

import javafx.scene.control.ListCell;

/**
 * Format how details are displayed in ListCell
 * @author Kiarie
 */
 public class VehicleListCell extends ListCell<Vehicle>{

    @Override
    protected void updateItem(Vehicle item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(item == null){
            setText("");
        }else{
            String model = item.getModel();
            String reg = item.getRegistration();
            
            setText(model+"  "+reg);
        }
    }
}
