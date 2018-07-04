package com.greentree.view.Register;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPublicKey;

import javax.swing.JFileChooser;

import com.greentree.model.business.manager.GreenTreeManager;
import com.greentree.view.MessageDialog;

/**
 * Provides handlers for <code>{@link RegisterJFrame}</code>
 * 
 * @author david.dietrich
 *
 */
public class RegisterController implements ActionListener {	
	/** this is prefixed to log lines from this class */
	private String logPfx;
	
	/**
	 * The <code>{@link com.greentree.view.RegisterJFrame}</code> object sending events to this 
	 * {@link ActionListener}
	 */
	private RegisterJFrame regJFrame;
	
	/**
	 * <code>{@link com.greentree.view.RegisterJFrame}</code> object sending events to the {@link 
	 * ActionListener} of this {@link RegisterController}
	 */
	private RegisterJInternalFrame regJInternalFrame;
	
	/**
	 * Used for sending error messages and such to the user
	 */
	private MessageDialog diag;
	
	/**
	 * Registers handlers for the given <code>{@link RegisterJFrame}</code> <code>{@link JFrame}</code>
	 * 
	 * @param jFrame <code>{@link RegisterJFrame}</code> object sending events to this controller
	 */
	public RegisterController(RegisterJFrame jFrame) {
		logPfx = this.getClass().getSimpleName();
		
		// save the argument to a class variable for future reference
		this.regJFrame = jFrame;
		
		// wire up listeners for the buttons
		this.regJFrame.getCancelBtn().addActionListener(this);
		this.regJFrame.getSubmitBtn().addActionListener(this);
	}

	/**
	 * builds a new {@link RegisterController} to handle events from the given <code>
	 * RegisterJInternalFrame</code>
	 * 
	 * @param iFrame {@link RegisterJInternalFrame} sending events to this controller
	 */
	public RegisterController(RegisterJInternalFrame iFrame) {
		logPfx = this.getClass().getSimpleName();
		this.regJInternalFrame = iFrame;
		this.regJInternalFrame.getCancelBtn().addActionListener(this);
		this.regJInternalFrame.getSubmitBtn().addActionListener(this);
		System.out.println(logPfx + 
				           ": RegisterController(RegisterJInternalFrame iFrame) PASSED");
	}

	/**
	 * Invokes one of the <code>{@link RegisterController}</code> methods based on the <code>
	 * {@link RegisterJFrame}</code> element sending the <code>{@link ActionEvent}</code>
	 * 
	 * @param e <code>ActionEvent</code> used to determine which <code>TokenPrompt</code> element 
	 *     sent the <code>ActionEvent</code>
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(logPfx + ": actionPerformed(ActionEvent) " + e.getActionCommand());
		
		// if this was registered with a RegisterJInternalFrame, use RegisterJInternalFrame methods
	    if (this.regJInternalFrame instanceof RegisterJInternalFrame) {
	    	// fire the cancel button action when the cancel button is clicked
	    	if (e.getSource().equals(regJInternalFrame.getCancelBtn()))
	    		cancelButton_ActionPerformed(e);
	    	
	    	// fire the submit button action when the submit button is clicked
		    if ("Submit".equals(e.getActionCommand()))
		        submitBtn_actionPerformed(e);
	    } 
	    
	    // if this was constructed with a RegisterJFrame, use RegisterJFrame methods
	    else if (this.regJFrame instanceof RegisterJFrame) {
	    	if (e.getSource().equals(regJFrame.getCancelBtn()))
		        cancelButton_ActionPerformed(e);
		    if (e.getSource().equals(regJFrame.getSubmitBtn()))
		        submitBtn_actionPerformed(e);
	    }
	}

	/**
	 * Invokes {@link com.greentree.model.business.manager.GreenTreeManager#registerToken(String)}
	 * using a {@link String} constructed from the value of {@link RegisterJFrame#getPass()}.
	 * 
	 * TODO: replace <code>{@link com.greentree.model.business.manager.GreenTreeManager#registerToken(String)}</code> with 
	 *     <code>{@link com.greentree.model.business.manager.GreenTreeManager#registerToken(char[])}</code>.
	 * 
	 * @param e <code>{@link ActionEvent}</code> should be from <code>{@link 
	 * RegisterJFrame#getSubmitBtn()}</code> <code>JButton</code>
	 */
	private void submitBtn_actionPerformed(ActionEvent ev) {
		String msg = null;
		boolean success = false;
		// initialize the GreenTreeManager Singleton
		GreenTreeManager manager = GreenTreeManager.getInstance();
		if (!GreenTreeManager.loadProperties()) {
			msg = "failed to load properties file";
			System.out.println(logPfx + ": " + msg);
		    new MessageDialog("Error", msg);
		}
		
		success = manager.registerService("TokenService");
		if (!success) {
			System.out.println(logPfx + 
			                   ": manager.registerService(\"TokenService\"); was unsuccessful");
			 new MessageDialog("Error", "failed getting storage input/output");
			 return;
		}
		
		if (this.regJFrame instanceof RegisterJFrame) {
			success = manager.registerToken(new String(regJFrame.getPass()));
		} else if (this.regJInternalFrame instanceof RegisterJInternalFrame) {
			success = manager.registerToken(new String(regJInternalFrame.getPass()));
		}
		if (!success) {
			System.out.println(logPfx + ": " 
			                   + "manager.registerToken(new String(newTokenJFrame.getPass())); "
			                   + "was unsuccessful");
			 new MessageDialog("Error", "failed to save new token");
			 return;
		}
		
		// return the public key for the user to save and use in future authentication
		RSAPublicKey key = manager.getPublicKey();
		JFileChooser fc = new JFileChooser();
		int chooserState;
		
		if (this.regJFrame instanceof RegisterJFrame) {
			chooserState = fc.showSaveDialog(this.regJFrame);
		} else if (this.regJInternalFrame instanceof RegisterJInternalFrame) {
			chooserState = fc.showSaveDialog(this.regJInternalFrame);
		} else {
			System.out.println(this.logPfx + ": JFileChooser failed to get a response");
			return;
		}
		
		if (chooserState == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filePath = file.getAbsolutePath();
            System.out.println("Saving to: " + filePath + ".");
			
			// write the public key to the file chosen by the user in the JFileChooser
			try (ObjectOutputStream out = 
			    new ObjectOutputStream (new FileOutputStream(filePath))) {
				out.writeObject(key);
			} catch (IOException ex) {
				System.out.println(logPfx + ex.getClass().getName() + " writing to file " + 
					filePath + ": " + ex.getMessage());
			    diag = new MessageDialog(logPfx+ex.getClass().getName(), "error 0517");
			    diag.setModal(true);
			    diag.setVisible(true);
			}
        } else {
            System.out.println(logPfx + ": JFileChooser cancelled by user.");
        }
		
		if (this.regJFrame instanceof RegisterJFrame) {
			this.regJFrame.dispose();
		} else if (this.regJInternalFrame instanceof RegisterJInternalFrame) {
			this.regJInternalFrame.dispose();
		}
	}

	private void cancelButton_ActionPerformed(ActionEvent ev) {
		if (this.regJFrame instanceof RegisterJFrame) {
			this.regJFrame.dispose();
		} else if (this.regJInternalFrame instanceof RegisterJInternalFrame) {
			this.regJInternalFrame.dispose();
		}
		System.out.println(logPfx + ": cancelButton_ActionPerformed(ActionEvent) PASSED");
	}

}
