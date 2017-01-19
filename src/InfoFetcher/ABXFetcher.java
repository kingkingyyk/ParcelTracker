package InfoFetcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import dexi.TrackingData;
import dexi.dexi;

public class ABXFetcher extends InfoFetcher {

	private static DateTimeFormatter ABXFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public ABXFetcher (String n) {
		super(n);
	}
	
	public String getType() {return "ABX";}
	
	public void fetchInfo () throws NoTrackingException, Exception {
		URL url=new URL("http://www.abxexpress.com.my/track.asp?vsearch=True");
		HttpURLConnection.setFollowRedirects(true);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Referer","http://www.abxexpress.com.my/");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		conn.setRequestProperty("Accept-Language","en-US, en;q=0.5");
		conn.setDoOutput(true);
		DataOutputStream wr=new DataOutputStream(conn.getOutputStream());
		wr.writeBytes("tairbillno="+this.getTrackingNumber());
		wr.flush();
		wr.close();
		
		InputStream is=conn.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		ArrayList<String> lines=new ArrayList<>();
		String s;
		while ((s=br.readLine())!=null) {
			Document parsed=Jsoup.parse(s);
			if (parsed.hasText() && hasWord(parsed.text())) {
				lines.add(parsed.text());
			}
		}
		br.close(); is.close();

		if (lines.size()>=6 && !lines.get(5).equals("No records available")) {
			int currIndex=lines.indexOf("Activity")+1;
			while (currIndex<lines.size() && lines.get(currIndex).charAt(0)!='*') {
				TrackingData td=new TrackingData();
				td.setLocation(lines.get(currIndex));
				td.setEventTime(LocalDateTime.parse(lines.get(currIndex+1),ABXFormatter));
				td.setStatus(lines.get(currIndex+2));
				td.setSource(getType());
				currIndex+=3;
				if (!dexi.infoList.contains(td)) dexi.infoList.add(td);
			}
		} else throw new NoTrackingException("");
	}
	
}
