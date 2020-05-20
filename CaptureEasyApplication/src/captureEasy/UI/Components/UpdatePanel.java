package captureEasy.UI.Components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;

import captureEasy.Resources.Library;
import captureEasy.Resources.SoftwareUpdate;
import captureEasy.UI.ActionGUI;
import javax.swing.JLabel;

public class UpdatePanel extends Library implements MouseListener,MouseMotionListener{
	
	public JPanel panel_Update;
	public JPanel panel_UpdateNo;
	int saveTabIndex=0;
	SettingsPanel settingsPanel=null;
	public JButton btnProceed;
	public JButton btnYes;
	public static JLabel lblprogressflag;
	public JButton btnNo;
	public JLabel lblDoYouWant;
	public JPanel panel_UpdateYes;
	public JLabel lblLastUpdatedOn;
	public JLabel lblTime;
	public JLabel lblCurrentVersion;
	public JLabel lblNewLabel;
	public JLabel lblName;
	public JLabel lblVersion;
	public JLabel lblPublish;

	public UpdatePanel(JTabbedPane TabbledPanel) {
		panel_Update = new JPanel();
		panel_Update.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_Update.setBackground(Color.WHITE);
		panel_Update.setLayout(null);
		panel_Update.addMouseListener(this);
		panel_Update.addMouseMotionListener(this);
		//loadUpdated();
		//panel_Update.add(panel_UpdateNo);
		
	}
	
	public void loadUpdated() throws Exception
	{
		panel_UpdateNo = new JPanel();
		//panel_UpdateNo.setVisible(false);
		panel_UpdateNo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_UpdateNo.setBounds(12, 13, 405, 291);
		panel_UpdateNo.setLayout(null);
		panel_Update.add(panel_UpdateNo);


		btnProceed = new JButton("Exit");
		ActionGUI.dialog.getRootPane().setDefaultButton(btnProceed);
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ActionGUI.leaveControl=true;
				ActionGUI.dialog.dispose();
				
			}
		});
		btnProceed.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnProceed.setSelected(true);
		btnProceed.setBounds(144, 253, 110, 25);
		panel_UpdateNo.add(btnProceed);
		
		JLabel lblUpdateApplication = new JLabel("Your Application is upto date");
		lblUpdateApplication.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblUpdateApplication.setBounds(75, 30, 260, 25);
		panel_UpdateNo.add(lblUpdateApplication);
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(51, 84, 302, 137);
		panel_UpdateNo.add(panel);
		panel.setLayout(null);
		
		lblLastUpdatedOn = new JLabel("Last Updated on :");
		lblLastUpdatedOn.setBounds(25, 21, 265, 20);
		lblLastUpdatedOn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblLastUpdatedOn);
		
		lblTime = new JLabel("Time :");
		lblTime.setBounds(25, 57, 265, 20);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblTime);
		
		lblCurrentVersion = new JLabel("Current Version : ");
		lblCurrentVersion.setBounds(25, 91, 265, 20);
		lblCurrentVersion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblCurrentVersion);
	}
	
	public void loadNeedUpdate() throws Exception
	{
		panel_UpdateYes = new JPanel();
		panel_UpdateYes.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_UpdateYes.setBounds(12, 13, 405, 291);
		panel_UpdateYes.setLayout(null);
		
		lblNewLabel = new JLabel("Your application is out dated");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(75, 25, 260, 25);
		panel_UpdateYes.add(lblNewLabel);
		
		JLabel lblAnewVersionIs = new JLabel("A newer version is available.");
		lblAnewVersionIs.setBounds(90, 60, 243, 25);
		panel_UpdateYes.add(lblAnewVersionIs);
		lblAnewVersionIs.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(52, 90, 299, 123);
		panel_UpdateYes.add(panel_2);
		panel_2.setLayout(null);
		
		lblName = new JLabel();
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName.setBounds(15, 15, 300, 25);
		panel_2.add(lblName);
		
		lblVersion = new JLabel();
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblVersion.setBounds(15, 50, 300, 25);
		panel_2.add(lblVersion);
		
		lblPublish = new JLabel();
		lblPublish.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPublish.setBounds(15, 80, 272, 25);
		panel_2.add(lblPublish);
		
		lblDoYouWant = new JLabel("Do You want to download it now?");
		lblDoYouWant.setBounds(65, 220, 300, 25);
		panel_UpdateYes.add(lblDoYouWant);
		lblDoYouWant.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnYes = new JButton("Yes");
		btnYes.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnYes.setBounds(110, 250, 100, 25);
		panel_UpdateYes.add(btnYes);
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblprogressflag.setVisible(true);
				btnNo.setVisible(false);
				btnYes.setVisible(false);
				lblDoYouWant.setVisible(false);
				lblprogressflag.setText("Download started");
				SoftwareUpdate.startDownload();
			}
		});
		
		btnNo = new JButton("No");
		btnNo.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNo.setBounds(220, 250, 100, 25);
		panel_UpdateYes.add(btnNo);
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ActionGUI.leaveControl=true;
				ActionGUI.dialog.dispose();
				
			}
		});
		
		lblprogressflag = new JLabel();
		lblprogressflag.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblprogressflag.setBounds(50, 250, 301, 25);
		lblprogressflag.setVisible(false);
		panel_UpdateYes.add(lblprogressflag);
	}

	

	@Override
	public void mouseDragged(MouseEvent arg0) {
		ActionGUI.xDialog = arg0.getXOnScreen();
		ActionGUI.yDialog = arg0.getYOnScreen();
		ActionGUI.dialog.setLocation(ActionGUI.xDialog - ActionGUI.xxDialog, ActionGUI.yDialog - ActionGUI.xyDialog); 		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		ActionGUI.xxDialog = e.getX();
		ActionGUI.xyDialog = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
