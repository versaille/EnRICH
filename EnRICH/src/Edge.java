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

import java.util.Vector;


public class Edge implements Comparable <Object>{
	Node from;
	Node to;
	int totalCount; //the number of all edges between from node and two node
	int count; //the number of undirected edges
	int dirCount; //the number of directed edges
	Vector<String> source=new Vector<String>();
	
	public Edge (Node from, Node to)
	{
	  this.from=from;
	  this.to=to;
	  this.count=0;
	  this.dirCount=0;
	  this.totalCount=0;
	}
	
	public void addSource(String str)
	{
		//make sure String vector source does not contain repeats
		int i=0;
		while (source!=null && i<source.size())
		{
			if (source.elementAt(i).equals(str)==true)
				break;
			else i++;
		}
		if (i==source.size())
		source.add(str);
	}
	
	public void addCount()
	{
		count++;
		totalCount++;
	}
	
	public void addThisCount(int m)
	{
		count=m;
		totalCount=totalCount+m;
	}
	public void addDirCount() {
		// TODO Auto-generated method stub
		dirCount++;
		totalCount++;
	}
	
	public void addThisDirCount(int m)
	{
		dirCount=m;
		totalCount=totalCount+m;
	}
	
	public Node getFrom()
	{
		return from;
	}
	
	public String getFromLabel()
	{
		return from.getLabel();
	}
	
	public Node getTo()
	{
		return to;
	}
	
	public String getToLabel()
	{
		return to.getLabel();
	}
	
	public int getCount()
	{
		return count;
	}
		
	public Vector<String> getSource()
	{
		return source;
	}

	
	public int getDirCount()
	{
		return dirCount;
	}
	
	public int getTotalCount()
	{
		return totalCount;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		 if (this.totalCount == ((Edge)o).totalCount)
	            return 0;
	        else if ((this.totalCount) <( (Edge)o).totalCount)
	            return 1;
	        else
	            return -1;
	}

}
