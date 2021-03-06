package ParcelTracker;

import java.io.IOException;
import java.time.LocalDateTime;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class MainUI extends Application {
	private static MainUI current=null;
    private Stage primaryStage;
    private Pane rootLayout;
    private MainUIController ctrl;
    
    public static MainUI getCurrent() {
    	return MainUI.current;
    }
    
	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(ParcelTracker.APP_NAME+" - "+ParcelTracker.selectedProviders.get(0).getTrackingNumber());
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainUI.fxml"));
            ctrl=new MainUIController();
            loader.setController(ctrl);
            rootLayout = (Pane) loader.load();

            MainUI.current=this;
            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            	public void handle (WindowEvent we) {
            		System.exit(0);
            	}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
	public static void updateStatusx (String s) {
		Platform.runLater(new Runnable() {
			public void run () {
				//current.ctrl.status.setText(s);
			}
		});
	}
	
	public static void updateProgBar (int curr, int max) {
		Platform.runLater(new Runnable() {
			public void run () {
				current.ctrl.progBar.setProgress(((double)curr)/max);
			}
		});
	}
	

	@SuppressWarnings("unchecked")
	public static  void updateTable() {
		Platform.runLater(new Runnable() {
			public void run () {
				ObservableList<TrackingData> data=FXCollections.observableArrayList(ParcelTracker.infoList);
				((TableColumn<TrackingData,LocalDateTime>)(current.ctrl.table.getColumns().get(0))).setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDateTime>() {

					@Override
					public LocalDateTime fromString(String arg0) {
						return LocalDateTime.parse(arg0,TrackingData.DateFormatter);
					}

					@Override
					public String toString(LocalDateTime arg0) {
						return TrackingData.DateFormatter.format(arg0);
					}
					
				}));
				((TableColumn<TrackingData,LocalDateTime>)(current.ctrl.table.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<TrackingData,LocalDateTime>("eventTime"));
				((TableColumn<TrackingData,String>)(current.ctrl.table.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<TrackingData,String>("location"));
				((TableColumn<TrackingData,String>)(current.ctrl.table.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<TrackingData,String>("status"));
				((TableColumn<TrackingData,String>)(current.ctrl.table.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<TrackingData,String>("source"));
				current.ctrl.table.setItems(data);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public static  void updateQueryTable() {
		Platform.runLater(new Runnable() {
			public void run () {
				ObservableList<QueryData> data=FXCollections.observableArrayList(ParcelTracker.queryList);
				((TableColumn<QueryData,LocalDateTime>)(current.ctrl.queryTable.getColumns().get(0))).setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDateTime>() {

					@Override
					public LocalDateTime fromString(String arg0) {
						return LocalDateTime.parse(arg0,QueryData.DateFormatter);
					}

					@Override
					public String toString(LocalDateTime arg0) {
						return QueryData.DateFormatter.format(arg0);
					}
					
				}));
				((TableColumn<QueryData,LocalDateTime>)(current.ctrl.queryTable.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<QueryData,LocalDateTime>("updateTime"));
				((TableColumn<QueryData,String>)(current.ctrl.queryTable.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<QueryData,String>("name"));
				((TableColumn<QueryData,String>)(current.ctrl.queryTable.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<QueryData,String>("status"));
				current.ctrl.queryTable.setItems(data);
			}
		});
	}
	
}
