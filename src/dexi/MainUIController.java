package dexi;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;

public class MainUIController implements Initializable {
    @FXML Button copytn;
    @FXML TableView<TrackingData> table;
    @FXML TableView<QueryData> queryTable;
    @FXML ProgressBar progBar;
    @FXML Hyperlink repositoryLink;
    
	@Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		copytn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(new StringSelection(dexi.TrackingNumber), null);
			}
		});
		
		Tooltip repoTooltip=new Tooltip(); repoTooltip.setText("https://github.com/kingkingyyk/DEX-I-Auto-Tracking");
		repositoryLink.setTooltip(repoTooltip);
		
		repositoryLink.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try { Desktop.getDesktop().browse(new URI("https://github.com/kingkingyyk/DEX-I-Auto-Tracking"));
				} catch (Exception e) {}
			}
		});
	}
    
    
}
