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
    private int count=1;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        gp.setUserData("display");
        gp2.setUserData("hidden");
        setCalendar(month, true,getDisplayPane().getChildren());
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
        setCalendar(0,false,getDisplayPane().getChildren());
        month=0;
    }
    @FXML private void previous(ActionEvent event){
        statusPane();
        month--;
        setCalendar(month,true,getHiddenPane().getChildren());
        getHiddenPane().setLayoutX(-350);
        KeyValue kv = new KeyValue(getDisplayPane().layoutXProperty(), 350);//move displayed gridpane to x=350
        KeyValue kv1 = new KeyValue(getHiddenPane().layoutXProperty(), 0);//move hidden gridpane to x=0
        KeyFrame frame = new KeyFrame(Duration.seconds(3), kv,kv1);
        Timeline timeline = new Timeline(frame);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {//once gridpanes have move to their new positions change their userdata
                GridPane hidden = getHiddenPane();
                GridPane display = getDisplayPane();
                hidden.setUserData("display");// to represent their current status whether displaying or hidden
                display.setUserData("hidden");// set hidden gridpane to the expected layoutX
                System.out.println("method called previous");
                statusPane();
            }
        });
        timeline.play();
    }
    @FXML private void next(ActionEvent event){
        statusPane();
        month++;
        setCalendar(month,true,getHiddenPane().getChildren());
        getHiddenPane().setLayoutX(350);
        KeyValue kv = new KeyValue(getDisplayPane().layoutXProperty(), -350);//move displayed gridpane to x=-350
        KeyValue kv1 = new KeyValue(getHiddenPane().layoutXProperty(), 0);//move hidden gridpane to x=0
        KeyFrame frame = new KeyFrame(Duration.seconds(3), kv,kv1);
        Timeline timeline = new Timeline(frame);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {//once gridpanes have move to their new positions change their userdata
                GridPane hidden = getHiddenPane();
                GridPane display = getDisplayPane();
                hidden.setUserData("display");// to represent their current status whether displaying or hidden
                display.setUserData("hidden");// set hidden gridpane to the expected layoutX
                System.out.println("method called next");
                statusPane();
            }
        });
        timeline.play();
    }
    private void setCalendar(int val,boolean change,ObservableList<Node> nodes){
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
            lbl.setDisable(true);
            prevMonthEndVal++;
        }
        
        int day=1;
        boolean disable=false;
        for (int i = (startMonthVal-1); i < 42; i++) {
            Label lbl = (Label) nodes.get(i);
            lbl.setText(Integer.toString(day));
            lbl.setDisable(disable);
            if(day==endMonthVal){
                day=0;
                disable=true;
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
    private void statusPane(){
        System.out.println("--------------------count="+count+"--------------------------");
        String str=(String) gp.getUserData();
        System.out.println("gp: "+str);
        String str2=(String) gp2.getUserData();
        System.out.println("gp2: "+str2);
        System.out.println("--------------------count="+count+"--------------------------");
        count++;
    }
}
