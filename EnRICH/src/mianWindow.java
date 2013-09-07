/*  <EnRICH qualitatively integrate heterogeneous data sets while filtering each of them based on its attributes.>
    Copyright (C) <2012>  <Xia Zhang>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractButton;
import javax.swing.DebugGraphics;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.RepaintManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class mianWindow {

	private JFrame frmIrankBetaVersion; //frame of the main window
	private JTable table=new JTable();
	private JTable table2=new JTable();
	private DefaultListModel fileListModel=new DefaultListModel(); //data model associated with input file list
	private DefaultListModel fileListModel2=new DefaultListModel();//data model associated with input file check list 
	private boolean isNetwork;// indicate data type
	private boolean aplFilter; //indicate whether to apply filters
	private DefaultTableModel fileModel=new DefaultTableModel();//data model associated with the table that shows the selected file;
	private DefaultTableModel filterModel=new FilterTableModel();//data model associated with the table where filters are specified for  the selected file;
	private HashMap <String, Vector<Vector<String>>> fileFilterTable=new HashMap<String, Vector<Vector<String>>>();
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mianWindow window = new mianWindow();
					window.frmIrankBetaVersion.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mianWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIrankBetaVersion = new JFrame();
		frmIrankBetaVersion.setTitle("EnRICH Beta Version");
		frmIrankBetaVersion.setBounds(100, 100, 900, 700);
		frmIrankBetaVersion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon=new ImageIcon("../imageIcons/logo.jpg");
		frmIrankBetaVersion.setIconImage(icon.getImage());
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmIrankBetaVersion.getContentPane().setLayout(gridBagLayout);
		
		
		
		JLabel lblGettingStarted = new JLabel("1. Each file contains ONE network/list");
		GridBagConstraints gbc_lblGettingStarted = new GridBagConstraints();
		gbc_lblGettingStarted.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGettingStarted.gridwidth = 2;
		gbc_lblGettingStarted.insets = new Insets(0, 0, 5, 5);
		gbc_lblGettingStarted.gridx = 0;
		gbc_lblGettingStarted.gridy = 0;
		frmIrankBetaVersion.getContentPane().add(lblGettingStarted, gbc_lblGettingStarted);
	
		//step1: "open file or directory" button
        JButton btnOpen = new JButton("Open File or Directory"); 
        btnOpen.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent e) {
        		openButtonAction(e);
        		
        	}

        	private void openButtonAction(ActionEvent e) {
        		// TODO Auto-generated method stub
        		
        		JFileChooser openchooser = new JFileChooser();
        		//by default, JFileChooser can select only files
        		openchooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        		//only one single file is selected at a time
        		openchooser.setMultiSelectionEnabled(false);
        		//only text files are visible
        		openchooser.setFileFilter(new FileNameExtensionFilter("text files", "txt"));
        		openchooser.setCurrentDirectory(new File("."));
        		int indicator =openchooser.showOpenDialog(frmIrankBetaVersion);
        		if (indicator==JFileChooser.APPROVE_OPTION)
        		{
        			File f= openchooser.getSelectedFile(); //return file;
        			
        			if (f.isFile())
        			{   //add input file into list data model
        				fileListModel.addElement(f.getPath());
        				fileListModel2.addElement(new CheckListItem(f.getPath()));
        			
        			}
        			else {
        				File [] inFiles=f.listFiles(new textFileFilter()); //return string[];
        			    //add all text files in the input directory into list data model
        				for (int i=0; i<inFiles.length; i++)
        				{
        				fileListModel.addElement(inFiles[i].getPath());
        				fileListModel2.addElement(new CheckListItem(inFiles[i].getPath()));
        				}
        			}
        		}//openButtonAction(ActionEvent e) ends 
        	}}); //open button action listener ends
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
        gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut.gridx = 2;
        gbc_horizontalStrut.gridy = 0;
        frmIrankBetaVersion.getContentPane().add(horizontalStrut, gbc_horizontalStrut);
        GridBagConstraints gbc_btnOpen = new GridBagConstraints();
        gbc_btnOpen.gridwidth = 2;
        gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnOpen.insets = new Insets(0, 0, 5, 5);
        gbc_btnOpen.gridx = 0;
        gbc_btnOpen.gridy = 1;
        frmIrankBetaVersion.getContentPane().add(btnOpen, gbc_btnOpen);
        
       //step1: create input file list and associate it with its list model
        final JList list_1 = new JList(fileListModel); 
        list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollpane=new JScrollPane(list_1);
		GridBagConstraints gbc_list_1 = new GridBagConstraints();
		gbc_list_1.gridwidth = 2;
		gbc_list_1.gridheight = 4;
		gbc_list_1.insets = new Insets(0, 0, 5, 5);
		gbc_list_1.fill = GridBagConstraints.BOTH;
		gbc_list_1.gridx = 0;
		gbc_list_1.gridy = 2;
		frmIrankBetaVersion.getContentPane().add(scrollpane, gbc_list_1);
		
		
		//step1: "remove the selected" button, its listener interacts with two list data models
		JButton btnRemoveTheSelected = new JButton("Remove the Selected");
		btnRemoveTheSelected.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int sid = 0;
				// TODO Auto-generated method stub
				if(list_1.getSelectedValue() != null)
					sid=list_1.getSelectedIndex();
				fileListModel2.remove(sid);
				fileListModel.removeElement(list_1.getSelectedValue());
				
			}});
		GridBagConstraints gbc_btnRemoveTheSlected = new GridBagConstraints();
		gbc_btnRemoveTheSlected.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveTheSlected.gridwidth = 2;
		gbc_btnRemoveTheSlected.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveTheSlected.gridx = 0;
		gbc_btnRemoveTheSlected.gridy = 6;
		frmIrankBetaVersion.getContentPane().add(btnRemoveTheSelected, gbc_btnRemoveTheSlected);
		
	    //step1: clear all files button, its listener interacts with two list data models
		JButton btnClearAll = new JButton("Clear All Files");
		btnClearAll.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileListModel.removeAllElements();
				fileListModel2.removeAllElements();
				//fileModel.getDataVector().removeAllElements();
				//fileModel.fireTableDataChanged();
				for (int i=table.getColumnCount()-1; i>=0;i--){
					TableColumn column=table.getColumnModel().getColumn(i);
					table.getColumnModel().removeColumn(column);
				}
				for (int i=table2.getColumnCount()-1; i>=0;i--){
					TableColumn column=table2.getColumnModel().getColumn(i);
					table2.getColumnModel().removeColumn(column);
				}
				
			}});
		GridBagConstraints gbc_btnClearAll = new GridBagConstraints();
		gbc_btnClearAll.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClearAll.gridwidth = 2;
		gbc_btnClearAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearAll.gridx = 0;
		gbc_btnClearAll.gridy = 7;
		frmIrankBetaVersion.getContentPane().add(btnClearAll, gbc_btnClearAll);
		
		JLabel lblBorderLabel = new JLabel("******************************Customize your Integration Pool by Selecting Files and Indicating Running Preferences******************************");
		GridBagConstraints gbc_lblBorderLabel = new GridBagConstraints();
		gbc_lblBorderLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblBorderLabel.gridx = 0;
		gbc_lblBorderLabel.gridy = 8;
		gbc_lblBorderLabel.gridwidth=8;
		frmIrankBetaVersion.getContentPane().add(lblBorderLabel, gbc_lblBorderLabel);
		
		//step2: label
        JLabel lblTheSelected = new JLabel("2.Show the selected file (in the left box)"); 
        GridBagConstraints gbc_lblTheSelected = new GridBagConstraints();
        gbc_lblTheSelected.gridwidth = 2;
        gbc_lblTheSelected.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblTheSelected.insets = new Insets(0, 0, 5, 5);
        gbc_lblTheSelected.gridx = 3;
        gbc_lblTheSelected.gridy = 0;
        frmIrankBetaVersion.getContentPane().add(lblTheSelected, gbc_lblTheSelected);
        
      //step2:associate this table with its data model
		table = new JTable(fileModel); 
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnSelectionAllowed(true);
	    JScrollPane scrollpane3=new JScrollPane(table);
		scrollpane3.getViewport().setOpaque(false);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridheight = 2;
		gbc_table.gridwidth = 5;
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 3;
		gbc_table.gridy = 1;
		frmIrankBetaVersion.getContentPane().add(scrollpane3, gbc_table);
		//MAKE THE DRAWING SLOWER TO DEBUG
		//DebugGraphics.setFlashTime(100);
		//RepaintManager.currentManager(table).setDoubleBufferingEnabled(false);
	   // table.setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);
        
        //step2: show button
        JButton btnShow = new JButton("Show"); 
        btnShow.addActionListener(new ActionListener(){

        	@Override
        	public void actionPerformed(ActionEvent e) {
        		// TODO Auto-generated method stub
        		showSelectedFile();
        	}

        	private void showSelectedFile() {
        		Vector<String> colNames;
        		Vector<Vector<String>> rr;
        		    rr=showSelectedTable(list_1.getSelectedValue().toString());//return data table
        		    colNames=showCol(list_1.getSelectedValue().toString()); //return column names of this data table
        		    fileModel.setDataVector(rr, colNames);	//put the whole data table into table file model
        	}

        	// return the data table (excluding column names) of a file as a vector of vectors(rows)
        	private Vector<Vector<String>> showSelectedTable(String fn) {//fn:pathname string
        		// TODO Auto-generated method stub
        		Scanner in;
        		Vector<String> r;
        		Vector<Vector<String>> rr = new Vector<Vector<String>>();
        		try {
        			in = new Scanner (new File(fn));
        			if (in.hasNextLine()) {//omit the column names
        				String header = in.nextLine();
        			} 
        			while(in.hasNextLine()){
        				String line = in.nextLine().trim();
        				if (line.isEmpty()){
        					continue;
        				}else{
        				String[] tokens = line.split("\\t");
        			
        				r=new Vector <String> ();
        				for (int i=0; i<tokens.length; i++){
        					r.add(tokens[i]);
        				}
        				rr.add(r);						}     
        			}
        		} catch (FileNotFoundException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		return rr;
        	}});
        GridBagConstraints gbc_btnShow = new GridBagConstraints();
        gbc_btnShow.insets = new Insets(0, 0, 5, 5);
        gbc_btnShow.gridx = 6;
        gbc_btnShow.gridy = 0;
        frmIrankBetaVersion.getContentPane().add(btnShow, gbc_btnShow);
        
        //step2: save button, update table file model and re-write this file being edited
        JButton btnSave = new JButton("Save"); 
        btnSave.addActionListener(new ActionListener(){

        	@Override
        	public void actionPerformed(ActionEvent arg0) {
        	// TODO Auto-generated method stub
        				
        		//update table file model 
        		if(table.isEditing()){ 
        			int   row   =   table.getEditingRow(); 
        			int   col   =   table.getEditingColumn(); 
        			table.getCellEditor(row,col).stopCellEditing(); 
        		 }
        				
        		//re-write the selected file being edited.
        		try {
        			 FileWriter out = new FileWriter(list_1.getSelectedValue().toString());
        			 PrintWriter outprint=new PrintWriter(out);
        			 int i=0;
        			 for(i=0; i<fileModel.getColumnCount()-1;i++) 
        			 {
        				outprint.printf("%s\t",fileModel.getColumnName(i));
        			 }
        					   
        			 outprint.printf("%s\n", fileModel.getColumnName(fileModel.getColumnCount()-1));
        			 for (int j=0; j<fileModel.getRowCount(); j++)
        			 {
        				 int k;
        				
        				 for (k=0; k<fileModel.getColumnCount()-1; k++)
        				 {
        						 outprint.printf("%s\t", fileModel.getValueAt(j, k)); 
        				 }
        				
        				outprint.printf("%s\n", fileModel.getValueAt(j, fileModel.getColumnCount()-1));
        			 }
        					 
        					   
        			 outprint.close();
        			} catch (IOException e) {
        			// TODO Auto-generated catch block
        					e.printStackTrace();
        			}  				
        	}});
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.insets = new Insets(0, 0, 5, 0);
        gbc_btnSave.gridx = 7;
        gbc_btnSave.gridy = 0;
        frmIrankBetaVersion.getContentPane().add(btnSave, gbc_btnSave);
        		
        	
        		
        		
        // step3:label
        JLabel lblFilterOf = new JLabel("3.Specify filters for the selected file (in the left box)");
        GridBagConstraints gbc_lblFilterOf = new GridBagConstraints();
        gbc_lblFilterOf.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblFilterOf.gridwidth = 2;
        gbc_lblFilterOf.insets = new Insets(0, 0, 5, 5);
        gbc_lblFilterOf.gridx = 3;
        gbc_lblFilterOf.gridy = 3;
        frmIrankBetaVersion.getContentPane().add(lblFilterOf, gbc_lblFilterOf);
        		
        // step3: associate this table with its data model
        table2 = new JTable(filterModel);
        table2.setColumnSelectionAllowed(true);
        JScrollPane scrollpane4=new JScrollPane(table2);
        GridBagConstraints gbc_table2 = new GridBagConstraints();
        gbc_table2.gridwidth = 5;
        gbc_table2.gridheight=4;
        gbc_table2.insets = new Insets(0, 0, 5, 0);
        gbc_table2.fill = GridBagConstraints.BOTH;
        gbc_table2.gridx = 3;
        gbc_table2.gridy = 4;
        frmIrankBetaVersion.getContentPane().add(scrollpane4, gbc_table2);
				
	    //set up table editor,  JComboBox is one of the default table editor classes
        final JComboBox comboBox = new JComboBox();
        		comboBox.addItem("List Element");//case0
        		comboBox.addItem("Edge Undirected Node");//case1
        		comboBox.addItem("Edge From Node");//case2
        		comboBox.addItem("Edge To Node");// case3
        		comboBox.addItem("Label Attribute__CONTAINS");//case4
        		comboBox.addItem("Label Attribute__EQUALS TO"); //case5
        		comboBox.addItem("Value Attribute__>");//case6
        		comboBox.addItem("Value Attribute__<");//case7
        		comboBox.addItem("Value Attribute__=");//case8
        		comboBox.addItem("Value Attribute__>=");//case9
        		comboBox.addItem("Value Attribute__<=");//case10
        		comboBox.addItem("Value Attribute__> & <");//case11
        		comboBox.addItem("Value Attribute__>= & <=");//case12
        		comboBox.addItem("Ingore when filtering");//case13
        		
        // step3: edit button 
        JButton btnEdit = new JButton("Edit"); 
        btnEdit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
				Vector <String> columnNames = new Vector<String>();
				//add pre-determined column names
				columnNames.add("ColumnName");
				columnNames.add("ColumnProperty__Operator");
				columnNames.add("ColumnFilter");
				//data is a vector of vectors(rows)
				Vector <Vector<String>> data = new Vector<Vector<String>>();
				Vector<String> colNames = showCol(list_1.getSelectedValue().toString());	
				//initializing this table by filling the first entry of each row (or all entries of the first column)
				data=getIniData(colNames);
				((FilterTableModel) filterModel).setDataType(isNetwork);
				filterModel.setDataVector(data, columnNames);
				//install table cell editor for second column
				table2.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
			}

			private Vector<Vector<String>> getIniData(Vector<String> colNames) {
			// TODO Auto-generated method stub
				Vector<Vector<String>> iniData=new Vector<Vector<String>>();
				for (int i=0; i<colNames.size(); i++)
				{
					Vector<String> r=new Vector<String>();
					r.add(colNames.elementAt(i));
					iniData.add(r);
				}	
				return iniData;
			}});
        GridBagConstraints gbc_btnEdit = new GridBagConstraints();
        gbc_btnEdit.insets = new Insets(0, 0, 5, 5);
        gbc_btnEdit.gridx = 6;
        gbc_btnEdit.gridy = 3;
        frmIrankBetaVersion.getContentPane().add(btnEdit, gbc_btnEdit);	
        		
        //step3: save2 button
        JButton btnSave2 = new JButton("Save"); 
        btnSave2.addActionListener(new ActionListener(){

        	@Override
        	public void actionPerformed(ActionEvent e) {
        		// TODO Auto-generated method stub
        				
        		if(table2.isEditing()){ 
      				    int   row   =   table2.getEditingRow(); 
      				    int   col   =   table2.getEditingColumn(); 
      				    table2.getCellEditor(row,col).stopCellEditing(); 
        			}
        		//attach the table being edited (hash value) to this file name(hash key)
        		addFileFilter(list_1.getSelectedValue().toString()); 
        				
        	}

        	//attach filter table to this file name
			private void addFileFilter(String fn) {
				// TODO Auto-generated method stub
					Vector<Vector<String>> filterInfo = new Vector<Vector<String>>();;
					for (int i = 0; i<table2.getColumnCount();i++)
					{
						Vector<String> r=new Vector<String>();
						for (int j=0; j<table2.getRowCount();j++)
						{
							r.add((String) table2.getValueAt(j, i));
						}
						filterInfo.add(r);
					}
						
					fileFilterTable.put(fn, filterInfo);					
			}});
        GridBagConstraints gbc_btnSave2 = new GridBagConstraints();
        gbc_btnSave2.insets = new Insets(0, 0, 5, 0);
        gbc_btnSave2.gridx = 7;
        gbc_btnSave2.gridy = 3;
        frmIrankBetaVersion.getContentPane().add(btnSave2, gbc_btnSave2);
        		
        //step4: label_1		
        JLabel lblSelectFiles = new JLabel("4. Select Files to Integrate");
        GridBagConstraints gbc_lblSelectFiles = new GridBagConstraints();
        gbc_lblSelectFiles.anchor = GridBagConstraints.WEST;
        gbc_lblSelectFiles.insets = new Insets(0, 0, 5, 5);
        gbc_lblSelectFiles.gridwidth=2;
        gbc_lblSelectFiles.gridx = 0;
        gbc_lblSelectFiles.gridy = 9;
        frmIrankBetaVersion.getContentPane().add(lblSelectFiles, gbc_lblSelectFiles);		
        		
        //step4: associate this check list with its data model
		final JList list = new JList(fileListModel2);
		JScrollPane scrollpane2=new JScrollPane(list);
		list.setCellRenderer(new CheckListRenderer()); //cell render paints this cell area;
		list.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent event){
					JList list = (JList) event.getSource();
					// Get index of item clicked
					int index = list.locationToIndex(event.getPoint());
					CheckListItem item = (CheckListItem)
					list.getModel().getElementAt(index);
		 
					// Toggle selected state
					item.setSelected(! item.isSelected());
		 
					// Repaint cell
					list.repaint(list.getCellBounds(index, index));
			}});  
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 2;
		gbc_list.gridheight=3;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 10;
		frmIrankBetaVersion.getContentPane().add(scrollpane2, gbc_list);
		
		
		
		//step4: label_2
		JLabel lblNewLabel = new JLabel("Select Data Type:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 9;
		frmIrankBetaVersion.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		//step4: button group
		ButtonGroup buttonGroup=new ButtonGroup();
		ButtonGroup buttonGroup2=new ButtonGroup();
		
		//step4: list radio button
		JRadioButton rdbtnList_1 = new JRadioButton("List");
		buttonGroup.add(rdbtnList_1);
		rdbtnList_1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isNetwork=false;
				//System.out.println(isNetwork);
			}});
		GridBagConstraints gbc_rdbtnList_1 = new GridBagConstraints();
		gbc_rdbtnList_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnList_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnList_1.gridx = 3;
		gbc_rdbtnList_1.gridy = 10;
		frmIrankBetaVersion.getContentPane().add(rdbtnList_1, gbc_rdbtnList_1);
		
		
		
		//step4: network radio button
		JRadioButton rdbtnNetwork = new JRadioButton("Network");
		buttonGroup.add(rdbtnNetwork);
		rdbtnNetwork.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				isNetwork=true;
				//System.out.println(isNetwork);
				
			}});
		GridBagConstraints gbc_rdbtnNetwork = new GridBagConstraints();
		gbc_rdbtnNetwork.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNetwork.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNetwork.gridx = 3;
		gbc_rdbtnNetwork.gridy = 11;
		frmIrankBetaVersion.getContentPane().add(rdbtnNetwork, gbc_rdbtnNetwork);
		
		//step4: label_3
		JLabel lblApplyFiltersYou = new JLabel("Apply filters you specify for each file?");
		GridBagConstraints gbc_lblApplyFiltersYou = new GridBagConstraints();
		gbc_lblApplyFiltersYou.insets = new Insets(0, 0, 5, 5);
		gbc_lblApplyFiltersYou.gridx = 5;
		gbc_lblApplyFiltersYou.gridy = 9;
		frmIrankBetaVersion.getContentPane().add(lblApplyFiltersYou, gbc_lblApplyFiltersYou);
		
		//step4: yes radio button
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Yes");
		buttonGroup2.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				aplFilter=true;
			}});
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 5;
		gbc_rdbtnNewRadioButton.gridy = 10;
		frmIrankBetaVersion.getContentPane().add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		
		//step4: no radio button
		JRadioButton rdbtnNo = new JRadioButton("No");
		buttonGroup2.add(rdbtnNo);
		rdbtnNo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				aplFilter=false;
			}});
		GridBagConstraints gbc_rdbtnNo = new GridBagConstraints();
		gbc_rdbtnNo.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNo.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNo.gridx = 5;
		gbc_rdbtnNo.gridy = 11;
		frmIrankBetaVersion.getContentPane().add(rdbtnNo, gbc_rdbtnNo);
		
		

		// step4: progress bar
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 5;
		gbc_progressBar.gridy = 12;
		frmIrankBetaVersion.getContentPane().add(progressBar, gbc_progressBar);
		
				
		//step4: run button
		final JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnRun.setEnabled(false);
				
				//get the list of  file names 
					ArrayList <String> fnList=new ArrayList<String>();		
					for (int i=0; i<list.getModel().getSize(); i++)
					{  
						boolean sel = ((CheckListItem) list.getModel().getElementAt(i)).isSelected();
						if(sel==true)
						{
							String str =list.getModel().getElementAt(i).toString();
							fnList.add(str);
						}
					}
					
					//convert this list of file names into an array of file names
					String[] fileNames =new String[fnList.size()];		
					for(int i=0; i<fnList.size(); i++)
					{
						fileNames[i] = fnList.get(i);
					}
							
					/*initiate IntePool object with four parameters
					 * String[]: an array of selected file names
					 * fileFilterTable: a hash table that store each file name(hash key) and its filter table(value)
					 * boolean: data type, network(true) or list(false)
					 * boolean: apply filter table or not
					*/
					IntePool pool=new IntePool(fileNames,fileFilterTable,isNetwork, aplFilter); //run process
					pool.addPropertyChangeListener(
					            new PropertyChangeListener() {
					                public  void propertyChange(PropertyChangeEvent evt) {
					                    if ("progress".equals(evt.getPropertyName())) {
					                        progressBar.setIndeterminate(false);
					                        progressBar.setValue((Integer)evt.getNewValue());
					                    }
					                }
					            });
					pool.execute();
					
				/*receive the result from running program, update result-receiving
				 *  variables for each run-button click
				 */
					ResultWrapper result = null;
					try {
						result=pool.get();
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (ExecutionException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					Node[] nodes=null;
					int[][]nodeStat=null;
					Edge[] edges=null;
					int [][] edgeStat=null;
					//return results by calling get() methods of ResultWrapper class
					if(result.getNodes()!=null){
						nodes=result.getNodes();
						int size=result.getNodeStat().length;
						nodeStat=new int[size][2];
						nodeStat=result.getNodeStat();
					}
					
					if(result.getEdges()!=null){
						edges=result.getEdges();
						int size2=result.getEdgeStat().length;
						edgeStat=new int[size2][2];
						edgeStat=result.getEdgeStat();
						
					}
					
					//save file dialog		
					JFileChooser savechooser=new JFileChooser();
					savechooser.setMultiSelectionEnabled(false);
					savechooser.setCurrentDirectory(new File("."));
					savechooser.setAcceptAllFileFilterUsed(false);
					savechooser.setFileFilter(new FileNameExtensionFilter("text files(*.txt)", "txt"));
					indicatorDialog dialog=new indicatorDialog();
					dialog.setVisible(true); //dialog pops out
					//return the user's choice of output format; pipe=1: text; pipe=2: visualization
					int pipe=0;
					if(dialog.getOK()==true)
					{
						pipe=dialog.getPipe();
					}
					
					//dialog option 1: text output
					if (pipe==1)                                           
					{
								
						int indicator=savechooser.showSaveDialog(frmIrankBetaVersion);
						if(indicator==JFileChooser.APPROVE_OPTION)
						{
							File f = savechooser.getSelectedFile();
							String filePath = f.getPath();
							if(!filePath.toLowerCase().endsWith(".txt"))
							{
								f = new File(filePath + ".txt");
							}
							PrintStream out=null;
							try {
								out=new PrintStream(f);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							TextPrinter mytext=new TextPrinter(out);
							mytext.addInputInfo(aplFilter, fileNames, fileFilterTable);
							mytext.addOutputInfo(nodes, nodeStat, edges, edgeStat);
							mytext.printFileHeader();
							if (isNetwork==true){
								mytext.printEdgeStat();
								mytext.printEdge();
								mytext.printNodeStat();
								mytext.printNode();
							}else{
								mytext.printElementStat();
								mytext.printElement();
							}
						}
							
						
					}else if(pipe==2){             // dialog option 2 visualization window
									
						MySketch sketch=new MySketch(nodes, edges, nodeStat, edgeStat,fileNames,fileFilterTable, aplFilter);
						sketch.ini(); 
					}
												
					btnRun.setEnabled(true);
					progressBar.setValue(0);
			}});
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.insets = new Insets(0, 0, 5, 5);
		gbc_btnRun.gridx = 6;
		gbc_btnRun.gridy = 12;
		frmIrankBetaVersion.getContentPane().add(btnRun, gbc_btnRun);
		
		JMenuBar menuBar = new JMenuBar();
		frmIrankBetaVersion.setJMenuBar(menuBar);
		
		JMenu mnVisual = new JMenu("Visualize");
		menuBar.add(mnVisual);
		
		JMenuItem mntmImportIrankText = new JMenuItem("Import EnRICH Text Result");//menu item 1 visualize
		mntmImportIrankText.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser openchooser = new JFileChooser();
        		openchooser.setMultiSelectionEnabled(false);
        		openchooser.setFileFilter(new FileNameExtensionFilter("text files", "txt"));
        		openchooser.setCurrentDirectory(new File("."));
        		int indicator =openchooser.showOpenDialog(frmIrankBetaVersion);
        		if (indicator==JFileChooser.APPROVE_OPTION)
        		{
        			File f= openchooser.getSelectedFile(); //return file;
        			Pipe inPipe=new Pipe(f);
        			inPipe.parseIn();
        			MySketch sketch=new MySketch(inPipe.getNodes(), inPipe.getEdges(), inPipe.GetNodeStat(), inPipe.GetEdgeStat(),inPipe.getfNames(), inPipe.getfileFilterTable(), inPipe.getAplFilter());
					//sketch.setVisible(true);
        			sketch.ini();		
        		}
				
			}});
		mnVisual.add(mntmImportIrankText);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		mntmHelp.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			 HelpDialog help = null;
			help = new HelpDialog();
			help.setResizable(true);
			help. setTitle("EnRICH User Maunual");
			
				help.ini("please visit: http://xiazhang.public.iastate.edu/demo.html");
		
			 help.setVisible(true);
				
			}});
	
	
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		JMenuItem mntmIrank = new JMenuItem("EnRICH");
		mnAbout.add(mntmIrank);
		mntmIrank.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				HelpDialog about = null;
				about = new HelpDialog();
				about.setResizable(true);
				about.setTitle("About EnRICH");
				about.ini("EnRICH qualitatively integrate heterogeneous data sets " +
						"while filtering each of them based on its attributes."+"\r\n\n"+
    "Copyright (C) <2012>  <Xia Zhang>"+"\r\n\n"+

   "This program is free software: you can redistribute it and/or modify "
    +"it under the terms of the GNU General Public License as published by "
    +"the Free Software Foundation, either version 3 of the License, or "
    +"(at your option) any later version."+"\r\n"+

    "This program is distributed in the hope that it will be useful, "
    +"but WITHOUT ANY WARRANTY; without even the implied warranty of "
    +"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the "
    +"GNU General Public License for more details"+"\r\n"+

    "You should have received a copy of the GNU General Public License "
   +"along with this program.  If not, see http://www.gnu.org/licenses");
		
				about.setVisible(true);
					
			}});
			
	}//initialize() end
	
	    //return column names of a file as a vector of string
    	protected Vector<String> showCol(String fn) {//fn:pathname string
		// TODO Auto-generated method stub
    			Scanner in;
    			Vector<String> colNames = new Vector<String>();
    			try {
    				in = new Scanner (new File(fn));
			
    				if(in.hasNextLine()){
    					String line = in.nextLine().trim();
    					String[] tokens = line.split("\\t");
    					for (int i=0; i<tokens.length; i++){
    						colNames.addElement(tokens[i]);
    					}						
    				} 
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			return colNames;
    	}
    	

}
