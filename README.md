EnRICH: Extraction & Ranking using Integration and Criteria Heuristics
**********************************************************************

Install
=======
You can chooose to either compile source code (make sure you add the external library in the 'lib' folder), or to download an executable .jar program from http://xiazhang.public.iastate.edu/homepage.html


Features
========
EnRICH can qualitatively integrate heterogeneous networks or list datasets. At the same time, dataset-specific filters can be applied with the user-specified filtering criteria based on attributes from individual datasets.

Specifically, it has the following features:

Excel-like Input Data:
----------------------

EnRICH treats each list or network as a tab-delimited text file which can be easily generated by saving an excel file as a text file. This tab-delimited data table must have column names. EnRICH not only displays all uploaded file names for the user to select, but also shows any selected file as a scrollable data table for the user to browse and edit. This design makes the handling of a batch of files easier particularly when the user wants to browse or edit the files.
 
Easy Specification of Multiple Filters:
---------------------------------------

EnRICH applies a set of attribute-based filtering criteria to each network or list before it is merged with other datasets. EnRICH allows the user to specify of filtering criteria by first choosing the appropriate attribute description from a pull-down menu and then inputting a specific filter.
 
Customization of Integration Pool:
----------------------------------

EnRICH enables the user to select and de-select datasets from a pool of uploaded files. By clicking the mouse, the user can quickly generate alternative combinations of data files that make up the integration pool (files to be integrated). EnRICH also provides a running mode option: with and without filtering. This design avoids re-operation on filters when the user swings between 'integration while filtering' and 'integration only'. 
 
Interactive Visualization of Integrated Network:
-------------------------------------------------
EnRICH enables an interactive visual analysis of the integrated network independent of a third-party visualization software. EnRICH visualization consists of two components: the integrated network and the plot. The plot component has two panels: 1) the number of node vs. node degree and 2) the number of edges vs. edge-reoccurrence plot. All data points in the two plots are clickable to re-draw the integrated network at the selected level of node degree or edge reoccurrence. EnRICH visualization represents the integrated network in a easily-perceived way. First, the edge reoccurrence is denoted as line thickness and the node degree as node size. Second,the hybrid edge between the undirected and the directed is displayed in an overlapping yet distinguishable way. Third, all sources of the integrated edge can be traced back to their original data source by right clicking on the edge. Fourth, node label can be shown or removed at the node-specific level, which allows the user to show labels for part of the network while designating the remaining nodes as unlabeled. 
 
Text Output Ready for other Visualization Tool:
----------------------------------------------

EnRICH also saves results as text files. EnRICH text output provides the user with several options for output content. The user can save the complete result as a text file which can be re-uploaded into EnRICH to visualize the integrated network. The user can also save only a portion of the result. For either complete or partial content, EnRICH text output saves the same type of data in a tab-delimited data table. This design makes the data ready, if desired, for other visualization software.

Run EnRICH
==========
A demo vedio and example dataset can be found at: http://xiazhang.public.iastate.edu/demo.html

How to Prepare Input Data:
--------------------------

1. A network or list is represented by a tab-delimited data table that is stored in one single text file.
2. Each column of this table must represent the same type of information(eg. node label) and has a unique column name.
3. A tab-delimited text file can be generated in Excel by saving the table as text file.

How to Launch EnRICH:
---------------------

1. Download EnRICH.jar and double click EnRICH to launch it.
2. If double-clicking does not work for Mac users, launch it from the Terminal by typing '-java -jar EnRICH.jar' under the directory that holds EnRICH.
3. EnRICH is java application, so the computer should be installed with JRE(JAVA RUNTIME ENVIRONMENT). JRE is already installed on most computers, but you need to check it.

How to Run EnRICH:
-----------------

1. Follow the numerical order of steps in the user interface window.
2. Upload input data in step 1, and then select one file from step 1 file name box for steps 2 and 3. After you finish steps 2 and 3 for each file, go to step 4 to customize your integration pool by selecting and de-selecting file names in step 4 file name box.
3. A dialog box will appear after running is complete. You can choose to save the result as text file or visualize it.

How to Interact with EnRICH visualization:
------------------------------------------

1. Follow the dialog, you should be able to see a visualizaiton window.
2. The left pane is an integrated network while the right pane contains two statistical plots.
3. Click left mouse to show or dissipate the node label, and click right mouse to show or hide the edge label. Nodes and edges can be dragged around to be what you like
4. Data points within the two statistical plots are clickable to update the integrated network.
5. Visualization can be saved as image file. Text output is attainable at this step as well.
