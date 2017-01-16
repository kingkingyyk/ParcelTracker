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
		if (dexi.TrackingNumber.equals("")) return;
		Thread mainUIT=new Thread() {
			public void run () {
				MainUI.main(null);
			}
		};
		mainUIT.start();
		Thread t=new Thread() {
			public void run () {
				int lastSize=0; TrackingData latestTD=null;
				while (MainUI.getCurrent()==null) {
					try { Thread.sleep(100); } catch (InterruptedException e) {}
				}
				while (true) {
					MainUI.updateStatus(formatter.format(new Date())+" | Updating...");
					
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
					MainUI.updateStatus(status);
					
					if (infoList.size()>lastSize) {
						Collections.sort(infoList);
						
						if (!infoList.getFirst().equals(latestTD)) {
							latestTD=infoList.getFirst();
							lastSize=infoList.size();
							
							TrackingData latest=infoList.get(0);
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "New Update from "+latest.getSource()+" at "+latest.getLocation()+"!\nStatus : "+latest.getStatus(),"Tracking",JOptionPane.INFORMATION_MESSAGE);
						}
						
						MainUI.updateTable();
						try { Thread.sleep(500); } catch (InterruptedException e) {}
					} else if (infoList.size()==0) {
						MainUI.updateStatus(":( No record found");
					}
					
					int max=0;
					LocalDateTime dt=LocalDateTime.now();
					if (dt.getHour()>=7 && dt.getHour()<=19) max=5*60;
					else max=10*60;
					for (int i=1;i<=max;i++) {
						MainUI.updateProgBar(i,max);
						try { Thread.sleep(1000); } catch (InterruptedException e) {}
					}
				}
			}
		};
		t.start();
	}
}
