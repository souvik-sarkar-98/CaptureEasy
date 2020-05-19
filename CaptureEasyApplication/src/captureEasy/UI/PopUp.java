package captureEasy.UI;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jnativehook.mouse.SwingMouseAdapter;

import captureEasy.Resources.Library;
import captureEasy.Resources.PathsNKeys;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JTextArea;

import java.awt.SystemColor;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PopUp extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int xx,xy;
	public static boolean control=true;
	public static boolean decision;
	public JButton btnNewButton;
	public JButton btnNo;
	public JTextArea txtrExceptionOccuredPlease;

	/**
	 * Launch the application.
	 */
	public JLabel lblIcon;
	public static JDialog  PopDia;
	

	/**
	 * Create the application.
	 */
	
	public PopUp(String title,String PopUpType,String text,String OkButton_Text,String cancelButton_Text) 
	{
		control=false;
		try{ActionGUI.dialog.setAlwaysOnTop(false);}catch(Exception e){}
		try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e){}
		try{PopDia.dispose();}catch(Exception e){}
		setResizable(false);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setUndecorated(true);
		setAlwaysOnTop(true);
		setLocation(screensize.width/2-300, screensize.height/2-300);  
		setBounds(screensize.width/2-300, screensize.height/2-300, 425, 225);
		getContentPane().setLayout(null);
		addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mousePressed(MouseEvent e) {

				xx = e.getX();
				xy = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				int x = arg0.getXOnScreen();
				int y = arg0.getYOnScreen();
				setLocation(x - xx, y - xy);  
				setBounds(x - xx, y - xy, 425, 225);
			}
		});

		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		panel.setBounds(0, 0, 425, 225);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		JLabel lblError = new JLabel(title);
		lblError.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_1.add(lblError);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.menu);
		panel.add(panel_2, BorderLayout.SOUTH);

		btnNewButton = new JButton();
		btnNewButton.setVisible(false);
		if(!OkButton_Text.equalsIgnoreCase(""))
		{
			btnNewButton.setVisible(true);
			btnNewButton.setText(OkButton_Text);
			btnNewButton.setToolTipText("Click");
			btnNewButton.setSelected(true);
		}	
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try{ActionGUI.dialog.setAlwaysOnTop(true);}catch(Exception e){}
				control=true;
				decision=true;
				dispose();
				try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e){}
			}
		});
		btnNewButton.setBackground(new Color(204, 204, 255));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_2.add(btnNewButton);

		btnNo = new JButton();

		btnNo.setVisible(false);
		if(!cancelButton_Text.equalsIgnoreCase(""))
		{
			btnNo.setVisible(true);
			btnNo.setText(cancelButton_Text);
			btnNo.setToolTipText("Click");
		}

		btnNo.setBackground(new Color(204, 204, 255));
		btnNo.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{ActionGUI.dialog.setAlwaysOnTop(true);}catch(Exception e){}
				dispose();
				control=true;
				decision=false;
				try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e){}

			}
		});

		panel_2.add(btnNo);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.WEST);

		JPanel panel_4 = new JPanel();
		panel.add(panel_4, BorderLayout.EAST);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_5.setBackground(Color.WHITE);
		panel.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(null);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_6.setBackground(new Color(255, 255, 204));
		panel_6.setBounds(12, 13, 377, 125);
		if(OkButton_Text.equals("") && cancelButton_Text.equals(""))
		{
			panel_6.setBounds(12, 13, 377, 155);
		}
		panel_5.add(panel_6);

		lblIcon = new JLabel("Icon");
		lblIcon.setBounds(10, 35,50,50);


		try {
			Dimension size = lblIcon.getSize();
			BufferedImage master;
			if(PopUpType.equalsIgnoreCase("Error"))
			{
				master = ImageIO.read(new File(PathsNKeys.errorIcon));
			}
			else if(PopUpType.equalsIgnoreCase("Warning"))
			{
				master = ImageIO.read(new File(PathsNKeys.warningIcon));
			}
			else if(PopUpType.equalsIgnoreCase("comment"))
			{
				master = ImageIO.read(new File(PathsNKeys.commentIcon));
			}
			else
			{
				master = ImageIO.read(new File(PathsNKeys.informationIcon));
			}
			Image scaled = master.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
			lblIcon.setIcon(new ImageIcon(scaled));
		} catch (IOException e) {
			Library.logError(e,"Exception on reading image in POPUP");
		}
		panel_6.setLayout(null);

		panel_6.add(lblIcon);

		JPanel panel_7 = new JPanel();
		panel_7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_7.setBackground(new Color(255, 255, 204));
		panel_7.setBounds(75, 13, 285, 110);
		panel_6.add(panel_7);

		txtrExceptionOccuredPlease = new JTextArea();
		txtrExceptionOccuredPlease.setBounds(0, 0, 285, 100);

		txtrExceptionOccuredPlease.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				if(txtrExceptionOccuredPlease.isEditable())
					txtrExceptionOccuredPlease.setToolTipText(txtrExceptionOccuredPlease.getText());
			}
			public void removeUpdate(DocumentEvent e) {
				if(txtrExceptionOccuredPlease.isEditable())
					txtrExceptionOccuredPlease.setToolTipText(txtrExceptionOccuredPlease.getText());
			}
		});
		txtrExceptionOccuredPlease.setRows(5);
		if(PopUpType.equalsIgnoreCase("Error"))
		{
			txtrExceptionOccuredPlease.setForeground(Color.RED);
		}
		else
		{
			txtrExceptionOccuredPlease.setForeground(Color.BLACK);
		}
		if(PopUpType.equalsIgnoreCase("comment"))
		{
			txtrExceptionOccuredPlease.setFocusable(true);
			txtrExceptionOccuredPlease.setEditable(true);
			txtrExceptionOccuredPlease.setEnabled(true);
			txtrExceptionOccuredPlease.setBackground(Color.WHITE);
			txtrExceptionOccuredPlease.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			txtrExceptionOccuredPlease.requestFocusInWindow();
		}
		else
		{
			txtrExceptionOccuredPlease.setBackground(new Color(255, 255, 204));
			txtrExceptionOccuredPlease.setEditable(false);
			txtrExceptionOccuredPlease.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			txtrExceptionOccuredPlease.setFocusable(false);
		}
		txtrExceptionOccuredPlease.setLineWrap(true);
		txtrExceptionOccuredPlease.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtrExceptionOccuredPlease.setColumns(18);
		int len=text.length();
		//txtrExceptionOccuredPlease.setToolTipText(text);
		String toolText="<html>";
		try{
			
			if(len>50)
			{
				int i=0;
				for(;i<(len/50);i++)
					toolText=toolText+text.substring(i*50, 50*(i+1))+"<br>";
				toolText=toolText+text.substring(i*50)+"</html>";
				
			}
		}catch(Exception e){}
		
		txtrExceptionOccuredPlease.setToolTipText(toolText);
		/*try{
			String tt="<html>";
			for(int i=0;i<len;i++)
			{
				tt=tt+text.charAt(i);
				if(text.charAt(i)=='.')
					tt=tt+"<br>";
			}
			txtrExceptionOccuredPlease.setToolTipText(tt+"</html>");
		}catch(Exception e){}*/
		if(len>140)
		{
			text=text.substring(0, 140)+" "
					+ "...";
		}
		txtrExceptionOccuredPlease.setText(text);

		;
		panel_7.setLayout(null);

		panel_7.add(txtrExceptionOccuredPlease);

		setVisible(true); 
		try{getRootPane().setDefaultButton(btnNewButton);}catch(Exception e){}
	}
}
