package dexi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TrackingData implements Comparable<TrackingData> {
	public static DateTimeFormatter DateFormatter=DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
	private ObjectProperty<LocalDateTime> eventTime;
	private SimpleStringProperty location;
	private SimpleStringProperty status;
	private SimpleStringProperty source;
	
	public TrackingData () {
		eventTime=new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
		location=new SimpleStringProperty("Location");
		status=new SimpleStringProperty("Status");
		source=new SimpleStringProperty("Source");
	}
	
	public LocalDateTime getEventTime () {return eventTime.get();}
	public void setEventTime (LocalDateTime dt) {eventTime.set(dt);}
	
	public String getLocation () {return location.get();}
	public void setLocation (String l) {location.set(l);}
	
	public String getStatus() {return status.get();}
	public void setStatus (String s) {status.set(s);}
	
	public String getSource() {return source.get();}
	public void setSource (String s) {source.set(s);}
	
	public String toHTML () {
		return "<tr><td>"+DateFormatter.format(getEventTime())+"</td><td>"+location+"</td><td>"+status+"</td><td>"+source+"</td>";
	}
	
	@Override
	public boolean equals (Object td) {
		if (td==null) return false;
		return compareTo((TrackingData)td)==0;
	}
	
	@Override
	public int compareTo(TrackingData td) {
		if (!getEventTime().equals(td.getEventTime())) return -getEventTime().compareTo(td.getEventTime());
		else if (!source.equals(td.source)) return -dexi.SourcePriority.valueOf(getSource()).ordinal()-dexi.SourcePriority.valueOf(td.getSource()).ordinal();
		return 0;
	}
	
	public String [] toStringAry() {
		return new String [] {DateFormatter.format(getEventTime()),getLocation(),getStatus(),getSource()};
	}
}
