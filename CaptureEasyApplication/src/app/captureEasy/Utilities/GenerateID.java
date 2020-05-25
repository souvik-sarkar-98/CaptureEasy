package app.captureEasy.Utilities;


import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import app.captureEasy.Resources.Library;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;


public class GenerateID extends Library{

	public JFrame d;
	JLabel lblPassword;
	public JButton btnProceed;
	private int xx;
	private int xy;
	private JPanel panel;
	private JLabel lblUsernameCannotBe;
	public static boolean leavecontrol=true;
	private JTextField textField;
	public JTextField textField_1;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JRadioButton rdbtnSouthAfrica;
	private JRadioButton rdbtnNamibia;
	private JLabel lblGender;
	private JLabel lblCountry;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JLabel label_error;
	private JLabel lblCopied;
	private JButton lblExit;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenerateID window = new GenerateID();
					window.d.setVisible(true);
					window.textField_1.requestFocusInWindow();
					window.d.getRootPane().setDefaultButton(window.btnProceed);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 */
	public GenerateID() {
		leavecontrol=false;
		initializeGenerateID();
	}

	/**
	 * Initialize the contents of the d.
	 */
	private void initializeGenerateID() {
		d = new JFrame();
		d.setUndecorated(true);
		d.setAlwaysOnTop(true);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		d.getContentPane().setLayout(null);
		d.setBounds(screensize.width / 2 - 300, screensize.height / 2 - 300, 400, 230);
		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			d.setIconImages(icons);

		} catch (IOException e2) {
			logError(e2,"Exception occured while reading icon");
		}

