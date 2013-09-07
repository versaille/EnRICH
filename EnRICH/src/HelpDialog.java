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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextPane;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class HelpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			HelpDialog dialog = new HelpDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			ImageIcon icon=new ImageIcon("../imageIcons/logo.jpg");
			dialog.setIconImage(icon.getImage());
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public HelpDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setToolTipText("");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		/*JTextPane txtpnThisIs = new JTextPane();
		txtpnThisIs.setText("this is\r\nwaht ");
		GridBagConstraints gbc_txtpnThisIs = new GridBagConstraints();
		gbc_txtpnThisIs.fill = GridBagConstraints.BOTH;
		gbc_txtpnThisIs.gridx = 0;
		gbc_txtpnThisIs.gridy = 0;
		contentPanel.add(txtpnThisIs, gbc_txtpnThisIs);*/
	}
		
		public void ini( String str) //throws IOException
		{
			//ImageIcon icon=new ImageIcon("../imageIcons/logo.jpg");
			//this.setIconImage(icon.getImage());
			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);
			textPane.setText(str);
			
			/*URL url = null;
			try {
				url = new URL (str); //("http://www.public.iastate.edu/~xiazhang/");
				textPane.setPage(url);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				 System.err.println("Attempted to read a bad URL: " + url);
			}*/
		
			JScrollPane editorScrollPane = new JScrollPane(textPane);
			/*editorScrollPane.setVerticalScrollBarPolicy(
			                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);*/
			//editorScrollPane.setPreferredSize(new Dimension(450, 250));
			//editorScrollPane.setMinimumSize(new Dimension(10, 10));
			
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 0;
			gbc_textPane.gridy = 0;
			contentPanel.add(editorScrollPane, gbc_textPane);
		}
		
	
		
}
