package dexi;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class ui extends JFrame {
	private static final long serialVersionUID = 2477333158647514062L;
	private JPanel contentPane;
	public JLabel lblInfo;
	public JLabel lblStatus;
	private JButton btnCp;

	public ui() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 395, 202);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setMinimumSize(new Dimension(365,168));
		setContentPane(contentPane);
		
		lblInfo = new JLabel("FETCHING DATA...");
		lblInfo.setVerticalAlignment(SwingConstants.TOP);
		
		setLocationRelativeTo(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(lblInfo);
		
		lblStatus = new JLabel("");
		contentPane.add(lblStatus, BorderLayout.NORTH);
		
		btnCp = new JButton("Copy Tracking Number");
		btnCp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String myString = dexi.TrackingNumber;
				StringSelection stringSelection = new StringSelection(myString);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		contentPane.add(btnCp, BorderLayout.SOUTH);
	}
}
