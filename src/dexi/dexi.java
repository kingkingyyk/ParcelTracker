package dexi;

import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import dexi.InfoFetcher.NoTrackingException;

public class dexi {
	
	private static SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/YYYY hh:mm aaa");
	public static String TrackingNumber="";
	public static boolean NotifyFlag=false;
	public static long FetchCount=0;
	public static LinkedList<TrackingData> infoList=new LinkedList<>();
	public static enum SourcePriority{DEXI,ABX};
	
	private static void setupGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}
	
	private static char [] timeUnitName={'h','m','s'};
	private static int [] timeUnit={24*60,60,1};
	
	private static String secondToStr (int second) {
		StringBuilder sb=new StringBuilder();
		for (int i=0;i<timeUnit.length;i++) {
			if (second/timeUnit[i]>0) {
				sb.append(second/timeUnit[i]);
				sb.append(timeUnitName[i]);
				sb.append(' ');
				second%=timeUnit[i];
			}
		}
		return sb.toString();
	}
	
	public static void main (String [] args) throws Exception {
		setupGUI();
		new inputTrackingNo();
		ui window=new ui();
		window.setLocationRelativeTo(null);
		Thread t=new Thread() {
			public void run () {
				int lastSize=0; TrackingData latestTD=null;
				while (true) {
					window.setTitle("UPDATE @ "+formatter.format(new Date()));
					
					String status="<html>";
					try {
						InfoFetcher.fetchDexiInfo(TrackingNumber);
						status+="DEX-I - OK";
					} catch (NoTrackingException e) { status+="DEX-I - No Record";
					} catch (Exception e) { status+="DEX-I - ERROR"; e.printStackTrace();}
					
					status+=" | ";
					try {
						InfoFetcher.fetchABXInfo(TrackingNumber);
						status+="ABX - OK";
					} catch (NoTrackingException e) { status+="ABX - No Record";
					} catch (Exception e) { status+="ABX - ERROR"; }
					status+="<br>Next update in ";
					
					if (infoList.size()>lastSize && !infoList.getFirst().equals(latestTD)) {
						Collections.sort(infoList);
						latestTD=infoList.getFirst();
						lastSize=infoList.size();
						
						TrackingData latest=infoList.get(0);
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "New Update from "+latest.source+" at "+latest.location+"!\nStatus : "+latest.status,"Tracking",JOptionPane.INFORMATION_MESSAGE);
						
						StringBuilder sb=new StringBuilder();
						sb.append("<html><table border=\"1\">");

						for (TrackingData td : infoList) {
							sb.append(td.toHTML());
						}
						sb.append("</table></html>");
						window.lblInfo.setText(sb.toString());
						window.lblStatus.setText(status+"</html>");
						
						try { Thread.sleep(500); } catch (InterruptedException e) {}
						window.pack();
					} else if (infoList.size()==0) {
						window.lblInfo.setText(":( No record found");
					}
					
					for (int i=300;i>=0;i--) {
						window.lblStatus.setText(status+secondToStr(i)+"...</html>");
						window.pack();
						try { Thread.sleep(1000); } catch (InterruptedException e) {}
					}
				}
			}
		};
		t.start();
		window.setVisible(true);
	}
}
