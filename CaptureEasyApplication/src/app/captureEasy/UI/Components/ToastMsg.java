package app.captureEasy.UI.Components;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import app.captureEasy.Annotations.NoLogging; 

public class ToastMsg extends JFrame { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JWindow w;
	JPanel p=null;
	String msg;
	int x,y,width;
	public ToastMsg(String s) 
	{ 
		msg=s;

		w = new JWindow(); 
		w.setBackground(new Color(0, 0, 0, 0)); 
		w.setAlwaysOnTop(true);
		//System.out.println(s);
		setText(s);

	} 
	public void setText(String text)
	{
		msg=text;
		Font font=new Font("Tahoma", Font.BOLD, 18);
		width=(int)(font.getStringBounds(text, new FontRenderContext(new AffineTransform(),true,true)).getWidth())-25;
		setEndLocation(this.x,this.y);
		if(p!=null)
			w.remove(p);
		p = new JPanel() { 
			private static final long serialVersionUID = 1L;
			@NoLogging
			public void paintComponent(Graphics g) 
			{
				int height = g.getFontMetrics().getHeight(); 
				g.setColor(Color.BLACK); 
				g.fillRect(10, 10, width + 45, height + 15); 
				g.setColor(Color.BLACK); 
				g.drawRect(10, 10, width + 45, height + 15); 
				g.setFont(font);
				g.setColor(new Color(255, 255, 255, 240)); 
				g.drawString(text, 20, 32); 
				int t = 250; 
				for (int i = 0; i < 4; i++) { 
					t -= 60; 
					g.setColor(new Color(0, 0, 0, t)); 
					g.drawRect(10 - i, 10 - i, width + 45 + i * 2, height + 15 + i * 2); 
				} 
			}
		}; 	

		w.add(p); 
		w.setVisible(true);
		w.setSize(width+500,100);

	}
	
	
	public void terminationLogic() throws InterruptedException
	{
		Thread.sleep(2000);
	}
	
	public void setEndLocation(int x,int y) 
	{
		this.x=x;this.y=y;
		w.setLocation(x-width-60, y); 

	}
	public void setLocation(int x,int y) 
	{
		this.x=x;this.y=y;
		w.setLocation(x, y); 

	}

	public void showToast() 
	{ 
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {  
					w.setOpacity(1); 
					terminationLogic();
					for (double d = 1.0; d > 0.2; d -= 0.1) { 
						try {Thread.sleep(100);} catch (InterruptedException e) {} 
						w.setOpacity((float)d); 
					} 
					w.setVisible(false); 
					w.dispose();
				} 
				catch (Exception e) { 
				} 

			}

		}).start();

	} 
	public void terminateAfter(int milisec)
	{
		try{Thread.sleep(milisec);}catch(Exception e){}
		w.setVisible(false); 
		w.dispose();
	}

} 
