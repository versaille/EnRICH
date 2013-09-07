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
import java.io.FileFilter;
import java.io.FilenameFilter;


public class textFileFilter implements FileFilter {

	@Override
	public boolean accept(File file){
		// TODO Auto-generated method stub
		
		      if (file.getName().toLowerCase().endsWith("txt"))
		      {
		        return true;
		      }
		    
		return false;
	}

}
