package dexi;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class dexi {
	
	private static SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/YYYY hh:mm aaa");
	public static String TrackingNumber="";
	public static boolean NotifyFlag=false;
	public static long FetchCount=0;
	
	private static String fetchInfo () {
		FetchCount++;
		try {
			URL url=new URL("http://118.139.183.89/Podtrack/Details.aspx?ID="+TrackingNumber);
			InputStream is=url.openStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String s;
			ArrayList<String> lines=new ArrayList<>();
			while ((s=br.readLine())!=null) {
				lines.add(s);
			}
			int startIndex=-1;
			for (int i=0;i<lines.size() && startIndex==-1;i++) {
				if (lines.get(i).contains("<span id=\"GridView1_Label1_0\">")) {
					startIndex=i-1;
				}
			}
			StringBuilder sb=new StringBuilder();
			if (startIndex==-1) {
				sb.append("NO INFORMATION FOUND! PLEASE CHECK YOUR TRACKING NUMBER.");
			} else {
				sb.append("<table><tr>");
	
				for (;!lines.get(startIndex).contains("</table>");startIndex++) {
					sb.append(lines.get(startIndex));
				}
				sb.append("</table>");
			}
			br.close();
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		};
		return "CONNECTION FAILED!";
	}
	
	private static void setupGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}
	
	public static void main (String [] args) throws Exception {
		setupGUI();
		new inputTrackingNo();
		ui window=new ui();
		Thread t=new Thread() {
			public void run () {
				while (true) {
					window.setTitle("UPDATE @ "+formatter.format(new Date()));
					String s="<html>"+fetchInfo()+"</html>";
					if (!window.lblInfo.getText().equals(s) && !s.equals("<html>CONNECTION FAILED!</html>")) {
						window.lblInfo.setText(s);
						window.pack();
						if (FetchCount>1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "New Status!","Tracking",JOptionPane.INFORMATION_MESSAGE);
						} else {
							window.setLocationRelativeTo(null);
						}
					}
					try {
						Thread.sleep(300*1000);
					} catch (InterruptedException e) {}
				}
			}
		};
		t.start();
		window.setVisible(true);
	}
}
