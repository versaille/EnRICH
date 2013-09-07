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


public class Node implements Comparable<Object> {
	private String label;
	private int count;
	private Vector<String> source = new Vector<String>();

	public Node(String label)
	{
		this.label=label;
		this.count=1;	
	}
	
	public void addCount()
	{
		count++;
	}
	
	public void addThisCount(int c)
	{
		count=c;
	}
	
	public void addSource(String sr)
	{
		//make sure String vector source does not contain repeats
		int i=0;
		while (source!=null && i<source.size())
		{
			if (source.elementAt(i).equals(sr)==true)
				break;
			else i++;
		}
		if (i==source.size())
		source.add(sr);
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Vector<String> getSource()
	{
		return source;
	}
	
	public String getLabel()
	{
		return label;
	}
	//override this method to implement comparable interface
	public int compareTo(Object o) {
        if (this.count == ((Node)o) .count)
            return 0;
        else if ((this.count) <((Node)o).count)
            return 1;
        else
            return -1;
    }
}
