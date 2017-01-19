package ParcelTracker;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class DialogInputTrackingNumController implements Initializable {
    @FXML TextField trackingNo;
    @FXML TableView inputCompanies;
    @FXML Button btnOK;
    
    private static boolean isNumeric (String s) {
    	for (char c : s.toCharArray()) if (!Character.isDigit(c)) return false;
    	return true;
    }
    
	@Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		try {
			String clipboardText=(String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); 
			if (isNumeric(clipboardText)) trackingNo.setText(clipboardText);
		} catch (Exception e) {};
		
		btnOK.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					DialogInputTrackingNum.getCurrent().stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
