package app.techy10souvik.captureeasy.core.ui.components;



import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.MatteBorder;

import org.jnativehook.mouse.SwingMouseAdapter;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.Font;

public class SplashScreenComponent {

	private JDialog frame;
	private int xx,xy;
	private JLabel lblLogo;
	private JLabel lblCaptureEasy;
	private JLabel lblVersion;
	private int counter=0;

	
	private String[] logo= {"icons/logo1.png","icons/logo2.png","icons/logo3.png","icons/logo4.png"};
	private Timer timer;
	private JLabel lblMessage;

	/**
	 * Initialize the contents of the frame.
	 */
	
	public void start() {
		frame = new JDialog();
		frame.setResizable(false);
		frame.setUndecorated(true);;
		frame.setAlwaysOnTop(true);
		frame.setBounds(700, 170, 460, 350);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);MouseListener
		frame.getContentPane().setLayout(null);
		frame.addMouseListener(new SwingMouseAdapter() {
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

			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 460, 350);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		lblLogo = new JLabel();
		lblLogo.setSize(new Dimension(200, 200));
		lblLogo.setBounds(120, 22, 220, 200);

		panel.add(lblLogo);
		
		lblCaptureEasy = new JLabel("Capture Easy ");
		lblCaptureEasy.setFont(new Font("Tahoma", Font.BOLD, 45));
		lblCaptureEasy.setBounds(78, 258, 310, 50);
		panel.add(lblCaptureEasy);
		
		lblVersion = new JLabel();
		lblVersion.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblVersion.setBounds(269, 312, 179, 25);
		panel.add(lblVersion);
		
		JLabel lblX = new JLabel("X");
        lblX.setFont(new Font("Tahoma", 0, 20));
        lblX.setForeground(Color.RED);
        lblX.setBounds(439, 0, 21, 31);
        lblX.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent arg0) {
                try {
                    //Runtime.getRuntime().exec("Taskkill /f /PID " + Library.getPID());
                }
                catch (Exception ex) {}
                System.exit(0);
            }
        });
        panel.add(lblX);
        JPanel panelMsg = new JPanel();
        panelMsg.setBackground(Color.WHITE);
        panelMsg.setBounds(12, 232, 436, 28);
        lblMessage = new JLabel("Starting Application...");
        lblMessage.setFont(new Font("Tahoma", 1, 16));
        panelMsg.add(lblMessage);
        panel.add(panelMsg);
        
		TimerTask task = new TimerTask() {
	        public void run() {
	        	setImage(getClass().getClassLoader().getResourceAsStream(logo[counter++]));
	        }
	    };
	    this.timer = new Timer("Timer");
	    this.timer.scheduleAtFixedRate(task, 0, 300);
    	//System.out.println(getClass().getClassLoader().getResource(logo[counter]).getPath());
    	setImage(getClass().getClassLoader().getResourceAsStream(logo[counter++]));
	    frame.setVisible(true);
	   
	}
	
	public void stop() {
		this.timer.cancel();
		frame.dispose();

	}
	
	public void setVersion(String version) {
		lblVersion.setText("Version : "+version);
	}
	
	public void setMessage(String message) {
		lblMessage.setText(message);
	}
	
	private void setImage(InputStream image) {
		try {
			lblLogo.setIcon(new ImageIcon(ImageIO.read(image).getScaledInstance(lblLogo.getBounds().width,lblLogo.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
			if (counter == logo.length-1) {
				counter=0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	
}
