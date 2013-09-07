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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MySketch
{   private JFrame frame;
    private JPanel panel;
    private JScrollPane scroll;
	private Node[] nodes;
	private Edge[] edges;
	private int[][] nodeStat;
	private int[][] edgeStat;
	private NetworkSketch mydraw;
	private JFileChooser chooser;
	private String[] fileNames;
	private HashMap<String, Vector<Vector<String>>> fileFilterTable;
	private boolean aplFilter;
	

	
	public MySketch(final Node[] nodes, final Edge[] edges, final int[][]nodeStat,final int[][]edgeStat,
			final String[] fileNames, final HashMap<String,Vector<Vector<String>>> fileFilterTable, 
			final boolean aplFilter)
	{
		this.nodeStat=nodeStat;
		this.edgeStat=edgeStat;
		this.nodes=nodes;
		this.edges=edges;
		this.fileNames=fileNames;
		this. fileFilterTable=fileFilterTable;
		this.aplFilter=aplFilter;
		/*this.
		this.scroll=new JScrollPane();
		this.chooser=new JFileChooser();*/
	}
	
	public void ini(){
		int w=Toolkit.getDefaultToolkit().getScreenSize().width /5*4;
		int h=Toolkit.getDefaultToolkit().getScreenSize().height/5*4;
		frame=new JFrame();
		frame.setSize(w,h);
		frame.setLocationByPlatform(true);
		frame.setTitle("Visualization of EnRICH Result");
		ImageIcon icon=new ImageIcon("../imageIcons/logo.jpg");
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.setResizable(false);
		int sw=Toolkit.getDefaultToolkit().getScreenSize().width/50*39;///5*4;
		int sh=Toolkit.getDefaultToolkit().getScreenSize().height/50*36;///5*4;
		mydraw= new NetworkSketch(nodeStat,edgeStat,edges,sw,sh); 
		frame.getContentPane().add(mydraw);
		mydraw.init();
		frame.setVisible(true);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu mnAbout = new JMenu("Export");
		menuBar.add(mnAbout);
	
		chooser=new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setCurrentDirectory(new File("."));
		chooser.setAcceptAllFileFilterUsed(false);
	
		JMenuItem mnt1 = new JMenuItem("Results as Image");
		mnAbout.add(mnt1);
		frame.setJMenuBar(menuBar);
		mnt1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				chooser.resetChoosableFileFilters();
				chooser.setFileFilter(new FileNameExtensionFilter("image files", "jpeg", "png", "tif" ));
				int indicator=chooser.showSaveDialog(frame);
				if (indicator==JFileChooser.APPROVE_OPTION)
				{
					File outFile=chooser.getSelectedFile();
					save(outFile.getAbsolutePath());
				}
			}});
  
		JMenuItem mnt2 = new JMenuItem("Results as Text File");
		mnAbout.add(mnt2);
		frame.setJMenuBar(menuBar);
		mnt2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				chooser.resetChoosableFileFilters();
				chooser.setFileFilter(new FileNameExtensionFilter("text files(*.txt)", "txt"));
				int indicator=chooser.showSaveDialog(frame);
				if (indicator==JFileChooser.APPROVE_OPTION)
				{  
					File f = chooser.getSelectedFile();
					String filePath = f.getPath();
					if(!filePath.toLowerCase().endsWith(".txt"))
					{
						f = new File(filePath + ".txt");
					}
					PrintStream out=null;
					try {
						out=new PrintStream(f);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TextPrinter mytext=new TextPrinter(out);
					mytext.addInputInfo(aplFilter, fileNames, fileFilterTable);
					mytext.addOutputInfo(nodes, nodeStat, edges, edgeStat);
					mytext.printFileHeader();
					mytext.printEdgeStat();
					mytext.printEdge();
					mytext.printNodeStat();
					mytext.printNode();			
				}
			
			}});
	
		JMenuItem mnt3 = new JMenuItem("Only Node");
		mnAbout.add(mnt3);
		frame.setJMenuBar(menuBar);
		mnt3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				chooser.resetChoosableFileFilters();
				chooser.setFileFilter(new FileNameExtensionFilter("text files(*.txt)", "txt"));
				int indicator=chooser.showSaveDialog(frame);
				if (indicator==JFileChooser.APPROVE_OPTION)
				{  
					File f = chooser.getSelectedFile();
					String filePath = f.getPath();
					if(!filePath.toLowerCase().endsWith(".txt"))
					{
						f = new File(filePath + ".txt");
					}
					PrintStream out=null;
					try {
						out=new PrintStream(f);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 // print only nodes
					TextPrinter mytext=new TextPrinter(out);
					mytext.addOutputInfo(nodes, nodeStat, edges, edgeStat);
					mytext.printNode();	
				}
				
			}});
		
		JMenuItem mnt4 = new JMenuItem("Only Edge");
		mnAbout.add(mnt4);
		frame.setJMenuBar(menuBar);
		mnt4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				chooser.resetChoosableFileFilters();
				chooser.setFileFilter(new FileNameExtensionFilter("text files(*.txt)", "txt"));
				int indicator=chooser.showSaveDialog(frame);
				if (indicator==JFileChooser.APPROVE_OPTION)
				{  
					File f = chooser.getSelectedFile();
					String filePath = f.getPath();
					if(!filePath.toLowerCase().endsWith(".txt"))
					{
						f = new File(filePath + ".txt");
					}
					PrintStream out=null;
					try {
						out=new PrintStream(f);
					} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// print only edges
					TextPrinter mytext=new TextPrinter(out);
					mytext.addOutputInfo(nodes, nodeStat, edges, edgeStat);
					mytext.printEdge();	
				}
			}});

		}//ini() ends
	
	public void save(String filename){
		mydraw.saveImage(filename);
	}

}
