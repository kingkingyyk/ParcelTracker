package dexi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrackingData implements Comparable<TrackingData> {
	private static DateTimeFormatter DateFormatter=DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
	public LocalDateTime eventTime;
	public String location;
	public String status;
	public String source;
	
	public String toHTML () {
		return "<tr><td>"+DateFormatter.format(eventTime)+"</td><td>"+location+"</td><td>"+status+"</td><td>"+source+"</td>";
	}
	
	@Override
	public boolean equals (Object td) {
		if (td==null) return false;
		return compareTo((TrackingData)td)==0;
	}
	
	@Override
	public int compareTo(TrackingData td) {
		if (!eventTime.equals(td.eventTime)) return -eventTime.compareTo(td.eventTime);
		else if (!source.equals(td.source)) return -dexi.SourcePriority.valueOf(source).ordinal()-dexi.SourcePriority.valueOf(td.source).ordinal();
		return 0;
	}
}
