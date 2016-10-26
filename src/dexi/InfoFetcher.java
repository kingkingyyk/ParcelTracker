package dexi;

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

public class InfoFetcher {
	public static class NoTrackingException extends Exception {
		private static final long serialVersionUID = -5412427050254182301L;

		public NoTrackingException (String msg) {super(msg);}
	}
	
	private static DateTimeFormatter DEXIFormatter=DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
	
	public static boolean hasWord(String s) {
		boolean contains=false;
		for (char c : s.toCharArray()) contains|=(Character.isAlphabetic(c) || Character.isDigit(c));
		return contains;
	}
	
	public static void fetchDexiInfo (String number) throws NoTrackingException, Exception {
		URL url=new URL("http://118.139.183.89/Podtrack/Details.aspx?ID="+number);
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
			while (currIndex<lines.size() && !lines.get(currIndex).contains("For Delivery") && !lines.get(currIndex).equals("Back")) {
				TrackingData td=new TrackingData();
				td.eventTime=LocalDateTime.parse(lines.get(currIndex)+" "+lines.get(currIndex+1),DEXIFormatter);
				td.location=lines.get(currIndex+2);
				td.status=lines.get(currIndex+3);
				td.source="DEXI";
				currIndex+=4;
				
				if (!dexi.infoList.contains(td)) dexi.infoList.add(td);
			}
		} else throw new NoTrackingException("");
	}
	
	private static DateTimeFormatter ABXFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public static void fetchABXInfo (String number) throws NoTrackingException, Exception {
		URL url=new URL("http://www.abxexpress.com.my/track.asp?vsearch=True");
		HttpURLConnection.setFollowRedirects(true);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Referer","http://www.abxexpress.com.my/");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
		conn.setRequestProperty("Accept-Language","en-US, en;q=0.5");
		conn.setDoOutput(true);
		DataOutputStream wr=new DataOutputStream(conn.getOutputStream());
		wr.writeBytes("tairbillno="+number);
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
			int currIndex=12;
			while (currIndex<lines.size() && lines.get(currIndex).charAt(0)!='*') {
				TrackingData td=new TrackingData();
				td.location=lines.get(currIndex);
				td.eventTime=LocalDateTime.parse(lines.get(currIndex+1),ABXFormatter);
				td.status=lines.get(currIndex+2);
				td.source="ABX";
				currIndex+=3;
				if (!dexi.infoList.contains(td)) dexi.infoList.add(td);
			}
		} else throw new NoTrackingException("");
	}
}
