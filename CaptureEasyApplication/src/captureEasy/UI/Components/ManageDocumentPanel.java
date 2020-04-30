package captureEasy.UI.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

import captureEasy.UI.ActionGUI;
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

import java.awt.event.MouseAdapter;

public class ManageDocumentPanel extends Library implements MouseListener,MouseMotionListener{



	/** currently selected File. */
	public File currentFile;

	/** Main GUI container */
	public JPanel gui;

	/** Directory listing */
	/** Table model for File[]. */
	public FileTableModel fileTableModel;
	public boolean cellSizesSet = false;
	public int rowIconPadding = 8;

	/* File controls. */
	public JButton openFile;
	public JButton printFile;
	public JButton editFile;

	/* File details. */
	public JLabel fileName;
	public JTextField path;
	public JLabel date;
	public JLabel size;
	public JCheckBox readable;
	public JCheckBox writable;
	public JCheckBox executable;
	public JRadioButton isDirectory;
	public JRadioButton isFile;
	public DefaultMutableTreeNode root;


	public JPanel DocumentScrollPane;
	public static List<String> monthList=new ArrayList<String>();
	public static List<String> dayList=new ArrayList<String>();
	public static ArrayList<ArrayList<File>> Filelist=new ArrayList<ArrayList<File>>();
	File files;
	public FileSystemView fileSystemView;
	public Desktop desktop;
	public JTable table;
	public ListSelectionListener listSelectionListener;
	public DefaultTreeModel treeModel;
	public JTree tree;
	public JPanel panel_View;
	public JTextField textField;
	public JLabel lblCross;
	public JLabel label_SearchBtn;
	public JLabel label_delete;
	public JLabel label_rename;
	public JLabel label_createFolder;
	public JLabel label_Export;
	private JScrollPane ScrollPane_Table;
	private JScrollPane scrollPane_Tree;
	public TreeSelectionListener treeSelectionListener;
	private JSplitPane splitPane_View;

