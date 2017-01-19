package InfoFetcher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dexi.TrackingData;
import dexi.dexi;

public class DEXIFetcher extends InfoFetcher {
	private static DateTimeFormatter DEXIFormatter=DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

	public DEXIFetcher (String n) {
		super(n);
	}
	
	public String getType() {return "DEX-I";}
	
	public void fetchInfo () throws NoTrackingException, Exception {
		URL url=new URL("http://118.139.183.89/Podtrack/Details.aspx?ID="+this.getTrackingNumber());
		InputStream is=url.openStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String s;
		ArrayList<String> lines=new ArrayList<>();
		while ((s=br.readLine())!=null) {
			Document parsed=Jsoup.parse(s);
			if (parsed.hasText() && hasWord(parsed.text())) {
				lines.add(parsed.text());
			}
		}
		br.close();
		is.close();
		if (lines.size()>=4 && !lines.get(3).contains("No tracking information exists")) {
			int currIndex=10;
			if (lines.get(9).startsWith(": ")) currIndex+=4;
			while (currIndex<lines.size() && !lines.get(currIndex).contains("For Delivery") && !lines.get(currIndex).equals("Back")) {
				TrackingData td=new TrackingData();
				td.setEventTime(LocalDateTime.parse(lines.get(currIndex)+" "+lines.get(currIndex+1),DEXIFormatter));
				td.setLocation(lines.get(currIndex+2));
				td.setStatus(lines.get(currIndex+3));
				td.setSource(getType());
				currIndex+=4;
				
				if (!dexi.infoList.contains(td)) dexi.infoList.add(td);
			}
		} else throw new NoTrackingException("");
	}
	
	
}
