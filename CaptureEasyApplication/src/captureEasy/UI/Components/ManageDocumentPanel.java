package captureEasy.UI.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import captureEasy.Launch.Application;
import captureEasy.Resources.Library;
import captureEasy.Resources.PathsNKeys;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.ToastMsg;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jnativehook.mouse.SwingMouseAdapter;
import javax.swing.JTextField;

public class ManageDocumentPanel extends Library {



	/** currently selected File. */
	public File currentFile;

	/** Main GUI container */
	public JPanel gui;
	public static boolean changed=false;
	/** Directory listing */
	/** Table model for File[]. */
	public FileTableModel fileTableModel;
	public boolean cellSizesSet = false;
	//
	public int rowIconPadding = 8,pointer=-1;
	ToastMsg tm;
	/* File controls. */
	public JButton openFile;
	public JButton printFile;
	public JButton editFile;
	/* File details. */
	public JLabel fileName;
	public JLabel date;
	public JLabel size;
	public JCheckBox readable;
	public JCheckBox writable;
	public JCheckBox executable;
	public JRadioButton isDirectory;
	public JRadioButton isFile;
	public DefaultMutableTreeNode root;
	public File RFile;

	public JPanel DocumentScrollPane;
	//
	public List<String> pathTraverse=new ArrayList<String>();
	public static File[] searchResult=null;
	File files;
	public FileSystemView fileSystemView;
	public Desktop desktop;
	public JTable table;
	public ListSelectionListener listSelectionListener;
	public DefaultTreeModel treeModel;
	public JTree tree;
	public JPanel panel_View;
	public TextField textField;
	public JLabel lblCross;
	public JLabel label_SearchBtn;
	public JLabel label_Kebab;
	public JLabel label_Forword;
	public JLabel label_createFolder;
	public JLabel label_Back;
	private JScrollPane ScrollPane_Table;
	private JScrollPane scrollPane_Tree;
	public TreeSelectionListener treeSelectionListener;
	private JSplitPane splitPane_View;

	public  JTabbedPane TabbledPanel;
	public JPanel panel_Selection;

	public boolean isloaded=false;

	private JPopupMenu popupMenu;

	private JMenuItem menuItemAdd;

	private JMenuItem menuItemRemove;

	private JMenuItem menuItemRemoveAll;
	public static JTextField textField_Path;
	private JLabel label_refesh;
	private JLabel label_refresh;
	public ManageDocumentPanel(JTabbedPane TabbledPanel) 	{
		this.TabbledPanel=TabbledPanel;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			logError(ex,"Exception on UI");
		}
		{
			DocumentScrollPane = new JPanel();
			DocumentScrollPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			DocumentScrollPane.setBackground(Color.WHITE);
			DocumentScrollPane.setSize(new Dimension(437, 315));
			DocumentScrollPane.addMouseListener(new SwingMouseAdapter(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void mousePressed(MouseEvent e) {
					ActionGUI.xxDialog = e.getX();
					ActionGUI.xyDialog = e.getY();
				}
			});
			DocumentScrollPane.addMouseMotionListener(new MouseAdapter(){
				public void mouseDragged(MouseEvent arg0) {
					ActionGUI.xDialog = arg0.getXOnScreen();
					ActionGUI.yDialog = arg0.getYOnScreen();
					ActionGUI.dialog.setLocation(ActionGUI.xDialog - ActionGUI.xxDialog, ActionGUI.yDialog - ActionGUI.xyDialog); 		
					if(tm!=null)
					tm.setLocation(ActionGUI.dialog.getBounds().x+430/2+75,ActionGUI.dialog.getBounds().y+315/2);
				}
			});
			DocumentScrollPane.setLayout(null);
		}

