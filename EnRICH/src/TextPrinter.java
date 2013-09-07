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
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;


public class TextPrinter {
	private PrintStream out;
	private boolean aplFilter;
	private String[]fileNames;
	private HashMap <String, Vector<Vector<String>>> fileFilterTable=new HashMap<String, Vector<Vector<String>>>();
	private Node[] nodes;
	private Edge[] edges;
	private int[][] nodeStat;
	private int[][] edgeStat;
	
	public TextPrinter(PrintStream out)
	{
		this.out=out;
	}
	
	public void addInputInfo(boolean aplFilter, String[] fileNames,HashMap <String, Vector<Vector<String>>> fileFilterTable )
	{
		this.aplFilter=aplFilter;
		this.fileNames=fileNames;
		this.fileFilterTable=fileFilterTable;
	}
	public void addOutputInfo(Node[] nodes, int[][] nodeStat, Edge[] edges, int[][]edgeStat)
	{
		this.nodes=nodes;
		this.nodeStat=nodeStat;
		this.edges=edges;
		this.edgeStat=edgeStat;
	}
	public void printElement () {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Integrated List Table");
		out.printf("%s\t%s\t%s\n","Element Label","Element reoccurrence", "Sources");
		for (int i=0; i< nodes.length; i++)
		{
			out.printf("%s\t%d\t%s\n", nodes[i].getLabel(),nodes[i].getCount(), nodes[i].getSource());
		}
			out.println("\n");
		}
		

	public void printElementStat() {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Element Statistics Table");
		out.printf("%s\t%s\n","Element reoccurrence","The Number of elements(>=)this reoccurrence");
		for (int i=0; i<nodeStat.length;i++)
		{
			out.printf("%d\t%d\n",nodeStat[i][0], nodeStat[i][1]);	
		}
		out.println("\n");
		
	}

	
	public void printNode () {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Integrated Node Table");
		out.printf("%s\t%s\t%s\n","Node Label","Node Degree", "Sources");
		for (int i=0; i< nodes.length; i++)
		{
			out.printf("%s\t%d\t%s\n", nodes[i].getLabel(),nodes[i].getCount(), nodes[i].getSource());
		}
			out.println("\n");
		}
		

	public void printNodeStat() {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Node Statistics Table");
		out.printf("%s\t%s\n","Node Degree","The Number of nodes(>=)this degree");
		for (int i=0; i<nodeStat.length;i++)
		{
			out.printf("%d\t%d\n",nodeStat[i][0], nodeStat[i][1]);	
		}
		out.println("\n");
		
	}

	public void printEdge() {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Integrated Edge Table");
		out.printf("%s\t%s\t%s\t%s\t%s\t%s\n","Edge (From) Label","Edge (To) Label", "Edge reoccurrence(total)", "(undirected)", "(directed)", "Sources");
		for (int i=0; i< edges.length; i++)
		{
			out.printf("%s\t%s\t%d\t%d\t%d\t%s\n", edges[i].getFromLabel(), edges[i].getToLabel(), edges[i].getTotalCount(),edges[i].getCount(),edges[i].getDirCount(), edges[i].getSource());
		}
		out.println("\n");
		
	}

	public void printEdgeStat() {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Edge Statistics Table");
		out.printf("%s\t%s\n","Edge Reoccurrence","The Number of edges(>=)this reoccurrence");
		for (int i=0; i<edgeStat.length;i++)
		{
			out.printf("%d\t%d\n",edgeStat[i][0], edgeStat[i][1]);	
		}
		out.println("\n");	
		
	}

	public void printFileHeader() {
		// TODO Auto-generated method stub
		out.printf("%s\n", "Integration Pool: ");
		out.println("\n");
		for (int i=0; i<fileNames.length; i++)
		{
			out.printf("%s\t%s\n","FileName:", fileNames[i]);
			if (fileFilterTable.get(fileNames[i])!=null)
			{
				Vector<Vector<String>> filterVec = fileFilterTable.get(fileNames[i]);
				out.printf("%s\t%s\n","ColumnName:", filterVec.elementAt(0));
				out.printf("%s\t%s\n","ColumnProperty:", filterVec.elementAt(1));
				out.printf("%s\t%s\n","ColumnFilter:", filterVec.elementAt(2));
			}
				
		}
		out.println("\n");
		out.println("Filters applied:" + aplFilter);
		out.println("\n");
		
	}

}
