package dexi;

import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import InfoFetcher.ABXFetcher;
import InfoFetcher.DEXIFetcher;
import InfoFetcher.GDEXFetcher;
import InfoFetcher.InfoFetcher;
import InfoFetcher.InfoFetcher.NoTrackingException;

public class dexi {
	
	public static final String APP_NAME="Parcel Tracking";
	public static boolean NotifyFlag=false;
	public static long FetchCount=0;
	public static LinkedList<TrackingData> infoList=new LinkedList<>();
	public static LinkedList<QueryData> queryList=new LinkedList<>();
	public static HashMap<InfoFetcher,QueryData> queryDataMap=new HashMap<>();
	public static HashMap<String,Integer> queryStatusPriority=new HashMap<>();
	
	public static ArrayList<InfoFetcher> allProviders=new ArrayList<>();
	public static ArrayList<InfoFetcher> selectedProviders=new ArrayList<>();
	
	private static void setupGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}
	
	private static void setupProviders() {
		allProviders.add(new DEXIFetcher(""));
		allProviders.add(new ABXFetcher(""));
		allProviders.add(new GDEXFetcher(""));
	}
	
	private static void setupQueryData(String tn) {
		LocalDateTime now=LocalDateTime.now();
		
		for (InfoFetcher p : selectedProviders) {
			QueryData qd=new QueryData();
			qd.setName(p.getType());
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
		setupProviders();
		inputTrackingNo diag=new inputTrackingNo();
		String tn=diag.getTrackingNumber();
		if (tn.equals("")) return;
		
		selectedProviders.addAll(diag.getSelectedProviders());
		for (InfoFetcher input : selectedProviders) input.setTrackingNumber(tn);
		
		setupQueryData(tn);
		
		Thread mainUIT=new Thread() {
			public void run () {
				MainUI.launch(MainUI.class);
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
					
					for (InfoFetcher p : selectedProviders) {
						qd=queryDataMap.get(p);
						qd.setUpdateTime(LocalDateTime.now());
						qd.setStatus("Updating...");
						try {
							p.fetchInfo();
							qd.setStatus("OK");
						} catch (NoTrackingException e) { qd.setStatus("No Record");
						} catch (Exception e) { qd.setStatus("Error"); e.printStackTrace();}
						qd.setUpdateTime(LocalDateTime.now());
					}
					
					Collections.sort(queryList);
					MainUI.updateQueryTable();
					
					if (infoList.size()>lastSize) {
						Collections.sort(infoList);
						
						if (!infoList.getFirst().equals(latestTD)) {
							latestTD=infoList.getFirst();
							lastSize=infoList.size();
							
							TrackingData latest=infoList.get(0);
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "New update from "+latest.getSource()+" at "+latest.getLocation()+"!\n<html><b>"+latest.getStatus()+"</b></html>",dexi.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
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
