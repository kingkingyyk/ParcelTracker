package ParcelTracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class QueryData implements Comparable<QueryData> {
	public static DateTimeFormatter DateFormatter=DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
	private ObjectProperty<LocalDateTime> updateTime;
	private SimpleStringProperty name;
	private SimpleStringProperty status;

	public QueryData () {
		updateTime=new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
		name=new SimpleStringProperty("Logistics Company");
		status=new SimpleStringProperty("Status");
	}
	
	public LocalDateTime getUpdateTime () {return updateTime.get();}
	public void setUpdateTime (LocalDateTime dt) {updateTime.set(dt);}
	
	public String getName () {return name.get();}
	public void setName (String l) {name.set(l);}
	
	public String getStatus () {return status.get();}
	public void setStatus (String l) {status.set(l);}
	
	@Override
	public boolean equals (Object qd) {
		if (qd==null) return false;
		return compareTo((QueryData)qd)==0;
	}
	
	@Override
	public int compareTo(QueryData qd) {
		if (ParcelTracker.queryStatusPriority.get(getStatus())!=ParcelTracker.queryStatusPriority.get(qd.getStatus())) return -getStatus().compareTo(qd.getStatus());
		else if (!getUpdateTime().equals(qd.getUpdateTime())) return -getUpdateTime().compareTo(qd.getUpdateTime());
		else if (!getName().equals(qd.getName())) return getName().compareTo(qd.getName());
		return 0;
	}
}
