package dexi;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class inputTrackingNo extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField textField;

	public inputTrackingNo() {
		setModal(true);
		setTitle(dexi.APP_NAME);
		setResizable(false);
		setBounds(100, 100, 450, 127);
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
				if (!textField.getText().isEmpty()) {
					dexi.TrackingNumber=textField.getText();
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Please enter a tracking number!",dexi.APP_NAME,JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnOK.setBounds(345, 67, 89, 23);
		getContentPane().add(btnOK);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if (dexi.TrackingNumber.isEmpty()) {
					System.exit(0);
				}
			}
		});
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
