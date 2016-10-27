package dexi;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class ui2 extends Shell {
	public static Table table;
	public static Label lblStatus;
	public static ProgressBar progressBar;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		Thread t=new Thread() {
			public void run () {
				try {
					Display display = Display.getDefault();
					ui2 shell = new ui2(display);
					
				    Monitor primary = display.getPrimaryMonitor();
				    Rectangle bounds = primary.getBounds();
				    Rectangle rect = shell.getBounds();
				    
				    int x = bounds.x + (bounds.width - rect.width) / 2;
				    int y = bounds.y + (bounds.height - rect.height) / 2;
				    
				    shell.setLocation(x, y);
				    
					shell.open();
					shell.layout();
					while (!shell.isDisposed()) {
						if (!display.readAndDispatch()) {
							display.sleep();
						}
					}
					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ui2(Display display) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		setBackgroundImage(new Image(null,dexi.class.getClassLoader().getResourceAsStream("bg.jpg")));
		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
		progressBar.setBounds(0, 0, 544, 7);
		
		lblStatus = new Label(this, SWT.NONE);
		lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblStatus.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
		lblStatus.setBounds(10, 16, 219, 15);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 41, 544, 290);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnTime = new TableColumn(table, SWT.NONE);
		tblclmnTime.setWidth(104);
		tblclmnTime.setText("Time");
		
		TableColumn tblclmnLocation = new TableColumn(table, SWT.NONE);
		tblclmnLocation.setWidth(107);
		tblclmnLocation.setText("Location");
		
		TableColumn tblclmnEvent = new TableColumn(table, SWT.NONE);
		tblclmnEvent.setWidth(240);
		tblclmnEvent.setText("Event");
		
		TableColumn tblclmnSource = new TableColumn(table, SWT.NONE);
		tblclmnSource.setWidth(65);
		tblclmnSource.setText("Source");
		
		Button btnCopy = new Button(this, SWT.NONE);
		btnCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(new StringSelection(dexi.TrackingNumber), null);
			}
		});
		btnCopy.setBounds(398, 10, 136, 25);
		btnCopy.setText("Copy Tracking No.");
		createContents();
	}

	protected void createContents() {
		setText("DEX-I & ABX Tracking");
		setSize(550, 360);

	}

	@Override
	protected void checkSubclass() {}
	
	public static void updateStatus (String s) {
		Display.getDefault().syncExec(new Runnable() {
			public void run () {
				lblStatus.setText(s);
			}
		});
	}
	
	public static void setTitle (String s) {
		Display.getDefault().syncExec(new Runnable() {
			public void run () {
				Display.getCurrent().getActiveShell().setText(s);
			}
		});
	}
	
	public static void updateProgBar (int curr, int max) {
		Display.getDefault().syncExec(new Runnable() {
			public void run () {
				progressBar.setMaximum(max);
				progressBar.setSelection(curr);
			}
		});
	}
	
	public static  void updateTable() {
		Display.getDefault().syncExec(new Runnable() {
			public void run () {
				table.clearAll();
				for (TrackingData td : dexi.infoList) {
					TableItem item=new TableItem(table,SWT.NONE);
					item.setText(td.toStringAry());
				}
			}
		});
	}
}
