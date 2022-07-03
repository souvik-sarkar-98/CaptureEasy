package app.techy10souvik.captureeasy.core.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;

import org.jnativehook.mouse.SwingMouseAdapter;

public class TestOptionPane11 {
	private int xx;
	private int xy;
    public static void main(String[] args) {
        new TestOptionPane11();
    }

    public TestOptionPane11() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                final JDialog dialog = new JDialog((Frame)null, "Boo");

                JOptionPane op = new JOptionPane("Look ma, no hands", JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                op.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        String name = evt.getPropertyName();
                        if ("value".equals(name)) {

                            dialog.dispose();

                        }
                    }
                });
                op.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
                dialog.setUndecorated(true);
                dialog.setLayout(new BorderLayout());
                dialog.add(op);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
               // dialog.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));

                dialog.addMouseListener(new SwingMouseAdapter() {
        			
        			private static final long serialVersionUID = 1L;
				

        			@Override
        			public void mousePressed(MouseEvent e) {

        				xx = e.getX();
        				xy = e.getY();
        			}
        		});
                dialog.addMouseMotionListener(new MouseMotionAdapter() {
        			@Override
        			public void mouseDragged(MouseEvent arg0) {

        				int x = arg0.getXOnScreen();
        				int y = arg0.getYOnScreen();
        				dialog.setLocation(x - xx, y - xy);  
        				//dialog.setBounds(x - xx, y - xy, 425, 225);

        			}
        		});
            }
        });
    }

}