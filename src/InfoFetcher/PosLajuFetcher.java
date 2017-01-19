package InfoFetcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ParcelTracker.ParcelTracker;
import ParcelTracker.TrackingData;

public class PosLajuFetcher extends InfoFetcher {
	private static DateTimeFormatter DateFormatter=DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a");
	private static String [] tagsToRemove={"<tr>","</tr>","<td>","</td>"};
	
	public PosLajuFetcher(String n) {
		super(n);
	}

	@Override
	public String getType() {
		return "Pos Laju";
	}

	@Override
	public void fetchInfo() throws NoTrackingException, Exception {
		URL url=new URL("http://pos.com.my/postal-services/quick-access/?track-trace");
		HttpURLConnection.setFollowRedirects(true);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Referer","http://pos.com.my/postal-services/quick-access/?track-trace");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		conn.setRequestProperty("Accept-Language","en-US, en;q=0.5");
		conn.setDoOutput(true);
		DataOutputStream wr=new DataOutputStream(conn.getOutputStream());
		wr.writeBytes("trackingNo03="+this.getTrackingNumber()+"&hvtrackNoHeader03=&hvfromheader03=0");
		wr.flush();
		wr.close();
		
		InputStream is=conn.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String s;
		String line=null;
		while ((s=br.readLine())!=null) {
			if (s.contains("var strTD")) {
				line=s;
				break;
			}
		}
		br.close(); is.close();
		
		if (line!=null && !line.contains("No Record Found")) {
			String data=line.split("<tbody>")[1];
			for (String tag : tagsToRemove) data=data.replaceAll(tag,";");
			
			while (data.contains(";;")) data=data.replaceAll(";;",";");
			
			if (data.startsWith(";")) data=data.substring(1,data.length());
			if (data.endsWith(";")) data=data.substring(0,data.length()-1);
			
			String [] fields=data.split(";");
			for (int i=0;i<fields.length;i++) {
				while (fields[i].endsWith(" ") && fields[i].length()>0) fields[i]=fields[i].substring(0,fields[i].length()-1);
			}
			for (int i=0;i<fields.length;i+=3) {
				TrackingData td=new TrackingData();
				td.setEventTime(LocalDateTime.parse(fields[i],DateFormatter));
				td.setLocation(fields[i+2].replaceAll("Pos Laju ",""));
				td.setStatus(fields[i+1].replaceAll(" at",""));
				td.setSource(getType());

				if (!ParcelTracker.infoList.contains(td)) ParcelTracker.infoList.add(td);
			}
		} else throw new NoTrackingException("");
	}

}