	public  JTabbedPane TabbledPanel;
	public JPanel panel_Selection;
	public ManageDocumentPanel(JTabbedPane TabbledPanel)
	{
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
			DocumentScrollPane.addMouseListener(this);
			DocumentScrollPane.addMouseMotionListener(this);
			DocumentScrollPane.setLayout(null);

			panel_Selection = new JPanel();
			panel_Selection.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			panel_Selection.setBounds(10, 10, 415, 40);
			panel_Selection.setLayout(null);

			lblCross = new JLabel("");
			lblCross.setBounds(385, 8, 20, 20);
			panel_Selection.add(lblCross);

			lblCross.setToolTipText("Close window");
			try {
				lblCross.setIcon(new ImageIcon(ImageIO.read(new File(exitIcon)).getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));
			} catch (IOException e1) {
				logError(e1,"Exception in Icon loading: Image "+exitIcon+" Not Available");}
		}
		lblCross.addMouseListener(new MouseAdapter() {
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

		label_SearchBtn = new JLabel("");
		label_SearchBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		label_SearchBtn.setToolTipText("Search ");
		label_SearchBtn.setBounds(340, 8, 25, 25);
		panel_Selection.add(label_SearchBtn);
		try {
			label_SearchBtn.setIcon(new ImageIcon(ImageIO.read(new File(searchIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+searchIcon+" Not Available");
		}
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setToolTipText("Enter search key");
		textField.setBounds(235, 7, 100, 22);
		panel_Selection.add(textField);
		textField.setColumns(10);

		label_delete = new JLabel("");
		label_delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		label_delete.setToolTipText("Delete selected file");
		label_delete.setBounds(195, 7, 25, 25);
		panel_Selection.add(label_delete);
		try {
			label_delete.setIcon(new ImageIcon(ImageIO.read(new File(deleteIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+deleteIcon+" Not Available");

		}

		label_rename = new JLabel("");
		label_rename.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		label_rename.setToolTipText("Rename selected file");
		label_rename.setBounds(160, 7, 25, 25);
		try {
			label_rename.setIcon(new ImageIcon(ImageIO.read(new File(renameIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+renameIcon+" Not Available");
		}
		panel_Selection.add(label_rename);

		label_createFolder = new JLabel("");
		label_createFolder.setToolTipText("Create a new folder");
		label_createFolder.setBounds(125, 7, 25, 25);
		panel_Selection.add(label_createFolder);
		try {
			label_createFolder.setIcon(new ImageIcon(ImageIO.read(new File(createfolderIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));

		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+createfolderIcon+" Not Available");

		}

		label_Export = new JLabel("");
		label_Export.setToolTipText("Export selected files to folder");
		label_Export.setBounds(90, 7, 25, 25);
		panel_Selection.add(label_Export);
		try {
			label_Export.setIcon(new ImageIcon(ImageIO.read(new File(exportIcon)).getScaledInstance(25,25, java.awt.Image.SCALE_SMOOTH)));

		} catch (IOException e1) {
			logError(e1,"Exception in Icon loading: Image "+exportIcon+" Not Available");

		}

	}

	public void loadDocumentsTab() 
	{
		try{
			panel_View = new JPanel();
			panel_View.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			panel_View.setBounds(10, 57, 415, 245);
			fileSystemView = FileSystemView.getFileSystemView();
			desktop = Desktop.getDesktop();


			// the File tree
			root = new DefaultMutableTreeNode();
			treeModel = new DefaultTreeModel(root);

			treeSelectionListener = new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent tse){

					DefaultMutableTreeNode node =(DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
					showChildren(node);
					System.out.println((File)node.getUserObject());
				}
			};

			String DocUMENTpATH=property.getString("DocPath");
			if(DocUMENTpATH==null || "".equals(DocUMENTpATH.replaceAll("\\s", "")))
			{
				throw new Exception();
			}
			File RFile=new File (DocUMENTpATH);

			// show the file system roots.
			File[] roots =new SingleRootFileSystemView(RFile).getRoots();
			for (File fileSystemRoot : roots) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
				root.add( node );
				File[] files = fileSystemView.getFiles(fileSystemRoot, true);
				for (File file : files) {
					if (file.isDirectory()) {
						node.add(new DefaultMutableTreeNode(file));
						System.out.println("Adding "+file.getPath());
					}
				}
				//
			}
			panel_View.setLayout(null);

			splitPane_View = new JSplitPane();
			splitPane_View.setBounds(5, 5, 405, 235);
			splitPane_View.setDividerLocation(175);
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
			table.getSelectionModel().addListSelectionListener(listSelectionListener);
			ScrollPane_Table = new JScrollPane(table);
			splitPane_View.setRightComponent(ScrollPane_Table);
			listSelectionListener = new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					//int row = table.getSelectionModel().getLeadSelectionIndex();

					//System.out.println(  );
				}
			};
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					if (e.getClickCount() == 1 && column==2) {
						try {
							desktop.open(((FileTableModel)table.getModel()).getFile(row));
						} catch (IOException e1) {
							logError(e1,"Exception occured while opening file "+((FileTableModel)table.getModel()).getFile(row));

						}
					}
					else if (e.getClickCount() == 2 && column==1) {
						try {
							desktop.open(((FileTableModel)table.getModel()).getFile(row));
						} catch (IOException e1) {
							logError(e1,"Exception occured while opening file "+((FileTableModel)table.getModel()).getFile(row));
						}
					}
				}
			});
		}catch(Exception e)
		{
			TabbledPanel.setSelectedIndex(TabbledPanel.getTabCount()-1);
		}

		//lblCross.setEnabled(false);
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
		// not found!
		return null;
	}

	public void showErrorMessage(String errorMessage, String errorTitle) {
		JOptionPane.showMessageDialog(
				panel_View,
				errorMessage,
				errorTitle,
				JOptionPane.ERROR_MESSAGE
				);
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
		System.out.println(" Showing children " +node);
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
		System.out.println("Setting table Data");
		//	StackInfo info=new StackInfo(Thread.currentThread().getStackTrace());
		//System.out.println(info.getCallSequence());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				fileTableModel = new FileTableModel();
				table.setModel(fileTableModel);
				table.getSelectionModel().removeListSelectionListener(listSelectionListener);
				fileTableModel.setFiles(files);
				table.getSelectionModel().addListSelectionListener(listSelectionListener);
				if (!cellSizesSet) {
					Icon icon = fileSystemView.getSystemIcon(files[0]);

					// size adjustment to better account for CaptureEasy\\Resources\\Icons
					table.setRowHeight( icon.getIconHeight()+rowIconPadding );
					/*table.getColumnModel().getColumn(0).setWidth(10);
					table.getColumnModel().getColumn(1).setWidth(50);
					table.getColumnModel().getColumn(2).setWidth(10);*/
					/*setColumnWidth(0,-1);
					setColumnWidth(1,-1);
					setColumnWidth(2,-1);*/
					setJTableColumnsWidth(table,200,10,50,10);
					cellSizesSet = true;
				}
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
			System.out.println((String)tableColumn.getHeaderValue());
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
		System.out.println(row);
		//System.out.println(s.getCallSequence());
		File file = files[row];

		switch (column) {
		case 0:
			return fileSystemView.getSystemIcon(file);
		case 1:
			return fileSystemView.getSystemDisplayName(file);	
		case 2:
			try {
				return new ImageIcon(ImageIO.read(new File("C:/Users/USER/Desktop/Icons/open.png")).getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
			} catch (IOException e) {
			}
		default:
			System.out.println("Logic Error "+row+"   "+column);
		}
		return "";
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Class<?> getColumnClass(int column) {
		System.out.println(getColumnCount()+"    "+column);
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
		//label.setToolTipText(file.getPath());

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