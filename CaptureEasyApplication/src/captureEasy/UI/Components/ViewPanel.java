package captureEasy.UI.Components;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;

import captureEasy.Launch.Application;
import captureEasy.Resources.Library;
import captureEasy.Resources.SharedResources;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;

public class ViewPanel extends Library implements MouseListener,MouseMotionListener{
	public JPanel ViewScrollPane;
	public JPanel panel_Image;
	public static JLabel ImageLabel;
	public JLabel label_Prev;
	public JLabel label_VisitFolder;
	public JLabel Label_FullView;
	public JLabel label_Next;
	public JLabel label_Delete;
	public JPanel panel_Button;
	public static File[] files;
	public static int imgId;
	PopUp p = null;
	public JLabel lblExit;
	public JLabel label_SetComment;
	private JTabbedPane TabbedPanel;
	public boolean viewLoaded=false;

	public ViewPanel(JTabbedPane TabbedPanel)
	{
		this.TabbedPanel=TabbedPanel;
		ViewScrollPane = new JPanel();
		ViewScrollPane.setSize(new Dimension(434, 319));
		ViewScrollPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		ViewScrollPane.setBackground(new Color(255, 255, 255));
		ViewScrollPane.setLayout(null);
		ViewScrollPane.addMouseListener(this);
		ViewScrollPane.addMouseMotionListener(this);

	}
	public void loadViewTab()
	{


		{
			panel_Image = new JPanel();
			panel_Image.setSize(new Dimension(410, 250));
			panel_Image.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
			panel_Image.setBounds(12, 13, 410, 250);
			panel_Image.setLayout(null);
			{
				ImageLabel = new JLabel();
				ImageLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//////

						////////Zooommmm
						/////or full screen
					}
				});
				ViewPanel.ImageLabel.setToolTipText(null);
				ImageLabel.setSize(new Dimension(400, 250));
				ImageLabel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				ImageLabel.setBounds(0, 0, 410, 250);
				panel_Image.add(ImageLabel);
			}
		}

		{
			panel_Button = new JPanel();
			panel_Button.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			panel_Button.setBounds(12, 272, 410, 35);
			panel_Button.setLayout(null);
			{
				label_Prev = new JLabel("Previous");
				label_Prev.setSize(new Dimension(25, 25));
				label_Prev.setToolTipText("Swipe left");
				label_Prev.setBounds(160, 5, 33, 25);
				panel_Button.add(label_Prev);
				label_Prev.addMouseListener(new MouseAdapter(){
					@Override
					public void mouseClicked(MouseEvent arg0) {
						gotoPreviousImage();
					}
				});
				try {
					label_Prev.setIcon(new ImageIcon(ImageIO.read(new File(leftarrowIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					logError(e,"Exception in Icon loading: Image "+leftarrowIcon+" Not Available");
				}
			}
			{
				Label_FullView = new JLabel(" ");
				Label_FullView.setSize(new Dimension(25, 25));
				Label_FullView.setToolTipText("View fullscreen with image viewer");
				Label_FullView.setBounds(195, 5, 33, 25);
				panel_Button.add(Label_FullView);
				Label_FullView.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(Label_FullView.isEnabled())
						{
							try {
								if(ActionPanel.panel_4==null)
								{
									ActionGUI.dialog.dispose();
									ActionGUI.leaveControl=true;
									Application.sensor.play();
								}
								Desktop.getDesktop().open(files[imgId]);
							} catch (IOException e1) {}			
						}
					}
				});
				try {
					Label_FullView.setIcon(new ImageIcon(ImageIO.read(new File(redbuttonIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					logError(e,"Exception in Icon loading: Image "+redbuttonIcon+" Not Available");

				}
			}
			{
				label_Next = new JLabel(" ");
				label_Next.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) {
						gotoNextImage();
					}
				});

				label_Next.setSize(new Dimension(25, 25));
				label_Next.setToolTipText("Swipe right");
				label_Next.setBounds(230, 5, 33, 25);

				panel_Button.add(label_Next);
				try {
					label_Next.setIcon(new ImageIcon(ImageIO.read(new File(rightarrowIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					logError(e,"Exception in Icon loading: Image "+rightarrowIcon+" Not Available");

				}
			}
			{
				label_Delete = new JLabel("");

				label_Delete.setSize(new Dimension(25, 25));
				label_Delete.setToolTipText("Delete this image");
				label_Delete.setBounds(376, 0, 25, 30);
				label_Delete.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(label_Delete.isEnabled())
						{
							PopUp p=new PopUp("Confirm Delete","warning","Are you sure that you want to delete this file?","Yes","No");
							p.setVisible(true);
							p.btnNewButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									p.dispose();
									files[imgId].delete();
									ViewPanel.files=new File(property.getString("TempPath")).listFiles();
									sortFiles(ViewPanel.files);
									try {
										if(files.length>0)
										{
											if(imgId==files.length)
												imgId=0;
											System.out.println("files.length="+files.length+"imgId"+imgId);
											ImageLabel.setIcon(new ImageIcon(ImageIO.read(files[imgId]).getScaledInstance(410,250, java.awt.Image.SCALE_SMOOTH)));
											ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br><br>Click image to zoom</html>");
											if(comments.get(files[imgId].getName())!=null)
												ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br>Comment:"+comments.get(files[imgId].getName())+"<br><br>Click image to zoom</html>");

										}
										else
										{
											ImageLabel.setIcon(null);
											ImageLabel.setText("                                                You have nothing to view");
											ImageLabel.setToolTipText("");
											label_SetComment.setEnabled(false);
											label_Next.setEnabled(false);
											label_Prev.setEnabled(false);
											label_Delete.setEnabled(false);
											Label_FullView.setEnabled(false);
											for(int i=0;i<TabbedPanel.getTabCount();i++)
											{
												if(TabbedPanel.getTitleAt(i).toLowerCase().contains("save"))
												{
													TabbedPanel.removeTabAt(i);
													lblExit.setEnabled(true);
													break;
												}
											}
										}
									} catch (IOException e) {
										logError(e,"Exception occured");

									}
								}
							});				
						}
					}
				});

				try {
					label_Delete.setIcon(new ImageIcon(ImageIO.read(new File(deleteIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					logError(e,"Exception in Icon loading: Image "+deleteIcon+" Not Available");

				}

				panel_Button.add(label_Delete);
			}
			{
				label_VisitFolder = new JLabel("");
				label_VisitFolder.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(label_Delete.isEnabled())
						{
							try {
								Desktop.getDesktop().open(new File(property.getString("TempPath")));
							} catch (IOException e1) {
								logError(e1,"Exception on opening temporary folder ");

							}			
						}
					}
				});
				label_VisitFolder.setSize(new Dimension(25, 25));

				label_VisitFolder.setToolTipText("Visit screenshot folder");
				try {
					label_VisitFolder.setIcon(new ImageIcon(ImageIO.read(new File(folderIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					logError(e,"Exception in Icon loading: Image "+folderIcon+" Not Available");

				}
				label_VisitFolder.setBounds(12, 5, 25, 25);
				panel_Button.add(label_VisitFolder);
			}

			lblExit = new JLabel("exit");
			//lblExit.setBounds(308, 9, 20, 20);
			lblExit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(lblExit.isEnabled())
					{
						ActionGUI.dialog.dispose();
						ActionGUI.leaveControl=true;
						Application.sensor.play();
					}
				}
			});
			lblExit.setSize(new Dimension(20, 20));

			lblExit.setToolTipText("Exit");
			try {
				lblExit.setIcon(new ImageIcon(ImageIO.read(new File(exitIcon)).getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				logError(e,"Exception in Icon loading: Image "+exitIcon+" Not Available");

			}
			lblExit.setBounds(340, 7, 20, 20);
			panel_Button.add(lblExit);

			label_SetComment = new JLabel("");
			label_SetComment.setBounds(47, 3, 25, 25);
			panel_Button.add(label_SetComment);
			label_SetComment.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(label_SetComment.isEnabled())
					{
						String populateComment=comments.get(files[imgId].getName());
						if(populateComment==null)
							populateComment="";
						PopUp pp=new PopUp("Enter comment for "+files[imgId].getName(),"comment",populateComment,"Done","Cancel");
						pp.setVisible(true);
						pp.btnNewButton.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent arg0) {
								SharedResources.comments.put(files[imgId].getName(),pp.txtrExceptionOccuredPlease.getText() );	
								ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br>Comment:"+comments.get(files[imgId].getName())+"<br><br>Click image to zoom</html>");

							}
						});
					}
				}
			});
			label_SetComment.setSize(new Dimension(30, 30));

			label_SetComment.setToolTipText("Set comment to this picture");
			try {
				label_SetComment.setIcon(new ImageIcon(ImageIO.read(new File(commentIcon)).getScaledInstance(30,30, java.awt.Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				logError(e,"Exception in Icon loading: Image "+commentIcon+" Not Available");

			}
		}
		lblExit.setEnabled(false);
		viewLoaded=true;
	}
	public void gotoPreviousImage() {
		if(label_Prev!=null &&label_Prev.isEnabled())
		{
			imgId--;
			if(imgId<0)	
			{
				imgId=files.length-1;
			}
			try {
				ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br><br>Click image to zoom</html>");
				ImageLabel.setIcon(new ImageIcon(ImageIO.read(files[imgId]).getScaledInstance(410,250, java.awt.Image.SCALE_SMOOTH)));
				if(comments.get(files[imgId].getName())!=null)
					ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br>Comment:"+comments.get(files[imgId].getName())+"<br><br>Click image to zoom</html>");
			} catch (IOException e) {
				logError(e,"Exception occured while navigating to Previous image");
			}

		}		
	}
	
	public void gotoNextImage() {
		if(label_Next!=null && label_Next.isEnabled())
		{
			imgId++;
			if(imgId>files.length-1)	
			{
				imgId=0;
			}
			try {
				ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br><br>Click image to zoom</html>");
				ImageLabel.setIcon(new ImageIcon(ImageIO.read(files[imgId]).getScaledInstance(410,250, java.awt.Image.SCALE_SMOOTH)));
				if(comments.get(files[imgId].getName())!=null)
					ImageLabel.setToolTipText("<html>Filename : "+files[imgId].getName()+"<br>Comment:"+comments.get(files[imgId].getName())+"<br><br>Click image to zoom</html>");

			} catch (IOException e) {
				logError(e,"Exception occured while navigating to next image");
			}

		}
		
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
