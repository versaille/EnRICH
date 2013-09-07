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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;


public class IntePool extends SwingWorker<ResultWrapper, Object> {
	private String[] fileNames; //file names of all files
	//hash key:file name; hash value:filter table for this file
	private HashMap<String, Vector<Vector<String>>> fileFilterTable; 
	private boolean isNetwork; 
	private boolean aplFilter; 
	
	//below variables are updated for each file
	private String fileSource;
	private boolean isNullFilter;//hash map fileFilterTable.get() return null
	private String[] colNames;  
	private String[] colProperties; //filter properties: string
	private int [] colCases; //corresponding case numbers for filter properties
	private String[] filters; //specific filters such as strings and numbers
	private String[] nameStr; //intermediate for constructing node and edge label
	
	//below variables are updated for each input line 
	private boolean isPass;
	private boolean isDir;
	
	//results are passed to these variables
	//hash key:node label; hash value:Node Object
	private HashMap<String, Node> nodeTable=new HashMap<String, Node>();
	//hash key:fromLabel/toLabel (alphabetical order if undirected)
	private HashMap<String, Edge> edgeTable=new HashMap<String, Edge>();
	private int[][] statNodeArr;
	private ResultWrapper result;
	
	
	public IntePool(String[] fileNames, HashMap<String, Vector<Vector<String>>> fileFilterTable, boolean isNetwork, boolean aplFilter)
	{
		this.fileNames=fileNames;
		this.fileFilterTable=fileFilterTable;
		this.isNetwork=isNetwork;
		this.aplFilter=aplFilter;
	}
	
