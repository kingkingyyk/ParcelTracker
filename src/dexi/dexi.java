package dexi;

import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import dexi.InfoFetcher.NoTrackingException;

public class dexi {
	
	public static final String APP_NAME="DEX-I/ABX/GDEX Tracking";
	public static String TrackingNumber="";
	public static boolean NotifyFlag=false;
	public static long FetchCount=0;
	public static LinkedList<TrackingData> infoList=new LinkedList<>();
	public static LinkedList<QueryData> queryList=new LinkedList<>();
	public static HashMap<SourcePriority,QueryData> queryDataMap=new HashMap<>();
	public static HashMap<String,Integer> queryStatusPriority=new HashMap<>();
	public static enum SourcePriority{DEXI,ABX,GDEX};
	
	private static void setupGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}
	
	private static void setupQueryData() {
		LocalDateTime now=LocalDateTime.now();
		
		for (SourcePriority p : SourcePriority.values()) {
			QueryData qd=new QueryData();
			qd.setName(p.name());
			qd.setStatus("Updating");
			qd.setUpdateTime(now);
			queryList.add(qd);
			queryDataMap.put(p,qd);
		}
		
		queryStatusPriority.put("OK",0);
		queryStatusPriority.put("No Record",1);
		queryStatusPriority.put("Error",2);
	}
	
	public static void main (String [] args) throws Exception {
		setupGUI();
		new inputTrackingNo();
		setupQueryData();
		
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
					QueryData qd;
					
					//DEX-I==================
					qd=queryDataMap.get(SourcePriority.DEXI);
					qd.setUpdateTime(LocalDateTime.now());
					qd.setStatus("Updating...");
					
					try {
						InfoFetcher.fetchDexiInfo(TrackingNumber);
						qd.setStatus("OK");
					} catch (NoTrackingException e) { qd.setStatus("No Record");
					} catch (Exception e) { qd.setStatus("Error"); e.printStackTrace();}
					qd.setUpdateTime(LocalDateTime.now());
					
					//ABX======================
					qd=queryDataMap.get(SourcePriority.ABX);
					qd.setUpdateTime(LocalDateTime.now());
					qd.setStatus("Updating...");
					try {
						InfoFetcher.fetchABXInfo(TrackingNumber);
						qd.setStatus("OK");
					} catch (NoTrackingException e) { qd.setStatus("No Record");
					} catch (Exception e) { qd.setStatus("Error"); e.printStackTrace();}
					qd.setUpdateTime(LocalDateTime.now());
					
					//GDEX====================
					qd=queryDataMap.get(SourcePriority.GDEX);
					qd.setUpdateTime(LocalDateTime.now());
					qd.setStatus("Updating...");
					try {
						InfoFetcher.fetchGDEXInfo(TrackingNumber);
						qd.setStatus("OK");
					} catch (NoTrackingException e) { qd.setStatus("No Record");
					} catch (Exception e) { qd.setStatus("Error"); e.printStackTrace();}
					qd.setUpdateTime(LocalDateTime.now());
					
					Collections.sort(queryList);
					MainUI.updateQueryTable();
					
					if (infoList.size()>lastSize) {
						Collections.sort(infoList);
						
						if (!infoList.getFirst().equals(latestTD)) {
							latestTD=infoList.getFirst();
							lastSize=infoList.size();
							
							TrackingData latest=infoList.get(0);
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "New Update from "+latest.getSource()+" at "+latest.getLocation()+"!\nStatus : "+latest.getStatus(),dexi.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
						}
						
						MainUI.updateTable();
						try { Thread.sleep(500); } catch (InterruptedException e) {}
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
