package dexi;

import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
	
	public static void main (String [] args) throws Exception {
		setupGUI();
		new inputTrackingNo();
		ui2.main(null);
		Thread t=new Thread() {
			public void run () {
				int lastSize=0; TrackingData latestTD=null;
				while (true) {
					ui2.updateStatus(formatter.format(new Date())+" | Updating...");
					
					String status=formatter.format(new Date())+" | ";
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
					ui2.updateStatus(status);
					
					if (infoList.size()>lastSize) {
						Collections.sort(infoList);
						
						if (!infoList.getFirst().equals(latestTD)) {
							latestTD=infoList.getFirst();
							lastSize=infoList.size();
							
							TrackingData latest=infoList.get(0);
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "New Update from "+latest.source+" at "+latest.location+"!\nStatus : "+latest.status,"Tracking",JOptionPane.INFORMATION_MESSAGE);
						}
						
						ui2.updateTable();
						try { Thread.sleep(500); } catch (InterruptedException e) {}
					} else if (infoList.size()==0) {
						ui2.updateStatus(":( No record found");
					}
					
					int max=0;
					LocalDateTime dt=LocalDateTime.now();
					if (dt.getHour()>=7 && dt.getHour()<=19) max=5*60;
					else max=10*60;
					for (int i=1;i<=max;i++) {
						ui2.updateProgBar(i,max);
						try { Thread.sleep(1000); } catch (InterruptedException e) {}
					}
				}
			}
		};
		t.start();
	}
}