	protected ResultWrapper doInBackground() throws Exception
	{
		setProgress(0);
		int progress=0;
		
		for (int i=0; i<fileNames.length; i++)
		{
			try{
				//Interrupted exception 
				Thread.sleep(100);
				progress=((i+1)/fileNames.length)*100;
				setProgress(progress);
		
				try{
					/*default: fileFilterTabel.get() returns true
					 * it will return false if the user did not specify any filter
					 * */
					isNullFilter=false; 
					Scanner in=new Scanner(new File(fileNames[i]));
					//update variables for each file
					updateFilters(fileNames[i]); 
					fileSource=getFileSource(fileNames[i]);
					//omit the column names
					if (in.hasNextLine()) {
						String header = in.nextLine();
					} 
					//process each line
					while(in.hasNextLine()){
						String line = in.nextLine().trim(); 
						if (line.isEmpty()){
							continue;
						}else{
							isPass=doFiltering(line); //filter this line
						
							if (isPass==true){ 
								if(isNetwork==true){
								//update isDir, the variable which will be used in addEdge()
									if(nameStr[0].equals("undirected")) isDir=false;
									else isDir=true;
								/* addEdge() adds edge to edgeTable, at the same time,
								 * it also adds the nodes that compose this edge into nodeTable*/
									addEdge(nameStr[1],nameStr[2]);
								}else{
									Node n=addNode(nameStr[0]);
								}	
							}else {//skip this line if it didn't pass the filters
							continue;
							}	
						}
					}//while ends
				
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
			 } catch (InterruptedException ie) {
				 ie.printStackTrace();
			 }
						
		} //for end
		
		if (isNetwork==true){
			result=new ResultWrapper(this.getNodes(), this.getEdges(), this.getNodeStat(), this.getEdgeStat() );
		}else{
			result=new ResultWrapper(this.getNodes(), null, this.getNodeStat(), null);
		}
		
		return result;
	}
	
	/*addEdge() looks for the edge that is denoted by the labels of its two nodes in edgeTable;
	 * if found, add edge count and also node count; 
	 * if not found, create this edge and put it into edgeTable*/
	private void addEdge(String fromLabel, String toLabel) {
		// TODO Auto-generated method stub
		fromLabel=fromLabel.toLowerCase();
		toLabel=toLabel.toLowerCase();
		StringBuilder builder=new StringBuilder();
		//this label represents a pair of nodes, serve as hash key for edge between them
		String pairLabel; 
		
		//label order: alphabetical order
		if (fromLabel.compareTo(toLabel)>0)
		{
			 pairLabel=builder.append(fromLabel).append('/').append(toLabel).toString();
		}else{
				pairLabel=builder.append(toLabel).append('/').append(fromLabel).toString();
		}
		
		Edge e=(Edge)edgeTable.get(pairLabel);
        if (e==null)
        {   //findNode will invoke node.addCount()
        	Node from=findNode(fromLabel);
            Node to=findNode(toLabel);
            Edge ee=new Edge(from, to);
            ee.addSource(fileSource);
            if(isDir==true)
            {
            	ee.addDirCount();
            }else{
            	ee.addCount();
            }
        	edgeTable.put(pairLabel, ee);
        }else{
        	e.addSource(fileSource);
        	if (isDir==true) e.addDirCount();
        	else e.addCount();
         }
        	
	}

	private Node findNode(String label) {
		// TODO Auto-generated method stub
		label=label.toLowerCase();
        Node n=(Node) nodeTable.get(label);
        if (n!=null){
        	n.addCount();
        	n.addSource(fileSource);
        	return n;  
        }else {
        	return addNode(label);
        }
        
	}

	private Node addNode(String label) {
		// TODO Auto-generated method stub
		label=label.toLowerCase();
		Node n=(Node)nodeTable.get(label);
		if (n==null)
		{
			//System.out.println("label and fileSource:" + label + fileSource);
			Node nn=new Node(label);
			nn.addSource(fileSource);
			nodeTable.put(label, nn);
		} else {
			n.addCount();
			n.addSource(fileSource);
		}
        
        return nodeTable.get(label);
	}
	
	/*getFileSource() returns file name from the file path string*/
	private String getFileSource(String str) {
		// TODO Auto-generated method stub
		String source;
			int dotloc, slashloc;
			int len=str.length()-1;
			dotloc=len;
			while(str.charAt(dotloc)!='.') 
				{
				dotloc--;
				}
			slashloc=dotloc;
			while(str.charAt(slashloc)!='\\')
				{
				slashloc--;
				}
			source=str.substring(slashloc+1, dotloc);
			return source;
	}

	
/*updateFilters() relies on fileName and private field isNullFilter to 
 * update variables including 
 * String[] colNames
 * String[] colProperties
 * String[] filters
 * int [] colCases*/
	private void updateFilters(String fileName) {
		// TODO Auto-generated method stub
		if (fileFilterTable.get(fileName)!=null)
		{	
			//filterVec is the whole filter table for each file
			Vector<Vector<String>> filterVec = fileFilterTable.get(fileName);
			colNames=new String[filterVec.get(0).size()]; //column name
			colProperties=new String[filterVec.get(0).size()]; //column property
			filters=new String[filterVec.get(0).size()]; //specific filters
			for(int i=0; i<filterVec.get(0).size(); i++){
				 colNames[i]=filterVec.get(0).get(i);
				 colProperties[i] = filterVec.get(1).get(i);
				 filters[i] = filterVec.get(2).get(i);
			 }
			//convert column properties to case numbers which will be used in doFiltering()
			colCases=new int[colProperties.length];
			for (int i=0; i<colProperties.length;i++)
			{
				if(colProperties[i]==null) {
					 colCases[i]=-1;
			  	 } else if (colProperties[i].equals("List Element")==true){
					 colCases[i]=0;
				 }else if(colProperties[i].equals("Edge Undirected Node")==true) {
					 colCases[i]=1;
				 }else if(colProperties[i].equals("Edge From Node")==true) {
					 colCases[i]=2;
				 }else if(colProperties[i].equals("Edge To Node")==true) {
					 colCases[i]=3;
				 }else if(colProperties[i].equals("Label Attribute__CONTAINS")==true) {
					 colCases[i]=4;
				 }else if(colProperties[i].equals("Label Attribute__EQUALS TO")==true) {
					 colCases[i]=5;
				 }else if(colProperties[i].equals("Value Attribute__>")==true) {
					 colCases[i]=6;
				 }else if(colProperties[i].equals("Value Attribute__<")==true) {
					 colCases[i]=7;
				 }else if(colProperties[i].equals("Value Attribute__=")==true) {
					 colCases[i]=8; 
				 }else if(colProperties[i].equals("Value Attribute__>=")==true) {
					 colCases[i]=9;
				 }else if(colProperties[i].equals("Value Attribute__<=")==true) {
					 colCases[i]=10;
				 }else if(colProperties[i].equals("Value Attribute__> & <")==true) {
					 colCases[i]=11;
				 }else if(colProperties[i].equals("Value Attribute__>= & <=")==true) {
					 colCases[i]=12;
				 }else if(colProperties[i].equals("Ingore when filtering")==true) {
					 colCases[i]=13;
				 }
			 }
		}else {
			//what if fileFilterTable.get(fileName) does not exist or return null?
			//filters[] will not be invoked in doFiltering() yet colCases[] will
			isNullFilter=true;
			if (isNetwork==true) 
			{
				colCases=new int[2];
				colCases[0]=1;
				colCases[1]=1;
			}else{
				colCases=new int[1];
				colCases[0]=0;
			}
		}
	
	}

	private boolean doFiltering(String line) {
		// TODO Auto-generated method stub
		//empty pass state for each line
		boolean isPass=false;
		/*empty nameStr for each line
		 * nameStr[0] indicates whether it is directed*/
		nameStr=new String[3];
		
		/*if this file has filters and
		 * the user wants to apply filters */
		if (aplFilter==true && isNullFilter==false)
		{
			String[] tokens = line.split("\\t");
			
			for (int i=0; i<tokens.length;i++)
			{
					float f;
					float f2; 
					float f3;
					String[] numbers=new String[2];
			
					switch(colCases[i]){
					case -1: //directly pass this taken
						isPass=true;
						break;
					case 0:  //this token is the label of list element
						nameStr[0]=tokens[i].trim();
						isPass=true;
						break;
					case 1: //this token is the label of one node that composes an undirected edge
						nameStr[0]="undirected";
						//if one node label is already put into nameStr[1], put it into nameStr[2]
						if (nameStr[1]==null)nameStr[1]=tokens[i].trim();
						else nameStr[2]=tokens[i].trim();
						isPass=true;
						break;
					case 2: //this token is the label of FROM node
						nameStr[0]="from_to";
						nameStr[1]=tokens[i].trim();
						isPass=true;
						break;
					case 3: //this token is the label of TO node
						nameStr[0]="from_to";
						nameStr[2]=tokens[i].trim();
						isPass=true;
						break;
					case 4: // if this token contains this filter string, if this filter string contains multiple filters, containMap() will deal with it
						if (tokens[i]!=null && filters[i]!=null && containMap(tokens[i], filters[i])==true)
						{
							isPass=true; 
						}
						else {
							isPass=false;
						}
						break;
					case 5:// if this token equals this filter string, same above, equalMap will deal with it
						if (tokens[i]!=null && filters[i]!=null && equalMap(tokens[i], filters[i])==true)
						{
							isPass=true;
						}
						else{
							isPass=false;
						}
						break;
					case 6: //if this token is greater than this filter number
						if (tokens[i]!=null && filters[i]!=null && (Float.parseFloat(tokens[i].trim())>Float.parseFloat(filters[i].trim())) )
						{
							isPass=true;
						}
						else{
							isPass=false;
						}
						break;
					case 7: //if this token is less than this filter number
						if (tokens[i]!=null && filters[i]!=null && (Float.parseFloat(tokens[i].trim())<Float.parseFloat(filters[i].trim())) )
						{
							isPass=true;
						}
						else{
							isPass=false;
						}
						break;
					case 8: //if this token is equal to  this filter number
						if (tokens[i]!=null && filters[i]!=null && (Float.parseFloat(tokens[i].trim())==Float.parseFloat(filters[i].trim())) )
						{
							isPass=true;
						}
						else{
							isPass=false;
						}
						break;
					case 9: //if this token is greater than or equal to this filter number
						if (tokens[i]!=null && filters[i]!=null && (Float.parseFloat(tokens[i].trim())>=Float.parseFloat(filters[i].trim())) )
						{
							isPass=true;
						}
						else{
							isPass=false;
						}
						break;
					case 10://if this token is less than or equal to this filter number
						if (tokens[i]!=null && filters[i]!=null && (Float.parseFloat(tokens[i].trim())<=Float.parseFloat(filters[i].trim())) )
						{
							isPass=true;
						}
						else{
						isPass=false;
						}
						break;
				
					case 11: // if there are two filter numbers 
						if(filters[i]!=null && tokens[i]!=null)
						{
							numbers= filters[i].split("&");
							f = Float.parseFloat(tokens[i].trim());
							f2=Float.parseFloat(numbers[0].trim());
							f3=Float.parseFloat(numbers[1].trim());
							if (f>f2 && f<f3) isPass=true;
							else isPass=false;
						} else{
							isPass=false;
						}
						break;
					case 12:
						if(filters[i]!=null && tokens[i]!=null)
						{
							numbers= filters[i].split("&");
							f = Float.parseFloat(tokens[i].trim());
							f2=Float.parseFloat(numbers[0].trim());
							f3=Float.parseFloat(numbers[1].trim());
							if (f>=f2 && f<=f3) isPass=true;
							else isPass=false;
						} else{
							isPass=false;
						}
						break;
						// case 13: doing nothing
					case 13:
						isPass=true;
					}// switch end
					if (isPass==false) break;// break from for loop if any token does not pass
				}//for end
		
			
			}else {/* the user does not want to apply filters
			       * or this file does not have filters at all on attributes */
				
				/*if this file has filters but the user does not want to apply them,
				 * it means attributes can be ignored, but the first token and second token
				 * which may denote the element label or edge's node label should be kept in String[] tokens*/
				String[] tokens2= line.split("\\t");
				String[] tokens;
				if (isNullFilter==false) 
				{   /*file has filters, the user doesn't want to apply.
				 	Since we do not know whether the label column is the first and second column, 
				 	we cannot just copy the first and second element*/
					tokens=new String[tokens2.length];
					System.arraycopy(tokens2,0,tokens,0, tokens2.length);
				}else{
					/* file has no filters, default conditions:
					 * 1. file only contain label columns which will be the first and second
					 * 2. the first and second columns will be labels, which is why the user
					 * does not bother specifying column properties*/
					
					if (isNetwork==true) //copy two node labels to tokens
						{
							tokens=new String[2];
							System.arraycopy(tokens2,0,tokens,0, 2);
						}else {
							tokens=new String[1]; //copy list element label to tokens
							System.arraycopy(tokens2,0,tokens,0, 1);
						}
				}
				
				for (int i=0; i<tokens.length;i++)
				{
						float f;
						float f2; 
						float f3;
						String[] numbers=new String[2];
				
						switch(colCases[i]){
						case -1:
							isPass=true;
							break;
						case 0:
							nameStr[0]=tokens[i].trim();
							isPass=true;
							break;
						case 1:
							nameStr[0]="undirected";
							if (nameStr[1]==null)
								nameStr[1]=tokens[i].trim();
							else nameStr[2]=tokens[i].trim();
							isPass=true;
							break;
						case 2:
							nameStr[0]="from_to";
							nameStr[1]=tokens[i].trim();
							isPass=true;
							break;
						case 3:
							nameStr[0]="from_to";
							nameStr[2]=tokens[i].trim();
							isPass=true;
							break;
						}//switch
				}//for
				
			}//the first if-else
		
		return isPass;
		
	}
    /*equalMap() test whether string equals string 2, 
     * or any token that is separated by ";" in string 2*/
	private boolean equalMap(String string, String string2) {
		// TODO Auto-generated method stub
		String[] multiTag=string2.split(";");
		for (int i=0; i<multiTag.length; i++)
		{
			if (string.equalsIgnoreCase(multiTag[i]))
				return true;
		}
		return false;
	}
	/*containMap() test whether string contains string 2, 
     * or any token that is separated by ";" in string 2*/
	private boolean containMap(String string, String string2) {
		// TODO Auto-generated method stub
		//string=tokens[i] in each data line; string2=filters[i] in filterTable
		String[] multiTag=string2.split(";");
		for (int i=0; i<multiTag.length; i++)
		{
			
			if (string.contains(multiTag[i].trim()))
				return true;
		}
		return false;
	}
	
	
	

	//return result
	public Node[] getNodes()
	{
		Node[] nodes=new Node[nodeTable.size()];
		java.util.Iterator<String> it=nodeTable.keySet().iterator();
		int i=0;
		while (it.hasNext())  
		  {  
		  nodes[i]=nodeTable.get(it.next());  
		  i++;
		  }
		Arrays.sort(nodes);
		return nodes; 
		
	}
	
	public Edge[] getEdges()
	{
		Edge[] edges=new Edge[edgeTable.size()];
		java.util.Iterator<String> it=edgeTable.keySet().iterator();
		int i=0;
		while (it.hasNext())  
		  {  
		  edges[i]=edgeTable.get(it.next());  
		  i++;
		  }
		Arrays.sort(edges);
		return edges; 
		
	}
	public int[][] getNodeStat()
	{
		Node[] nodes=this.getNodes();
		int total=1; 
	    int [] CountArr=new int[2]; // an unique count 
	    int [] AmountArr=new int[2];// the frequency of this unique count
	    int add=0;
	    int i=1;
	   
	   
	    for (i=1; i<nodes.length;i++)
	    {  
	    	if(add==CountArr.length-1)// expand it if the array size is not big enough
	    	{
	    		CountArr=(int[])expand(CountArr);
	    		AmountArr=(int[])expand(AmountArr);
	    	}
	    	if (nodes[i]!=null)
	    	{ // node[] is already sorted in ascending order by its count
	    		if (nodes[i-1].getCount()==nodes[i].getCount())
	    		{
	    			total++;
	    			continue;
	    		}else{
	    			CountArr[add]=nodes[i-1].getCount();
	    			AmountArr[add]=total;
	    			total=1; //re-initialize for each unique count;	
	    			add++;
	    			}	
	    		}
	    	}
	    	
	    	//first element
	    	CountArr[add]=nodes[i-1].getCount();
	    	AmountArr[add]=total;
		
	    	int m;
	    	for(m=0; m<CountArr.length; m++)
	    	{
	    		if (CountArr[m]==0)break;
	    	} //m is the actual size of CountArr
		
	    	statNodeArr= new int[m][2];
			
	    	for(int j=0; j<m;j++)
	    	{
	    		int cumuAmount=AmountArr[j];
	    		for(int k=0; k<m; k++)
	    		{
	    			if (CountArr[j]<CountArr[k])	
	    				cumuAmount += AmountArr[k];
	    		}
	    		statNodeArr[j][0]=CountArr[j];
	    		statNodeArr[j][1]=cumuAmount;
	    	}
	    
		
		return statNodeArr;
	}
	
		

	public int[][] getEdgeStat()
	{
		Edge[] edges=this.getEdges();
		int total=1; 
	    int [] CountArr=new int[2]; //an unique count
	    int [] AmountArr=new int[2]; //the frequency of this unique count
	    int add=0;
	    int i=1;
		
		for (i=1; i<edges.length;i++){
			if(add==CountArr.length-1){
				CountArr=(int[])expand(CountArr);
				AmountArr=(int[])expand(AmountArr);
			}
			if (edges[i]!=null){
				// node[] is already sorted in ascending order by its count
				if (edges[i-1].getTotalCount()==edges[i].getTotalCount()){
					total++;
	                continue;
				}else{
					CountArr[add]=edges[i-1].getTotalCount();
					AmountArr[add]=total;
					total=1; //re-initialize for each unique count;	
					add++;
				}	
			}
		}
		CountArr[add]=edges[i-1].getTotalCount();
		AmountArr[add]=total;
		
		int m;
		for(m=0; m<CountArr.length; m++){
			if (CountArr[m]==0)break;
		} //m is the actual size of CountArr
		
		int[][] statEdgeArr = new int[m][2];
			
		for(int j=0; j<m;j++){
			int cumuAmount=AmountArr[j];
			for(int k=0; k<m; k++){
				if (CountArr[j]<CountArr[k])
					cumuAmount += AmountArr[k];
			}
			statEdgeArr[j][0]=CountArr[j];
			statEdgeArr[j][1]=cumuAmount;
		}
		return statEdgeArr;
	}
	
	private int[] expand(int[] a) {
	    int length = a.length;
	    int newLength = length + (length / 2); // 50% more
	    int[] newArray = new int[newLength];
	    System.arraycopy(a, 0, newArray, 0, length);
	    return newArray;
	}

	


    
}
