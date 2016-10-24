package dexi;

import java.awt.BorderLayout;
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

public class ui extends JFrame {
	private static final long serialVersionUID = 2477333158647514062L;
	private JPanel contentPane;
	public JLabel lblInfo;
	
	public ui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 395, 168);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setMinimumSize(new Dimension(365,168));
		setContentPane(contentPane);
		
		lblInfo = new JLabel("FETCHING DATA...");
		lblInfo.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblInfo, BorderLayout.CENTER);
		
		JButton btncopy = new JButton("Copy Tracking Number");
		btncopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String myString = dexi.TrackingNumber;
				StringSelection stringSelection = new StringSelection(myString);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		
		setLocationRelativeTo(null);
		contentPane.add(btncopy, BorderLayout.SOUTH);
	}

}
