package dexi;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import InfoFetcher.InfoFetcher;

import javax.swing.ListSelectionModel;

public class inputTrackingNo extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	public String trackingNo="";
	private JTable table;
	
	public inputTrackingNo() {
		setModal(true);
		setTitle(dexi.APP_NAME);
		setResizable(false);
		setBounds(100, 100, 450, 295);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblInputTN = new JLabel("Enter your tracking number :");
		lblInputTN.setBounds(10, 11, 424, 14);
		getContentPane().add(lblInputTN);
		
		textField = new JTextField();
		textField.setBounds(10, 36, 424, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a tracking number!",dexi.APP_NAME,JOptionPane.WARNING_MESSAGE);
				} else if (getSelectedProviders().size()==0){
					JOptionPane.showMessageDialog(null, "Please select at least one company!",dexi.APP_NAME,JOptionPane.WARNING_MESSAGE);
				} else {
					trackingNo=textField.getText();
					dispose();
				}
			}
		});
		btnOK.setBounds(345, 232, 89, 23);
		getContentPane().add(btnOK);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 67, 424, 154);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setShowVerticalLines(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Company", "Query?"
			}
		) {
			private static final long serialVersionUID = -9194137673929140667L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Boolean.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			
		    public boolean isCellEditable(int row, int column) {
		    	return column==1;
		    }
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if (trackingNo.isEmpty()) {
					System.exit(0);
				}
			}
		});
		
		customInitialize();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
    private static boolean isNumeric (String s) {
    	for (char c : s.toCharArray()) if (!Character.isDigit(c)) return false;
    	return true;
    }
    
	private void customInitialize () {
		try {
			String clipboardText=(String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); 
			if (isNumeric(clipboardText)) textField.setText(clipboardText);
;
		} catch (Exception e) {
			e.printStackTrace();
		};
		
		for (InfoFetcher input : dexi.allProviders) ((DefaultTableModel)table.getModel()).addRow(new Object[] {input,false});
	}
	
	public ArrayList<InfoFetcher> getSelectedProviders() {
		ArrayList<InfoFetcher> list=new ArrayList<>();
		for (int i=0;i<table.getRowCount();i++) if ((boolean)table.getValueAt(i,1)) list.add((InfoFetcher)table.getValueAt(i,0));
		return list;
	}
	
	public String getTrackingNumber() {
		return this.trackingNo;
	}
}
