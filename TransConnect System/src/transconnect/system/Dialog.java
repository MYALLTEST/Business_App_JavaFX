package transconnect.system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Kiarie
 */
    
public class Dialog  {
    private static int option=2;
    /**
     * Create simple message dialog
     * @param owner Window
     * @param message Message to display
     * @param title Title to used by the message dialog
     * @param dialogIcon Type of icon to use
     */
    public static void showDialog(Window owner,String message,String title,DialogIcon dialogIcon){
       final Stage stage= new Stage(StageStyle.UTILITY);
        
        VBox root= new VBox(5);
        root.setPadding(new Insets(6));
        String appCSS = Dialog.class.getResource("main.css").toExternalForm();
        root.getStylesheets().add(appCSS);
        
        Image image;
        switch(dialogIcon.getValue()){
            case 1:
                image= new Image(Dialog.class.getResourceAsStream("/resources/info.png"));
                break;
            case 2:
                image= new Image(Dialog.class.getResourceAsStream("/resources/question.png"));
                break;
            case 3:
                image= new Image(Dialog.class.getResourceAsStream("/resources/error.png"));
                break;
            case 4:
                image= new Image(Dialog.class.getResourceAsStream("/resources/warning.png"));
                break;
                default:
                image= new Image(Dialog.class.getResourceAsStream("/resources/info.png"));
                    
        }
        
        
        Label lblMsg= new Label(message, new ImageView(image));
        lblMsg.setPrefSize(249, 75);
        lblMsg.setWrapText(true);
        lblMsg.setAlignment(Pos.CENTER_LEFT);
        lblMsg.setFont(new Font("Tahoma", 11));
        
        Button btnOK= new Button("OK");
        btnOK.setDefaultButton(true);
        btnOK.setPrefSize(60, 45);
        root.setAlignment(Pos.CENTER_RIGHT);
        btnOK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        
        root.getChildren().addAll(lblMsg,btnOK);
        
        Scene scene= new Scene(root, 250, 85);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }
    /**
     * Show a confirmation dialog box that returns a value depending on what the user selects
     * @param owner Window
     * @param message Message to display
     * @param title Title to used by the message dialog
     * @param dialogIcon Type of icon to use
     * @return Integer 
     */
    public static int showConfirmDialog(Window owner,String message,String title,DialogIcon dialogIcon){
        final Stage stage= new Stage(StageStyle.UTILITY);
        
        VBox root= new VBox(5);
        root.setPadding(new Insets(6));
        String appCSS = Dialog.class.getResource("main.css").toExternalForm();
        root.getStylesheets().add(appCSS);
        
        Image image;
        switch(dialogIcon.getValue()){
            case 2:
                image= new Image(Dialog.class.getResourceAsStream("/resources/question.png"));
                break;
            default:
                image= new Image(Dialog.class.getResourceAsStream("/resources/info.png"));
                    
        }
        
        
        Label lblMsg= new Label(message, new ImageView(image));
        lblMsg.setPrefSize(249, 75);
        lblMsg.setWrapText(true);
        lblMsg.setAlignment(Pos.CENTER_LEFT);
        lblMsg.setFont(new Font("Tahoma", 11));
        
        Button btnOK= new Button("OK");
        btnOK.setDefaultButton(true);
        btnOK.setPrefSize(60, 45);
        btnOK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                setOption(1);
                stage.close();
            }
        });
        Button btnCancel= new Button("Cancel");
        btnCancel.setCancelButton(true);
        btnCancel.setPrefSize(60, 45);
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                setOption(2);
                stage.close();
            }
        });
        HBox hBox= new HBox(5);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnOK,btnCancel);
        
        root.setAlignment(Pos.CENTER_RIGHT);
        root.getChildren().addAll(lblMsg,hBox);
        
        Scene scene= new Scene(root, 250, 85);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        return option;
    }
    private static void setOption(int val){
        option=val;
    }
}
