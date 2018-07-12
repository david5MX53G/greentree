/**
 * 
 */
package com.greentree.view;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 * Spawns a simple <code>{@link JDialog}</code> for giving the user error messages, and so forth.
 * 
 * @author david5MX53G
 *
 */
public class MessageDialog extends JDialog implements ActionListener {
	/**
	 * Eclipse generated this nice <code>{@link long}</code> to implement <code>{@link 
	 * java.io.Serializable}</code>
	 */
	private static final long serialVersionUID = 274842388147411424L;


	/** {@link Container}, {@link GridBagLayout}, and {@link GridBagConstraints} members */
	Container cPane = getContentPane();
	GridBagLayout gBagL = new GridBagLayout();
	GridBagConstraints gBagC = new GridBagConstraints();
	
	/** {@link JButton} and {@link JTextArea} */
	JButton okBtn = new JButton("OK");
	JTextArea textArea = new JTextArea();

	/**
	 * Spawns a small <code>{@link JDialog}</code> suitable for error messaging
	 * 
	 * @param title {@link String} shows in the title bar
	 * @param msg {@link String} shows in the body
	 */
	public MessageDialog(String title, String msg) {
		// set JFrame params
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(title);
		
		// set layout and constraints
		cPane.setLayout(gBagL);
		gBagC.weightx = 10.0;
		gBagC.fill = GridBagConstraints.HORIZONTAL;
		
		// set up label
		textArea.setEditable(false);
		textArea.append(msg);
		gBagC.gridx = 0;
		gBagC.gridy = 0;
		cPane.add(textArea, gBagC);
		
		// position button
		gBagC.gridx = 0;
		gBagC.gridy = 1;
		cPane.add(okBtn, gBagC);
		okBtn.addActionListener(this);
		
		// size and draw
		pack();
		setVisible(true);
		setModal(true);
		setVisible(true);
	}

	/**
	 * Closes this <code>{@link MessageDialog}</code>
	 *
	 * @param e <code>{@link ActionEvent}</code> - used to confirm what element threw the <code>
	 *     ActionEvent</code>
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okBtn) {
                    dispose();
            }
	}

}