		panel = new JPanel();
		panel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		panel.setBounds(0, 0, 400, 230);
		d.getContentPane().add(panel);
		panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				xx = e.getX();
				xy = e.getY();
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				int x = arg0.getXOnScreen();
				int y = arg0.getYOnScreen();
				d.setLocation(x - xx, y - xy);  
			}
		});
		panel.setLayout(null);

		JLabel lblGenId = new JLabel("Generate National ID");
		lblGenId.setBounds(109, 7, 188, 20);
		lblGenId.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblGenId);

		JLabel lblUsername = new JLabel("National ID :");
		lblUsername.setBounds(30, 45, 104, 20);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblUsername);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(146, 44, 179, 22);
		panel.add(textField);
		textField.setColumns(20);

		


		lblPassword = new JLabel("DOB (DD/MM/YYYY) :");
		lblPassword.setBounds(30, 80, 179, 20);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblPassword);

		btnProceed = new JButton("Generate");
		btnProceed.setToolTipText("Generate National Id");
		btnProceed.setMargin(new Insets(2, 5, 2, 5));
		btnProceed.setBounds(95, 185, 100, 29);
		btnProceed.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					textField.setText("");
					textField_1.setToolTipText("");
					textField_1.setBackground(Color.WHITE);
					label_error.setVisible(false);
					lblCopied.setVisible(false);

				if(isValidDate(textField_1.getText()))
				{
					textField_1.setBackground(Color.WHITE);
					label_error.setVisible(false);
					String id=getNationalID(textField_1.getText(),rdbtnMale.isSelected(),rdbtnSouthAfrica.isSelected());
					textField.setText(id);
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents((Transferable) (new StringSelection(id)), null);
					lblCopied.setVisible(true);
					
				}
				else
				{
					textField_1.setBackground(Color.PINK);
					textField_1.setToolTipText("Invalid Date");
					label_error.setVisible(true);
				}
				
			}
		});
		String mask="##/##/####";
		textField_1 = new JFormattedTextField(createFormatter(mask));
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_1.setBounds(224, 79, 142, 22);
		panel.add(textField_1);
		textField_1.setColumns(10);
		textField_1.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {}
			public void removeUpdate(DocumentEvent e) {}
		});
		
		lblGender = new JLabel("Gender :");
		lblGender.setBounds(30, 113, 70, 20);
		lblGender.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblGender);
		
		rdbtnMale = new JRadioButton("Male");
		buttonGroup_1.add(rdbtnMale);
		rdbtnMale.setSelected(true);
		rdbtnMale.setBounds(109, 112, 59, 22);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(rdbtnMale);
		
		rdbtnFemale = new JRadioButton("Female");
		buttonGroup_1.add(rdbtnFemale);
		rdbtnFemale.setBounds(225, 110, 75, 22);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(rdbtnFemale);
		
		lblCountry = new JLabel("Country :");
		lblCountry.setBounds(30, 145, 75, 20);
		lblCountry.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblCountry);
		
		rdbtnSouthAfrica = new JRadioButton("South Africa");
		buttonGroup.add(rdbtnSouthAfrica);
		rdbtnSouthAfrica.setSelected(true);
		rdbtnSouthAfrica.setBounds(109, 145, 107, 22);
		rdbtnSouthAfrica.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(rdbtnSouthAfrica);
		
		rdbtnNamibia = new JRadioButton("Namibia");
		buttonGroup.add(rdbtnNamibia);
		rdbtnNamibia.setBounds(225, 145, 79, 22);
		rdbtnNamibia.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(rdbtnNamibia);
		btnProceed.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(btnProceed);

		lblUsernameCannotBe = new JLabel();
		lblUsernameCannotBe.setBounds(442, 105, 0, 0);

		lblUsernameCannotBe.setForeground(Color.RED);
		lblUsernameCannotBe.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblUsernameCannotBe);
		
		JButton btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			Timer timer;
			public void actionPerformed(ActionEvent arg0) {
				try{timer.stop();}catch(Exception e){}
				Random random=new Random();
				textField_1.setBackground(Color.WHITE);
				label_error.setVisible(false);
				String randomDay=String.valueOf(random.nextInt(28-1)+1);String randomMonth=String.valueOf(random.nextInt(12-1)+1);
				if(randomDay.length()==1)
					randomDay="0"+randomDay;
				if(randomMonth.length()==1)
					randomMonth="0"+randomMonth;
				textField_1.setText(randomDay+randomMonth+String.valueOf(random.nextInt((2000-1975)+1)+1975));
				boolean gen=random.nextBoolean();
				rdbtnMale.setSelected(gen);
				rdbtnFemale.setSelected(!gen);
				String id=getNationalID(textField_1.getText(),gen,rdbtnSouthAfrica.isSelected());
				textField.setText(id);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents((Transferable) (new StringSelection(id)), null);
				lblCopied.setVisible(true);
				
				
			}
		});
		btnRandom.setToolTipText("Generate Random National Id");
		btnRandom.setMargin(new Insets(2, 5, 2, 5));
		btnRandom.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnRandom.setBounds(210, 185, 100, 29);
		panel.add(btnRandom);
		
		lblExit = new JButton();
		lblExit.setBounds(375, 4, 20, 20);
		lblExit.setBorderPainted(false);
		try{
			lblExit.setIcon(new ImageIcon(ImageIO.read(new File(exitIcon)).getScaledInstance(lblExit.getBounds().width,lblExit.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			lblExit.setText("exit");logError(e,"Exception in Icon loading: Image "+exitIcon+" Not Available");
		}
		lblExit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				d.dispose();
			}
			
			
		});
		lblExit.setBackground(new Color(0,0,0,0));
		lblExit.setBorderPainted(false);
		panel.add(lblExit);
		
		label_error = new JLabel("");
		label_error.setVisible(false);
		label_error.setToolTipText("Invalid Date");
		label_error.setBounds(372, 82, 16, 16);
		panel.add(label_error);
		try{
			label_error.setIcon(new ImageIcon(ImageIO.read(new File(errorIcon)).getScaledInstance(label_error.getBounds().width,label_error.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			label_error.setText("Close");logError(e,"Exception in Icon loading: Image "+errorIcon+" Not Available");
		}
		
		lblCopied = new JLabel("Copied");
		lblCopied.setVisible(false);
		lblCopied.setForeground(new Color(0, 153, 0));
		lblCopied.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCopied.setBounds(335, 43, 56, 22);
		panel.add(lblCopied);
		

	}
	public static boolean isValidDate(String strDate)
	   {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(strDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
		  
	   }
	public String getNationalID(String DOB, boolean isGenderMale,boolean isCountrySA)
	{
		String random = null;
		String day=DOB.split("/")[0],month=DOB.split("/")[1],Year=DOB.split("/")[2];
		if(Year.length()==4)
		{
			Year=Year.substring(2,4);
		}
		if(isGenderMale)
			random=String.valueOf(new Random().nextInt(9999-5000)+5000);
		else
			random=String.valueOf(new Random().nextInt(4999));
		int len=random.length();
		if(len<4)
		{
			for(int i=0;i<4-len;i++)
				random="0"+random;
		}
		if(isCountrySA)
			random=random+"08";
		else
			random=random+"18";

		return Year+month+day+random+checkLuhn(Year+month+day+random);
	}
	int checkLuhn(String Number) 
	{ 
		int nDigits = Number.length(); 

		int oSum = 0,eSum=0; 

		for (int i = nDigits - 2; i >= 0; i=i-2)  
		{ 
			oSum=oSum+Character.getNumericValue(Number.charAt(i));
		} 
		for (int i = nDigits - 1; i >= 0; i=i-2)  
		{ 
			String s=String.valueOf(Character.getNumericValue(Number.charAt(i))*2);
			if(s.length()==2)
			{
				s=String.valueOf(Character.getNumericValue(s.charAt(0))+Character.getNumericValue(s.charAt(1)));
			}
			eSum=eSum+Integer.parseInt(s);
		} 
		if((eSum+oSum)%10==0)
			return 0;
		else
			return 10-((eSum+oSum)%10);

	}
	  protected static MaskFormatter createFormatter(String s) {
		    MaskFormatter formatter = null;
		    try {
		      formatter = new MaskFormatter(s);
		    } catch (ParseException ex) {
		      System.err.println("formatter is bad: " + ex.getMessage());
		    }
		    return formatter;
		  }
}
