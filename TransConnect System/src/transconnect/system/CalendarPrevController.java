package transconnect.system;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Kiarie
 */
public class CalendarPrevController implements Initializable {
    
    @FXML private GridPane gp;
    @FXML private GridPane gp2;
    @FXML private TextField txtIndex;
    @FXML private TextField txtText;
    @FXML private AnchorPane root;
    @FXML private Label lblMonth;
    
   
    private int month;
    private ObservableList<Node> displayNodes;
    private ObservableList<Node> hiddenNodes;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        gp.setUserData("display");
        gp2.setUserData("hidden");
        displayNodes = getDisplayPane().getChildren();
        hiddenNodes = getHiddenPane().getChildren();
        initial(month, true,displayNodes);
    }
    @FXML private void set(ActionEvent event){
        ObservableList<Node> nodes=gp.getChildren();
        int index = 0;
        try {
            index = Integer.parseInt(txtIndex.getText());
        } catch (NumberFormatException numberFormatException) {
        }
        if(index<0){
            index=0;
        }
        if(index>nodes.size()){
            index=nodes.size()-1;
        }
        Label lbl= (Label) nodes.get(index);
        lbl.setText(txtText.getText());
    }
    @FXML private void setValues(ActionEvent event){
        initial(0,false,getHiddenPane().getChildren());
    }
    @FXML private void previous(ActionEvent event){
        month--;
        initial(month,true,getHiddenPane().getChildren());
        getHiddenPane().setLayoutX(-350);
        getDisplayPane().setLayoutX(0);
        KeyValue kv = new KeyValue(getDisplayPane().layoutXProperty(), 350);//move displayed gridpane to x=350
        KeyValue kv1 = new KeyValue(getHiddenPane().layoutXProperty(), 0);//move hidden gridpane to x=0
        KeyFrame frame = new KeyFrame(Duration.seconds(3), kv,kv1);
        Timeline timeline = new Timeline(frame);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {//once gridpanes have move to their new positions change their userdata
                getHiddenPane().setUserData("display");// to represent their current status whether displaying or hidden
                getDisplayPane().setUserData("hidden");// set hidden gridpane to the expected layoutX
                displayNodes = getDisplayPane().getChildren();
                hiddenNodes = getHiddenPane().getChildren();
            }
        });
        timeline.play();
    }
    @FXML private void next(ActionEvent event){
        month++;
        initial(month,true,getHiddenPane().getChildren());
        getHiddenPane().setLayoutX(350);
        getDisplayPane().setLayoutX(0);
        KeyValue kv = new KeyValue(getDisplayPane().layoutXProperty(), -350);//move displayed gridpane to x=-350
        KeyValue kv1 = new KeyValue(getHiddenPane().layoutXProperty(), 0);//move hidden gridpane to x=0
        KeyFrame frame = new KeyFrame(Duration.seconds(3), kv,kv1);
        Timeline timeline = new Timeline(frame);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {//once gridpanes have move to their new positions change their userdata
                getHiddenPane().setUserData("display");// to represent their current status whether displaying or hidden
                getDisplayPane().setUserData("hidden");// set hidden gridpane to the expected layoutX
                displayNodes = getDisplayPane().getChildren();
                hiddenNodes = getHiddenPane().getChildren();
            }
        });
        timeline.play();
    }
    private void initial(int val,boolean change,ObservableList<Node> nodes){
        //nodes 0-41
        //ObservableList<Node> nodes=getHiddenPane().getChildren();
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMMM, yyyy");
        Calendar cal=Calendar.getInstance();
        Calendar cal2=Calendar.getInstance();
        
        if (change) {
            cal.add(Calendar.MONTH, val);
            cal2.add(Calendar.MONTH, val);
        }
        
        lblMonth.setText(dateFormat.format(cal.getTime()));
        int endMonthVal=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal2.set(Calendar.DAY_OF_MONTH, 1);
        int startMonthVal= cal2.get(Calendar.DAY_OF_WEEK);
        
        cal2.add(Calendar.DAY_OF_MONTH, -1);
        int prevMonthEndVal=cal2.get(Calendar.DAY_OF_MONTH);
        int num=startMonthVal;
        prevMonthEndVal=prevMonthEndVal-(num-2);
        
        for (int i = 0; i < (startMonthVal-1); i++) {
            Label lbl = (Label) nodes.get(i);
            lbl.setText(Integer.toString(prevMonthEndVal));
            prevMonthEndVal++;
        }
        
        int day=1;
        for (int i = (startMonthVal-1); i < 42; i++) {
            Label lbl = (Label) nodes.get(i);
            lbl.setText(Integer.toString(day));
            if(day==endMonthVal){
                day=0;
            }
            day++;
        }
    }
    
    private GridPane getDisplayPane(){
        if(((String)gp.getUserData()).equals("display")){
            return gp;
        }else{
            return gp2;
        }
    }
    private GridPane getHiddenPane(){
        if(((String)gp.getUserData()).equals("hidden")){
            return gp;
        }else{
            return gp2;
        }
    }
}
