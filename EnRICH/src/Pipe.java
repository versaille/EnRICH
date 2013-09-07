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
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;


public class Pipe {

	private File f; 
	private boolean aplFilter;
	private String[] fNames;
	private HashMap<String, Vector<Vector<String>>> filterTable=new HashMap <String, Vector<Vector<String>>>();
	private String target; //each filename
	private Vector<Vector<String>> vec2;
	
	private ArrayList<Integer> num1=new ArrayList<Integer> ();//edgeStat column 1
	private ArrayList<Integer> num2=new ArrayList<Integer> (); //edgeStat column 2
	private ArrayList<Integer> num3=new ArrayList<Integer> (); //nodeStat column 1
	private ArrayList<Integer> num4=new ArrayList<Integer> (); //nodeStat column 2
	
	private HashMap<String, Node> nodeTable=new HashMap<String, Node>();
	private ArrayList<Edge> edgeArr=new ArrayList<Edge>();
	
	public Pipe(File f){
		this.f=f;
	
		this.vec2=new Vector<Vector<String>> ();
		
	}
	
	public void parseIn(){
		int block=0;
		String line;
		try {
			Scanner in=new Scanner(f);
			while(in.hasNextLine())
			{
				line=in.nextLine().trim();
				if(line.isEmpty()){
					continue;
				}else{
					if (line.contains("Integration Pool:")) block=1;
					else if(line.contains("Filters applied:")) judgeFilter(line);
					//block=2; 
					else if (line.contains("Edge Statistics Table")) block=3;
					else if (line.contains("Integrated Edge Table")) block=4;
					else if (line.contains("Node Statistics Table")) block=5;
					else if(line.contains("Integrated Node Table")) block=6;
					else {
						switch (block){
							case 1:
								addPool(line);
								break;
							case 3:
								if(line.contains("Edge Reoccurrence")) 
								{
									break;
								}else{
									addEdgeStat(line);
									break;
								}
								
							case 4:
								if (line.contains("Edge (From) Label"))
								{
									break;
								}else {
									addIntEdge(line);
									break;
								}
							case 5:
								if (line.contains("Node Degree")) 
								{
									break;
								}else{
									addNodeStat(line);
									break;
								}
								
						/*case 6:
							addIntNode(line);*/
						}
					}
				
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void addIntEdge(String line) {
		// TODO Auto-generated method stub
		String []tokens=line.split("\t");
		//System.out.println(tokens[5]);
		String [] sources=tokens[5].substring(tokens[5].indexOf('[')+1, tokens[5].indexOf(']')).split(",");
		String label1=tokens[0].trim();
		String label2=tokens[1].trim();
		Node n1=(Node) nodeTable.get(label1);
		Node n2=(Node) nodeTable.get(label2);
		if (n1==null)
		{
			n1=new Node(label1);
		}else{
			n1.addCount();
		}
		if (n2==null)
		{
			n2=new Node(label2);
		}else{
			n2.addCount();
		}
		for (int i=0; i<sources.length;i++)
		{
			n1.addSource(sources[i].trim());
			n2.addSource(sources[i].trim());
		}
		nodeTable.put(label1, n1);
		nodeTable.put(label2, n2);
		Edge e=new Edge(n1, n2);
		e.addThisCount(Integer.parseInt(tokens[3].trim()));
		e.addThisDirCount(Integer.parseInt(tokens[4].trim()));
		for (int i=0; i<sources.length;i++)
		{
			e.addSource(sources[i]);
		}
		edgeArr.add(e);
	}

	

	/*private void addIntNode(String label) {
		// TODO Auto-generated method stub
		String [] tokens=line.split("\t");
		Node n=new Node(tokens[1].trim());
		n.addThisCount(Integer.parseInt(tokens[1].trim()));
		target=tokens[2].substring(tokens[2].indexOf("[")+1, tokens[2].indexOf("]"));
		String [] tokens2=target.split(",");
		for (int i=0; i<tokens2.length; i++)
		{
			n.addSource(tokens2[i].trim());
		}	
	}*/

	private void addNodeStat(String line) {
		// TODO Auto-generated method stub
		String [] tokens=line.split("\t");
		num3.add(Integer.parseInt(tokens[0].trim())); 
		num4.add(Integer.parseInt(tokens[1].trim()));
	}

	private void addEdgeStat(String line) {
		// TODO Auto-generated method stub
		String [] tokens=line.split("\t");
		num1.add(Integer.parseInt(tokens[0].trim())); 
		num2.add(Integer.parseInt(tokens[1].trim()));	
	}

	private void addPool(String line) {
		// TODO Auto-generated method stub
		
		if(line.contains("FileName:")) 
		{   
			target=line.substring(line.indexOf(':')+1).trim();
			vec2.clear();
		}else {
			String target2=line.substring(line.indexOf('[')+1, line.indexOf(']'));
			String [] tokens=target2.split(",");
			Vector<String> vec=new Vector<String>();
			for (int i=0; i<tokens.length; i++)
			{
				vec.addElement(tokens[i]);
			}
			vec2.addElement(vec);
			filterTable.put(target,vec2);
		}	
		
	}

	private void judgeFilter(String line) {
		// TODO Auto-generated method stub
		if(line.contains("true"))
			aplFilter=true;
		else aplFilter=false;
	}

	public Node[] getNodes() {
		// TODO Auto-generated method stub
		Node [] nodes=new Node[nodeTable.size()];
		Iterator<String> it=nodeTable.keySet().iterator();
		int i=0;
		while(it.hasNext()){
			nodes[i]=nodeTable.get(it.next());
			i++;
		}
		Arrays.sort(nodes);
		return nodes;
	}

	public Edge[] getEdges() {
		// TODO Auto-generated method stub
		Edge [] edges=new Edge[edgeArr.size()];
		for (int i=0; i<edgeArr.size(); i++){
			edges[i]=edgeArr.get(i);
		}
		Arrays.sort(edges);
		return edges;
	}

	public int[][] GetNodeStat() {
		// TODO Auto-generated method stub
		int [][] nodeStat=new int[num3.size()][2];
		for (int i=0; i<num3.size(); i++){
			nodeStat[i][0]=num3.get(i);
			nodeStat[i][1]=num4.get(i);
		}
		return nodeStat;
	}

	public int[][] GetEdgeStat() {
		// TODO Auto-generated method stub
		int [][] edgeStat=new int[num1.size()][2];
		for (int i=0; i<num1.size(); i++){
			edgeStat[i][0]=num1.get(i);
			edgeStat[i][1]=num2.get(i);
		}
		return edgeStat;
		
	}

	public String[] getfNames() {
		// TODO Auto-generated method stub
		String [] fNames=new String[filterTable.size()];
		Iterator <String> it=filterTable.keySet().iterator();
		int i=0;
		while(it.hasNext()){
			fNames[i]=it.next();
			i++;
		}
		return fNames;
	}

	public HashMap<String, Vector<Vector<String>>> getfileFilterTable() {
		// TODO Auto-generated method stub
		
		return filterTable;
	}

	public boolean getAplFilter() {
		// TODO Auto-generated method stub
		return aplFilter;
	}
}
