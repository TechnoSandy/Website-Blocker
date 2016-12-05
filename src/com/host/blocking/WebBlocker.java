package com.host.blocking;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class WebBlocker {
	// private static final ResourceBundle BUNDLE =
	// ResourceBundle.getBundle("com.host.blocking.messages"); //$NON-NLS-1$
	static Locale locale = new Locale("en", "US");
	private static ResourceBundle BUNDLE;
	// To verify the domain name validity of text field
	String regex = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";

	public static Locale getLocale() {
		return locale;
	}

	public static void setLocale(Locale locale) {
		WebBlocker.locale = locale;
	}

	private JFrame frame;
	LinkedHashSet<String> line;
	public static final Map<String, String> HOSTS_PATHS = new HashMap<String, String>(20);

	static {
		HOSTS_PATHS.put("AIX", "unknown");
		HOSTS_PATHS.put("Digital Unix", "/etc/hosts");
		HOSTS_PATHS.put("FreeBSD", "/etc/hosts");
		HOSTS_PATHS.put("HP UX", "/etc/hosts");
		HOSTS_PATHS.put("Irix", "unknown");
		HOSTS_PATHS.put("Linux", "/etc/hosts");
		HOSTS_PATHS.put("Mac OS", "unknown");
		HOSTS_PATHS.put("Mac OS X", "/etc/hosts");
		HOSTS_PATHS.put("MPE/iX", "unknown");
		HOSTS_PATHS.put("Netware 4.11", "unknown");
		HOSTS_PATHS.put("OS/2", "unknown");
		HOSTS_PATHS.put("Solaris", "/etc/hosts");
		HOSTS_PATHS.put("Windows 2000", System.getenv("windir") + "\\system32\\drivers\\etc\\hosts");
		HOSTS_PATHS.put("Windows 7", System.getenv("windir") + "\\system32\\drivers\\etc\\hosts");
		HOSTS_PATHS.put("Windows 8.1", System.getenv("windir") + "\\system32\\drivers\\etc\\hosts");
		HOSTS_PATHS.put("Windows 95", System.getenv("windir") + "\\hosts");
		HOSTS_PATHS.put("Windows 98", System.getenv("windir") + "\\hosts");
		HOSTS_PATHS.put("Windows NT", System.getenv("windir") + "\\system32\\drivers\\etc\\hosts");
		HOSTS_PATHS.put("Windows Vista", System.getenv("windir") + "\\system32\\drivers\\etc\\hosts");
		HOSTS_PATHS.put("Windows XP", System.getenv("windir") + "\\system32\\drivers\\etc\\hosts");
	}

	private File hosts = null;
	private String hostsPath = "";

	/**
	 * Launch the application private JTextField textField;
	 * 
	 * /** Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebBlocker window = new WebBlocker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public WebBlocker() throws Exception {
		BUNDLE = ResourceBundle.getBundle("com.host.blocking.messages", locale); //$NON-NLS-1$
		String osName = System.getProperty("os.name");
		hostsPath = HOSTS_PATHS.get(osName);

		if (hostsPath.equals("unknown") || hostsPath.equals("") || hostsPath == null) {
			throw new Exception("OS is unsupported. If you would like to set the"
					+ "hosts path manually, use Hosts.setHostsPath(String)");
		}

		hosts = new File(hostsPath);

		if (!hosts.exists()) {
			throw new Exception("Unable to find hosts file at specified path."
					+ " Please set hostsPath manually with .setHostsPath()");
		}
		try {
			initialize();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		final Image frameicon = new ImageIcon(this.getClass().getResource("/Categories-applications-internet-icon.png"))
				.getImage();
		frame = new JFrame();
		// frame.getContentPane().setBackground(new Color(224, 255, 255));
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setBounds(100, 100, 654, 190);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setIconImage(frameicon);

		final JTextField textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textField.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				textField.setText(BUNDLE.getString("WebBlocker.textField.text"));
			}
		});
		textField.setToolTipText("Example : www.xyz.com");
		textField.setFont(new Font("Dialog", Font.BOLD, 20));
		textField.setForeground(Color.GRAY);
		textField.setText(BUNDLE.getString("WebBlocker.textField.text")); //$NON-NLS-1$
		textField.setBounds(246, 32, 382, 53);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		// System.out.println(textField.getFont().canDisplay('\u0915'));// to
		// check if the font support everthing in resource bundle properly 
		JButton BtnAddSite = new JButton(BUNDLE.getString("WebBlocker.BtnAddSite.text"));
		BtnAddSite.setToolTipText(BUNDLE.getString("WebBlocker.BtnAddSite.toolTipText"));
		BtnAddSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!textField.getText().matches(regex)) {
					JOptionPane.showMessageDialog(null, BUNDLE.getString("WebBlocker.textField.Validationtext"));
					// JOptionPane.showMessageDialog(null, "Domain Name not
					// correct type like www.xyz.com");
				} else {
					Scanner scan;
					try {
						scan = new Scanner(hosts);
						line = new LinkedHashSet<String>();
						while (scan.hasNextLine()) {
							line.add(scan.nextLine());
						}
						FileWriter fw = new FileWriter(hosts, true);
						BufferedWriter bfw = new BufferedWriter(fw);
						if (!line.contains("127.0.0.1 " + textField.getText())) {
							bfw.write("127.0.0.1 " + textField.getText() + "\n");
							bfw.newLine();
							bfw.flush();
							JOptionPane.showMessageDialog(null, "Added to the list");
						} else {
							JOptionPane.showMessageDialog(null, "Site Already in list ");
						}
						fw.close();
						bfw.close();
						scan.close();

					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			}
		});
		BtnAddSite.setFont(new Font("Dialog", Font.BOLD, 20));
		BtnAddSite.setBounds(10, 96, 119, 46);
		frame.getContentPane().add(BtnAddSite);

		JButton btnRemove = new JButton(BUNDLE.getString("WebBlocker.btnRemove.text")); //$NON-NLS-1$
		btnRemove.setToolTipText(BUNDLE.getString("WebBlocker.btnRemove.toolTipText")); //$NON-NLS-1$
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Scanner scan;
				try {
					scan = new Scanner(hosts);
					line = new LinkedHashSet<String>();
					while (scan.hasNextLine()) {
						line.add(scan.nextLine());
					}
					if (line.contains("127.0.0.1 " + textField.getText())) {
						// JOptionPane.showMessageDialog(null, "found " +
						// textField.getText());
						// JOptionPane.showMessageDialog(null,
						// BUNDLE.getString("WebBlocker.msgDialog.Found") +
						// textField.getText());
						// int flag = JOptionPane.showConfirmDialog(null,
						// "Are You sure u want to remove the " +
						// textField.getText() + " from the list ?");
						int flag = JOptionPane.showConfirmDialog(null,
								BUNDLE.getString("WebBlocker.msgDialog.AreUSure1") + textField.getText()
										+ BUNDLE.getString("WebBlocker.msgDialog.AreUSure2"));
						if (flag == JOptionPane.YES_OPTION) {
							line.remove("127.0.0.1 " + textField.getText());
							try {
								FileWriter fw = new FileWriter(hosts);
								BufferedWriter bfw = new BufferedWriter(fw);
								for (Iterator<String> iterator = line.iterator(); iterator.hasNext();) {
									String string = (String) iterator.next();
									bfw.write(string);
									bfw.newLine();
									bfw.flush();
								}
								JOptionPane.showMessageDialog(null, BUNDLE.getString("WebBlocker.msgDialog.Removed"));
								// JOptionPane.showMessageDialog(null, "Removed
								// from the list ");
								fw.close();
								bfw.close();
								scan.close();
							} catch (HeadlessException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}

					} else {
						JOptionPane.showMessageDialog(null, BUNDLE.getString("WebBlocker.btnRemove.NositeToRemove1")
								+ BUNDLE.getString("WebBlocker.btnRemove.NositeToRemove2"));
						// JOptionPane.showMessageDialog(null,
						// BUNDLE.getString("WebBlocker.btnRemove.NositeToRemove1")
						// + textField.getText() +
						// BUNDLE.getString("WebBlocker.btnRemove.NositeToRemove2"));
						// JOptionPane.showMessageDialog(null, "There is no " +
						// textField.getText() + " to remove");
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnRemove.setFont(new Font("Dialog", Font.BOLD, 20));
		btnRemove.setBounds(139, 96, 119, 46);
		frame.getContentPane().add(btnRemove);

		JButton btnListSite = new JButton(BUNDLE.getString("WebBlocker.btnListSite.text")); //$NON-NLS-1$
		btnListSite.setToolTipText(BUNDLE.getString("WebBlocker.btnListSite.toolTipText")); //$NON-NLS-1$
		btnListSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(hosts));
					String s = "", line = null;
					while ((line = br.readLine()) != null) {
						s += "\n" + line;
					}
					// System.out.print(s);
					JOptionPane.showMessageDialog(null, s);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnListSite.setFont(new Font("Dialog", Font.BOLD, 20));
		btnListSite.setBounds(268, 96, 119, 46);
		frame.getContentPane().add(btnListSite);

		JButton btnExitApplication = new JButton(BUNDLE.getString("WebBlocker.btnExitApplication.text")); //$NON-NLS-1$
		btnExitApplication.setToolTipText(BUNDLE.getString("WebBlocker.btnExitApplication.toolTipText")); //$NON-NLS-1$
		btnExitApplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.exit(1);
				Runtime.getRuntime().exit(1);
			}
		});
		btnExitApplication.setFont(new Font("Dialog", Font.BOLD, 20));
		btnExitApplication.setBounds(509, 96, 119, 44);
		frame.getContentPane().add(btnExitApplication);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 657, 21);
		frame.getContentPane().add(menuBar);

		JMenu mnHelp = new JMenu(BUNDLE.getString("WebBlocker.mnHelp.text")); //$NON-NLS-1$
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem(BUNDLE.getString("WebBlocker.mntmAbout.text")); //$NON-NLS-1$
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About ab = About.getInstance();
				ab.setIconImage(frameicon);
				ab.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		JMenu mnLaunguage = new JMenu(BUNDLE.getString("WebBlocker.mnLaunguage.text")); //$NON-NLS-1$
		menuBar.add(mnLaunguage);

		JMenuItem mntmHindi = new JMenuItem(BUNDLE.getString("WebBlocker.mntmHindi.text")); //$NON-NLS-1$
		mntmHindi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Locale locale = new Locale("hi", "IN");
				setLocale(locale);
				int option = JOptionPane.showConfirmDialog(null,
						BUNDLE.getString("WebBlocker.mntmHindi.confirmationMessage"));
				if (option == JOptionPane.OK_OPTION) {
					frame.dispose();
					WebBlocker.main(null);
					SwingUtilities.updateComponentTreeUI(frame);
				}

			}
		});
		mnLaunguage.add(mntmHindi);

		JMenuItem mntmEnglish = new JMenuItem(BUNDLE.getString("WebBlocker.mntmEnglish.text")); //$NON-NLS-1$
		mntmEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale locale = new Locale("en", "US");
				setLocale(locale);
				int option = JOptionPane.showConfirmDialog(null,
						BUNDLE.getString("WebBlocker.mntmEnglish.confirmationMessage"));
				if (option == JOptionPane.OK_OPTION) {
					frame.dispose();
					WebBlocker.main(null);
					SwingUtilities.updateComponentTreeUI(frame);
				}
			}
		});
		mnLaunguage.add(mntmEnglish);

		// JLabel lblNewLabel = new JLabel("<html><font
		// color='red'>MENU</font></html>");
		JLabel lblNewLabel = new JLabel(BUNDLE.getString("WebBlocker.lblNewLabel.text")); //$NON-NLS-1$
		lblNewLabel.setForeground(Color.GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 22));
		lblNewLabel.setBounds(0, 32, 236, 60);
		frame.getContentPane().add(lblNewLabel);

		JMenu mnTheme = new JMenu(BUNDLE.getString("WebBlocker.mnTheme.text")); //$NON-NLS-1$
		menuBar.add(mnTheme);

		JMenuItem mntmTest = new JMenuItem(BUNDLE.getString("WebBlocker.mntmTest.text")); //$NON-NLS-1$
		mntmTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmTest);

		JMenuItem mntmSmartLookAnd = new JMenuItem(BUNDLE.getString("WebBlocker.mntmSmartLookAnd.text")); //$NON-NLS-1$
		mntmSmartLookAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmSmartLookAnd);

		JMenuItem mntmAcryllookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmAcryllookandfeel.text")); //$NON-NLS-1$
		mntmAcryllookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmAcryllookandfeel);

		JMenuItem mntmNoirelookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmNoirelookandfeel.text")); //$NON-NLS-1$
		mntmNoirelookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmNoirelookandfeel);

		JMenuItem mntmMintlookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmMintlookandfeel.text")); //$NON-NLS-1$
		mntmMintlookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmMintlookandfeel);

		JMenuItem mntmMcwinlookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmMcwinlookandfeel.text")); //$NON-NLS-1$
		mntmMcwinlookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmMcwinlookandfeel);

		JMenuItem mntmLunalookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmLunalookandfeel.text")); //$NON-NLS-1$
		mntmLunalookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmLunalookandfeel);

		JMenuItem mntmHifilookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmHifilookandfeel.text")); //$NON-NLS-1$
		mntmHifilookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmHifilookandfeel);

		JMenuItem mntmGraphitelookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmGraphitelookandfeel.text")); //$NON-NLS-1$
		mntmGraphitelookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmGraphitelookandfeel);

		JMenuItem mntmFastlookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmFastlookandfeel.text")); //$NON-NLS-1$
		mntmFastlookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmFastlookandfeel);

		JMenuItem mntmBernsteinlookandfeel = new JMenuItem(
				BUNDLE.getString("WebBlocker.mntmBernsteinlookandfeel.text")); //$NON-NLS-1$
		mntmBernsteinlookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmBernsteinlookandfeel);

		JMenuItem mntmAluminiumlookandfeel = new JMenuItem(
				BUNDLE.getString("WebBlocker.mntmAluminiumlookandfeel.text")); //$NON-NLS-1$
		mntmAluminiumlookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmAluminiumlookandfeel);

		JMenuItem mntmAerolookandfeel = new JMenuItem(BUNDLE.getString("WebBlocker.mntmAerolookandfeel.text")); //$NON-NLS-1$
		mntmAerolookandfeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		mnTheme.add(mntmAerolookandfeel);
	}
}