		/*
		loadDocumentsTab(RFile.getAbsolutePath());
		DocumentScrollPane.add(panel_Selection);
		DocumentScrollPane.add(panel_View);
		///*/

	}

	public void loadDocumentsTab(String Path)  throws Exception
	{
		panel_Selection = new JPanel();
		panel_Selection.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_Selection.setBounds(10, 10, 415, 38);
		panel_Selection.setLayout(null);

		lblCross = new JLabel("");
		lblCross.setBounds(385, 8, 20, 20);
		panel_Selection.add(lblCross);

		lblCross.setToolTipText("Close window");
		try {
			lblCross.setIcon(new ImageIcon(ImageIO.read(new File(exitIcon)).getScaledInstance(18,18, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+exitIcon+" Not Available");}

		lblCross.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(lblCross.isEnabled())
				{
					ActionGUI.dialog.dispose();
					ActionGUI.leaveControl=true;
					try{Application.sensor.play();}catch(Exception e){};
				}
			}
		});
		textField_Path = new JTextField(Path);
		textField_Path.setEditable(true);
		textField_Path.setToolTipText(textField_Path.getText());
		textField_Path.setBounds(63, 8, 135, 22);
		panel_Selection.add(textField_Path);
		textField_Path.setColumns(10);
		label_refresh = new JLabel("");
		label_refresh.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent arg0) {
				textField_Path.setToolTipText(textField_Path.getText());
				changed=new File(textField_Path.getText()).exists();
					if(new File(textField_Path.getText()).exists())
					{
						/*textField_Path.setBackground(Color.WHITE);
						DocumentScrollPane.remove(panel_View);
					    loadDocumentsTab(textField_Path.getText());
						DocumentScrollPane.add(panel_View);
						showRootFile();*/
					}
					else
						textField_Path.setBackground(Color.PINK);
			}
		});
		label_refresh.setBounds(200, 8, 20, 22);
		panel_Selection.add(label_refresh);
		try {
			label_refresh.setIcon(new ImageIcon(ImageIO.read(new File(refreshIcon)).getScaledInstance(18,18, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+refreshIcon+" Not Available");
		}

		textField = new TextField();
		textField.setPlaceholder(" Search...");
		textField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setToolTipText("Enter search key");
		textField.setBounds(270, 8, 85, 22);
		panel_Selection.add(textField);
		textField.setColumns(10);
		JPanel panel = new JPanel();
		panel.setBackground(new Color(230, 230, 250));
		panel.setBorder(new MatteBorder(1, 0, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(355, 8, 20, 22);
		panel_Selection.add(panel);
		panel.setLayout(null);

		label_SearchBtn = new JLabel("");
		label_SearchBtn.setBackground(new Color(255, 255, 204));
		label_SearchBtn.addMouseListener(new SwingMouseAdapter() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {
				if(!textField.getText().replaceAll("\\s", "").equals(""))
				{
					if(ActionGUI.dialog.isVisible())
					{
						searchResult=null;
						 tm=new ToastMsg("Searching... ",ActionGUI.dialog.getBounds().x+430/2+75,ActionGUI.dialog.getBounds().y+315/2)
						{
							private static final long serialVersionUID = 1L;
							public void terminationLogic() throws InterruptedException
							{
								do{Thread.sleep(100);}while(ManageDocumentPanel.searchResult==null);
							}
						};
						tm.showToast();
					}
					searchResult=search(textField.getText());
					setTableData(searchResult);
				}
			}
		});
		label_SearchBtn.requestFocusInWindow();
		label_SearchBtn.setBounds(1, 1, 20, 20);
		panel.add(label_SearchBtn);
		label_SearchBtn.setToolTipText("Search ");
		try {
			label_SearchBtn.setIcon(new ImageIcon(ImageIO.read(new File(searchIcon)).getScaledInstance(18,18, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+searchIcon+" Not Available");
		}

		label_Kebab = new JLabel("");
		label_Kebab.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		label_Kebab.addMouseListener(new SwingMouseAdapter(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			//public void mouseClicked()
			{
				
			}
		});
		label_Kebab.setToolTipText("Click here to expand menu");
		label_Kebab.setBounds(248, 9, 20, 20);
		panel_Selection.add(label_Kebab);

		try {
			label_Kebab.setIcon(new ImageIcon(ImageIO.read(new File(kebabIcon)).getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+kebabIcon+" Not Available");

		}

		label_createFolder = new JLabel("");
		label_createFolder.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		label_createFolder.setToolTipText("Create a new folder");
		label_createFolder.setBounds(225, 9, 20, 20);
		panel_Selection.add(label_createFolder);
		try {
			label_createFolder.setIcon(new ImageIcon(ImageIO.read(new File(createfolderIcon)).getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));

		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+createfolderIcon+" Not Available");

		}

		label_Back = new JLabel("");
		label_Back.setEnabled(false);
		label_Back.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {
				//pathTraverse
				 if (pointer > 0) {
						label_Forword.setEnabled(true);
					 	pointer--;
			            String path = pathTraverse.get(pointer);
			            System.out.println(path + " Switch");
			            setTableData(new File(path).listFiles());
			            textField_Path.setText(path);
			            panel_View.revalidate();
			            panel_View.repaint();
			        }
				 else
				 {
						label_Back.setEnabled(false);
				 }
			}
		});
		label_Back.setToolTipText("Back");
		label_Back.setBounds(9, 9, 20, 20);
		panel_Selection.add(label_Back);
		try {
			label_Back.setIcon(new ImageIcon(ImageIO.read(new File(leftarrowIcon)).getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+leftarrowIcon+" Not Available");

		}
		
		label_Forword = new JLabel("");
		label_Forword.setEnabled(false);
		label_Forword.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (pointer < pathTraverse.size()) {
				 	pointer++;
		            String path = pathTraverse.get(pointer);
		            System.out.println(path + " Switch");
		            setTableData(new File(path).listFiles());
		            textField_Path.setText(path);
		            panel_View.revalidate();
		            panel_View.repaint();
		        }
			 else
			 {
					label_Forword.setEnabled(false);
			 }
			}
		});
		label_Forword.setToolTipText("Forword");
		label_Forword.setBounds(37, 9, 20, 20);
		try {
			label_Forword.setIcon(new ImageIcon(ImageIO.read(new File(rightarrowIcon)).getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+rightarrowIcon+" Not Available");
		}
		panel_Selection.add(label_Forword);
		
		label_refesh = new JLabel("");
		label_refesh.setBackground(Color.WHITE);
		label_refesh.setBounds(200, 8, 20, 22);
		try {
			label_refesh.setIcon(new ImageIcon(ImageIO.read(new File(refreshIcon)).getScaledInstance(20,22, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+refreshIcon+" Not Available");
		panel_Selection.add(label_refesh);
		}
		try{
			panel_View = new JPanel();
			panel_View.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			panel_View.setBounds(10, 55, 415, 250);
			fileSystemView = FileSystemView.getFileSystemView();
			desktop = Desktop.getDesktop();


			// the File tree
			root = new DefaultMutableTreeNode();
			treeModel = new DefaultTreeModel(root);

			treeSelectionListener = new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent tse){

					DefaultMutableTreeNode node =(DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
					showChildren(node);
				}
			};

			String DocUMENTpATH=textField_Path.getText();
			if(DocUMENTpATH==null || "".equals(DocUMENTpATH.replaceAll("\\s", "")) && !new File(DocUMENTpATH).exists())
			{
				throw new Exception();
			}
			RFile=new File (DocUMENTpATH);

			// show the file system roots.
			File[] roots =new SingleRootFileSystemView(RFile).getRoots();
			for (File fileSystemRoot : roots) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
				root.add( node );
				File[] files = fileSystemView.getFiles(fileSystemRoot, true);
				for (File file : files) {
					if (file.isDirectory()) {
						node.add(new DefaultMutableTreeNode(file));
						//System.out.println("Adding "+file.getPath());
					}
				}
				//
			}
			panel_View.setLayout(null);

			splitPane_View = new JSplitPane();
			splitPane_View.setBounds(5, 5, 405, 240);
			splitPane_View.setDividerLocation(150);
			panel_View.add(splitPane_View);
			scrollPane_Tree = new JScrollPane();
			splitPane_View.setLeftComponent(scrollPane_Tree);

			tree = new JTree(treeModel);
			tree.setRootVisible(false);
			scrollPane_Tree.setViewportView(tree);
			tree.setShowsRootHandles(true);
			tree.setVisibleRowCount(15);
			tree.setRowHeight(20);
			tree.setFont(new Font("Tahoma", Font.PLAIN, 16));
			tree.setLayout(null);
			//		Dimension d = ScrollPane_Table.getPreferredSize();
			tree.addTreeSelectionListener(treeSelectionListener);
			tree.setCellRenderer(new FileTreeCellRenderer());
			tree.expandRow(0);


			table = new JTable();
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table.setAutoCreateRowSorter(true);
			table.setShowVerticalLines(false);
			popupMenu = new JPopupMenu();
	        menuItemAdd = new JMenuItem("Add New Row");
	        menuItemRemove = new JMenuItem("Remove Current Row");
	        menuItemRemoveAll = new JMenuItem("Remove All Rows");
	         
	        /*menuItemAdd.addActionListener(this);
	        menuItemRemove.addActionListener(this);
	        menuItemRemoveAll.addActionListener(this);*/
	         
	        popupMenu.add(menuItemAdd);
	        popupMenu.add(menuItemRemove);
	        popupMenu.add(menuItemRemoveAll);

			table.setComponentPopupMenu(popupMenu);
			table.addMouseListener(new TableMouseListener(table));
			table.getSelectionModel().addListSelectionListener(listSelectionListener);
			ScrollPane_Table = new JScrollPane(table);
			splitPane_View.setRightComponent(ScrollPane_Table);
			listSelectionListener = new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {

					//System.out.println( row );
				}
			};
			table.addMouseListener(new SwingMouseAdapter() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void mouseClicked(MouseEvent e) {
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					if (e.getClickCount() == 1 && column==2) {
						try {
							desktop.open(((FileTableModel)table.getModel()).getFile(row));
						} catch (IOException e1) {
							PopUp p=new PopUp("ERROR","error",e1.getMessage().replace(((FileTableModel)table.getModel()).getFile(row).getPath(), ""),"Okay","");
							p.setVisible(true);
							PopUp.PopDia=p;
							logError(e1,"Exception occured while opening file "+((FileTableModel)table.getModel()).getFile(row));
						}
					}
					else if (e.getClickCount() == 2 && column==1) {
						try {
							desktop.open(((FileTableModel)table.getModel()).getFile(row));
						} catch (IOException e1) {
							PopUp p=new PopUp("ERROR","error",e1.getMessage().replace(((FileTableModel)table.getModel()).getFile(row).getPath(), ""),"Okay","");
							p.setVisible(true);
							PopUp.PopDia=p;
							logError(e1,"Exception occured while opening file "+((FileTableModel)table.getModel()).getFile(row));						}
					}
					else if (e.getClickCount() == 1 && column==1) {
						if(((FileTableModel)table.getModel()).getFile(table.getSelectionModel().getLeadSelectionIndex()).isDirectory())
						{
							setTableData(((FileTableModel)table.getModel()).getFile(table.getSelectionModel().getLeadSelectionIndex()).listFiles());
							label_Back.setEnabled(true);
				            textField_Path.setText(((FileTableModel)table.getModel()).getFile(table.getSelectionModel().getLeadSelectionIndex()).getAbsolutePath());
				           /* for(int i=pointer;i<pathTraverse.size();i++)
				            	pathTraverse.remove(i);*/
				            pathTraverse.add(((FileTableModel)table.getModel()).getFile(table.getSelectionModel().getLeadSelectionIndex()).getAbsolutePath());
							pointer++;
						}
					}
				}
			});
		}catch(Exception e)
		{
			TabbledPanel.setSelectedIndex(TabbledPanel.getTabCount()-1);
		}
		isloaded=true;
		//lblCross.setEnabled(false);
	}





	public void showRootFile() {
		// ensure the main files are displayed
		try{tree.setSelectionInterval(0,0);}catch(Exception e){}
	}

	public TreePath findTreePath(File find) {
		for (int ii=0; ii<tree.getRowCount(); ii++) {
			TreePath treePath = tree.getPathForRow(ii);
			Object object = treePath.getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;
			File nodeFile = (File)node.getUserObject();
			if (nodeFile==find) {
				return treePath;
			}
		}
		return null;
	}
	static List<File> fileList= new ArrayList<File>();
	public File[] search(String key)
	{
		List<File> result= new ArrayList<File>();
		try {
			fileList.clear();
			fileList(RFile);
			for(File file:fileList)
			{
				if(file.getName().toLowerCase().contains(key.toLowerCase()))
				{
					result.add(file);
					System.out.println(file.getAbsolutePath());
				}
			}
			return result.toArray(new File[result.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void fileList(File file) throws IOException
	{
		File[] files= file.listFiles();
		for(File f:files)
		{
			if(f.isDirectory())
			{
				fileList.add(f);
				fileList(f);
			}
			else
			{
				fileList.add(f);
			}
		}	
	} 



	public void showThrowable(Throwable t) {
		t.printStackTrace();
		JOptionPane.showMessageDialog(
				panel_View,
				t.toString(),
				t.getMessage(),
				JOptionPane.ERROR_MESSAGE
				);
		panel_View.repaint();
	}


	/** Add the files that are contained within the directory of this node.
    Thanks to Hovercraft Full Of Eels for the SwingWorker fix. */
	public void showChildren(final DefaultMutableTreeNode node) {
		tree.setEnabled(false);
		//System.out.println(" Showing children " +node);
		SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
			@Override
			public Void doInBackground() {
				File file = (File) node.getUserObject();
				System.err.println("Path="+file.getPath());
				if (file.isDirectory()) {
					File[] files = fileSystemView.getFiles(file, true); //!!
					if (node.isLeaf()) {
						for (File child : files) {
							if (child.isDirectory()) {
								publish(child);
							}
						}
					}
					setTableData(files);

					label_Back.setEnabled(true);
					textField_Path.setText(file.getAbsolutePath());
					pathTraverse.add(file.getAbsolutePath());
					/*for(int i=pointer;i<pathTraverse.size();i++)
		            	pathTraverse.remove(i);*/
					pointer++;
				}
				return null;
			}

			
			@Override
			protected void process(List<File> chunks) {
				for (File child : chunks) {
					node.add(new DefaultMutableTreeNode(child));
				}
			}

			@Override
			protected void done() {

				tree.setEnabled(true);

			}
		};
		worker.execute();


	}
	/** Update the table on the EDT */
	public void setTableData(final File[] files) {
		//System.out.println("Setting table Data");
		//	StackInfo info=new StackInfo(Thread.currentThread().getStackTrace());
		////System.out.println(info.getCallSequence());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				fileTableModel = new FileTableModel();
				table.setModel(fileTableModel);
				table.getSelectionModel().removeListSelectionListener(listSelectionListener);
				fileTableModel.setFiles(files);
				table.getSelectionModel().addListSelectionListener(listSelectionListener);
				if (files.length>0) {
					Icon icon = fileSystemView.getSystemIcon(files[0]);
					table.setRowHeight( icon.getIconHeight()+rowIconPadding );
					cellSizesSet = true;
				}
				setJTableColumnsWidth(table,200,10,50,10);

			}
		});
	}
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
			double... percentages) {

		double total = 0;
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			total += percentages[i];
		}

		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth((int)
					(tablePreferredWidth * (percentages[i] / total)));
		}
	}
	public void setColumnWidth(int column, int width) {
		TableColumn tableColumn = table.getColumnModel().getColumn(column);
		if (width<0) {
			//System.out.println((String)tableColumn.getHeaderValue());
			// use the preferred width of the header..
			JLabel label = new JLabel( (String)tableColumn.getHeaderValue() );
			Dimension preferred = label.getPreferredSize();
			// altered 10->14 as per camickr comment.
			width = (int)preferred.getWidth()+14;
		}
		tableColumn.setPreferredWidth(width);
		tableColumn.setMaxWidth(width);
		tableColumn.setMinWidth(width);
	}
}

