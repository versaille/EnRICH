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
/*this class wraps results of network integration*/
public class ResultWrapper {
	private Node[] nodes;
	private Edge[] edges;
	private int [][] nodeStat;
	private int [][] edgeStat;
	
	public ResultWrapper(Node[] nodes, Edge[] edges,int [][] nodeStat,int [][] edgeStat){
		this.nodes=nodes;
		this.edges=edges;
		this.nodeStat=nodeStat;
		this.edgeStat=edgeStat;
	}
	
	public Node[] getNodes(){
		return nodes;
		
	}
	
	public Edge[] getEdges(){
		return edges;
		
	}
	
	public int[][] getNodeStat(){
		return nodeStat;
		
	}
	
	public int[][] getEdgeStat(){
		return edgeStat;
		
	}

}
