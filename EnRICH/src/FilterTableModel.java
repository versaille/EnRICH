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

import javax.swing.table.DefaultTableModel;


public class FilterTableModel extends DefaultTableModel
{  
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   Boolean is=false;
	public FilterTableModel()
	{  
		
		
	}
	

	public boolean isCellEditable(int row, int col) {
		if(is==true){
    	  if (col==0 ||(col==2 & row==0)||(col==2 &row==1)) return false;
    	  else return true;
		}else{
			if (col==0 ||(col==2 & row==0)) return false;
	    	  else return true;
		}
		
      }
	public void setDataType(Boolean is2){
		this.is=is2;
		
	}

}
