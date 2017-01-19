package InfoFetcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ParcelTracker.TrackingData;
import ParcelTracker.ParcelTracker;

public class GDEXFetcher extends InfoFetcher {
	private static DateTimeFormatter GDEXFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public GDEXFetcher (String n) {
		super(n);
	}
	
	public String getType() {return "GDEX";}
	
	public void fetchInfo () throws NoTrackingException, Exception {
		URL url=new URL("http://web2.gdexpress.com/official/iframe/etracking2.php");
		HttpURLConnection.setFollowRedirects(true);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Referer","http://web2.gdexpress.com/official/iframe/etracking2.php");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		conn.setRequestProperty("Accept-Language","en-US, en;q=0.5");
		conn.setDoOutput(true);
		DataOutputStream wr=new DataOutputStream(conn.getOutputStream());
		wr.writeBytes("capture="+this.getTrackingNumber()+"&redoc_gdex=cnGdex&Submit=Track");
		wr.flush();
		wr.close();
		
		InputStream is=conn.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String s;
		String targetLine=null;
		while ((s=br.readLine())!=null) {
			if (s.contains("<td>"+this.getTrackingNumber())) {
				Document parsed=Jsoup.parse(s);
				if (parsed.hasText() && hasWord(parsed.text())) {
					targetLine=s;
					break;
				}
			}
		}
		br.close(); is.close();
		if (targetLine!=null) {
			String [] data=targetLine.split("<td>");
			for (int i=2;i<data.length;i+=4) {
				TrackingData td=new TrackingData();
				td.setEventTime(LocalDateTime.parse(Jsoup.parse(data[i]).text(),GDEXFormatter));
				td.setStatus(Jsoup.parse(data[i+1]).text());
				td.setLocation(Jsoup.parse(data[i+2]).text());
				td.setSource(getType());
				if (!ParcelTracker.infoList.contains(td)) ParcelTracker.infoList.add(td);
			}
		} else throw new NoTrackingException("");
	}
}
