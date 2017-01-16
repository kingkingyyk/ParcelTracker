package dexi;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;

public class MainUIController implements Initializable {
    @FXML Label status;
    @FXML Button copytn;
    @FXML TableView<TrackingData> table;
    @FXML ProgressBar progBar;
    
	@Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		copytn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(new StringSelection(dexi.TrackingNumber), null);
			}
		});
	}
    
    
}