/** A TableModel to hold File[]. */
class FileTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public File[] files;
	public FileSystemView fileSystemView = FileSystemView.getFileSystemView();
	public String[] columns = {
			"Icon",
			"File Name",
			"Open"
	};

	FileTableModel() {
		this(new File[0]);
	}

	FileTableModel(File[] files) {
		this.files = files;
	}
	public Object getValueAt(int row, int column) {
		//StackInfo s=new StackInfo(Thread.currentThread().getStackTrace());
		//System.out.println(row);
		////System.out.println(s.getCallSequence());
		File file = files[row];

		switch (column) {
		case 0:
			return fileSystemView.getSystemIcon(file);
		case 1:
			return fileSystemView.getSystemDisplayName(file);	
		case 2:
			try {
				return new ImageIcon(ImageIO.read(new File (PathsNKeys.openIcon)).getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
			} catch (IOException e) {
				Library.logError(e,"Exception in Icon loading: Image "+PathsNKeys.openIcon+" Not Available");
			}
		default:
			//System.out.println("Logic Error "+row+"   "+column);
		}
		return "";
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Class<?> getColumnClass(int column) {
		//System.out.println(getColumnCount()+"    "+column);
		switch (column) {
		case 0:
			return ImageIcon.class;
		case 1:
			return String.class;
		case 2:
			return ImageIcon.class;
		} 
		return String.class;
	}

	public String getColumnName(int column) {
		return columns[column];
	}

	public int getRowCount() {
		return files.length;
	}

	public File getFile(int row) {
		return files[row];
	}

	public void setFiles(File[] files) {
		this.files = files;
		fireTableDataChanged();
	}
}



/** A TreeCellRenderer for a File. */
class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	public FileSystemView fileSystemView;

	public JLabel label;

	FileTreeCellRenderer() {
		label = new JLabel();
		label.setOpaque(true);
		fileSystemView = FileSystemView.getFileSystemView();
	}

	@Override
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean selected,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		File file = (File)node.getUserObject();
		label.setIcon(fileSystemView.getSystemIcon(file));


		label.setText(fileSystemView.getSystemDisplayName(file));
		if (selected) {
			label.setBackground(backgroundSelectionColor);
			label.setForeground(textSelectionColor);
		} else {
			label.setBackground(backgroundNonSelectionColor);
			label.setForeground(textNonSelectionColor);
		}
		return label;
	}

}
class TableMouseListener extends SwingMouseAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	public TableMouseListener(JTable table) {
		this.table = table;
	}
	@Override
	public void mousePressed(MouseEvent event) {
		Point point = event.getPoint();
		int currentRow = table.rowAtPoint(point);
		table.setRowSelectionInterval(currentRow, currentRow);
	}
}