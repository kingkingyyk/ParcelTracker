package dexi;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DialogInputTrackingNum extends Application {
	private static DialogInputTrackingNum current=null;
    private Stage primaryStage;
    private Pane rootLayout;
    private boolean noExit;
    private DialogInputTrackingNumController ctrl;
    
    public static DialogInputTrackingNum getCurrent() {
    	return DialogInputTrackingNum.current;
    }
    
	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(dexi.APP_NAME);

        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DialogInputTrackingNum.fxml"));
            ctrl=new DialogInputTrackingNumController();
            loader.setController(ctrl);
            rootLayout = (Pane) loader.load();

            DialogInputTrackingNum.current=this;
            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            	public void handle (WindowEvent we) {
            		if (!noExit) System.exit(0);
            	}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void stop () {
		noExit=true;
		primaryStage.hide();
	}
	
	public String getTrackingNumber() {
		return this.ctrl.trackingNo.getText();
	}
	
	public static void drawTable() {
		
	}

	
}
