package com.host.blocking;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class About extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static About instance = null;

	/**
	 * Create the frame.
	 */
	public About() {

		StringBuilder sb = new StringBuilder();
		sb.append("This product is liscenced to : ");
		setTitle("About");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(20, 64, 414, 2);
		contentPane.add(separator);

		JLabel lblNewLabel = new JLabel("");
		Image loginIconRegistration = new ImageIcon(this.getClass().getResource("/About.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(loginIconRegistration));
		lblNewLabel.setBounds(37, 11, 387, 42);
		contentPane.add(lblNewLabel);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnOk.setBounds(335, 227, 89, 23);
		contentPane.add(btnOk);

		Image AboutCalculatorIcon = new ImageIcon(this.getClass().getResource("/Categories-applications-internet-icon.png"))
				.getImage();
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(37, 77, 49, 72);
		lblNewLabel_1.setIcon(new ImageIcon(AboutCalculatorIcon));
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel(sb.toString() + " " + System.getProperty("user.name"));
		lblNewLabel_2.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_2.setBounds(107, 86, 317, 137);
		contentPane.add(lblNewLabel_2);
	}

	public static About getInstance() {
		if (instance == null) {
			instance = new About();
		}
		return instance;
	}
}
