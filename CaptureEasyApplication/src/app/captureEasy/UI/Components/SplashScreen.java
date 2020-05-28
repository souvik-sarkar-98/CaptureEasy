package app.captureEasy.UI.Components;

import org.jnativehook.mouse.SwingMouseAdapter;

import app.captureEasy.Annotations.NoLogging;
import app.captureEasy.Resources.Library;
import app.captureEasy.UI.ActionGUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.Font;

public class SplashScreen extends Library{

	public JDialog frame;
	//public static boolean displaySplash=true;
	/**
	 * Create the application.
	 */
	int count=0;
	@NoLogging
	public SplashScreen() {
		initialize();
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					do
					{
						if(count==0)
						{
							lblLogo1.setIcon(new ImageIcon(ImageIO.read(new File(logoIcon1)).getScaledInstance(lblLogo1.getBounds().width,lblLogo1.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
							count++;
						}
						else if(count==1)
						{
							lblLogo1.setIcon(new ImageIcon(ImageIO.read(new File(logoIcon2)).getScaledInstance(lblLogo1.getBounds().width,lblLogo1.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
							count++;
						}
						else if(count==2)
						{
							lblLogo1.setIcon(new ImageIcon(ImageIO.read(new File(logoIcon3)).getScaledInstance(lblLogo1.getBounds().width,lblLogo1.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
							count++;
						}
						else if(count==3)
						{
							lblLogo1.setIcon(new ImageIcon(ImageIO.read(new File(logoIcon4)).getScaledInstance(lblLogo1.getBounds().width,lblLogo1.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
							count=0;
						}
						Thread.sleep(250);

					}while(terminate());
				} catch (InterruptedException | IOException e) {
				}
				frame.dispose();
			}

		}).start();

	}
	@NoLogging
	boolean terminate()
	{
		if(senGUI!=null && senGUI.frame.isVisible())
			return false;
		else if(ActionGUI.dialog!=null && ActionGUI.dialog.isVisible())
			return false;
		else 
			return true;
	}
	int xx,xy;
	public JLabel lblLogo1;
	public JLabel lblCaptureEasy;
	public JLabel lblVersion;

	/**
	 * Initialize the contents of the frame.
	 */
	@NoLogging
	private void initialize() {
		frame = new JDialog();
		frame.setResizable(false);
		frame.setUndecorated(true);;
		frame.setAlwaysOnTop(true);
		frame.setBounds(700, 170, 460, 350);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addMouseListener(new SwingMouseAdapter() {

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
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				int x = arg0.getXOnScreen();
				int y = arg0.getYOnScreen();
				frame.setLocation(x - xx, y - xy);  
				frame.setBounds(x - xx, y - xy, 460, 350);
				System.out.print(x - xx+"   ");
				System.out.println(y - xy);
			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 460, 350);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		lblLogo1 = new JLabel();
		lblLogo1.setSize(new Dimension(200, 200));
		lblLogo1.setBounds(120, 25, 220, 200);

		try {
			lblLogo1.setIcon(new ImageIcon(ImageIO.read(new File(logoIcon1)).getScaledInstance(lblLogo1.getBounds().width,lblLogo1.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		panel.add(lblLogo1);
		
		lblCaptureEasy = new JLabel("Capture Easy ");
		lblCaptureEasy.setFont(new Font("Tahoma", Font.BOLD, 50));
		lblCaptureEasy.setBounds(60, 235, 345, 68);
		panel.add(lblCaptureEasy);
		
		lblVersion = new JLabel("Version : ");
		lblVersion.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblVersion.setBounds(250, 305, 179, 25);
		panel.add(lblVersion);


	}
}
