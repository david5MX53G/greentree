package com.greentree.view.ViewLog;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class displays the data of the <code>{@link Token#blockChain}</code> in a scrollable <code>
 * {@link JTextArea}</code> (as demonstrated in <a href=
 * "https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextDemoProject/src/components/TextDemo.java">
 * TextDemo.java</a>).
 * 
 * @author david.dietrich
 *
 */
public class LogJFrame extends JFrame {
	/**
	 * Eclipse has generated this <code>long</code> so the class can implement <code>{@link 
	 * java.io.Serializable}</code>
	 */
	private static final long serialVersionUID = 7689145917555800441L;
	
	/** {@link Container} which holds all other elements in this {@link JFrame} */
	Container cPane = getContentPane();
    
    /**
     * contains the log messages in the <code>{@link Token#blockChain}</code>
     */
    JTextArea textArea = new JTextArea(20, 20);

    /**
     * provides scrolling for <code>{@link LogJFrame#textArea}</code>
     */
    JScrollPane scrollPane = new JScrollPane(textArea);
    
    /**
     * members for {@link GridBagLayout}
     */
    GridBagConstraints gBagC = new GridBagConstraints();
	GridBagLayout gBagL = new GridBagLayout();

    /**
     * Spawns a new {@link LogJFrame} which renders all <code>String</code> objects in the 
     * given <code>Iterator</code> in a JFrame.
     * 
     * @param it {@link Iterator}<{@link String}> displays its contents in the {@link 
     *     LogJFrame#textArea}
     */
    public LogJFrame(Iterator<String> it) {
    	super("Log Messages");
    	this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    	
    	// set up container layout and constraints
    	cPane.setLayout(gBagL);
    	gBagC.ipadx = 250;
		gBagC.gridwidth = GridBagConstraints.REMAINDER;
		gBagC.fill = GridBagConstraints.BOTH;
		gBagC.weightx = 1.0;
		gBagC.weighty = 1.0;
    	
    	// add elements
    	textArea.setEditable(false);
    	cPane.add(scrollPane, gBagC);
    	
    	// draw frame
    	this.pack();
    	this.setVisible(true);
        
        // fill with text
    	if (it.hasNext()) {
    		while (it.hasNext()) {
				textArea.append(it.next() + "\n");
			}
    	} else {
    		textArea.append("no messages");
    	}
    }
}
