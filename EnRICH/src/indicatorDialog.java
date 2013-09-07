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
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JRadioButton;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class indicatorDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Boolean ok;
	private int pipe=0;
	private ButtonGroup buttonGroup=new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public void changeOk(Boolean isOk){
		ok=isOk;
	}
	public static void main(String[] args) {
		try {
			indicatorDialog dialog = new indicatorDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public indicatorDialog() {
		this.ok=false;
		setModal(true);
		setTitle("Message");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblMessage = new JLabel("Running is done! You have two options to save result!");
			lblMessage.setFont(new Font("Tahoma", Font.BOLD, 14));
			GridBagConstraints gbc_lblMessage = new GridBagConstraints();
			gbc_lblMessage.gridheight = 2;
			gbc_lblMessage.gridwidth=2;
			gbc_lblMessage.insets = new Insets(0, 0, 5, 0);
			gbc_lblMessage.gridx = 0;
			gbc_lblMessage.gridy = 0;
			contentPanel.add(lblMessage, gbc_lblMessage);
		}
		{
			JRadioButton rdbtnText = new JRadioButton("1.");
			buttonGroup.add(rdbtnText);
			rdbtnText.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					pipe=1;
				}});
			GridBagConstraints gbc_rdbtnText = new GridBagConstraints();
			gbc_rdbtnText.anchor = GridBagConstraints.WEST;
			gbc_rdbtnText.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnText.gridheight=1;
			gbc_rdbtnText.gridwidth=1;
			
			gbc_rdbtnText.gridx = 0;
			gbc_rdbtnText.gridy = 2;
			contentPanel.add(rdbtnText, gbc_rdbtnText);
		}
		{
			JRadioButton rdbtnVisulize = new JRadioButton("2.");
			buttonGroup.add(rdbtnVisulize);
			 rdbtnVisulize.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					pipe=2;
				}});
			{
				JLabel lblNewLabel = new JLabel("Save Result as Text File");
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
				gbc_lblNewLabel.gridx = 1;
				gbc_lblNewLabel.gridy = 2;
				contentPanel.add(lblNewLabel, gbc_lblNewLabel);
			}
			GridBagConstraints gbc_rdbtnVisulize = new GridBagConstraints();
			gbc_rdbtnVisulize.anchor = GridBagConstraints.WEST;
			gbc_rdbtnVisulize.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnVisulize.gridx = 0;
			gbc_rdbtnVisulize.gridy = 3;
			contentPanel.add(rdbtnVisulize, gbc_rdbtnVisulize);
		}
		{
			JLabel lblOpenVisualizationWindow = new JLabel("Visualize Result (Network Only) ");
			GridBagConstraints gbc_lblOpenVisualizationWindow = new GridBagConstraints();
			gbc_lblOpenVisualizationWindow.anchor = GridBagConstraints.WEST;
			gbc_lblOpenVisualizationWindow.insets = new Insets(0, 0, 5, 0);
			gbc_lblOpenVisualizationWindow.gridx = 1;
			gbc_lblOpenVisualizationWindow.gridy = 3;
			contentPanel.add(lblOpenVisualizationWindow, gbc_lblOpenVisualizationWindow);
		}
		{
			JLabel lblNote = new JLabel("Note: You Can Save Text and Image File in Visualization Window");
			GridBagConstraints gbc_lblNote = new GridBagConstraints();
			gbc_lblNote.anchor = GridBagConstraints.WEST;
			gbc_lblNote.gridheight=1;
			gbc_lblNote.gridwidth=2;
			gbc_lblNote.gridx = 0;
			gbc_lblNote.gridy = 4;
			contentPanel.add(lblNote, gbc_lblNote);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
			    okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						changeOk(true);
						setVisible(false);
					}});
				//okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						setVisible(false);
					}});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}

	public int getPipe() {
		// TODO Auto-generated method stub
		return pipe;
	}

	public boolean getOK() {
		// TODO Auto-generated method stub
		return ok;
	}
	

}
