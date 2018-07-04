package com.greentree.view;

import com.greentree.view.ViewDriver;
import com.greentree.view.Authenticate.AuthController;
import com.greentree.view.Authenticate.AuthJFrame;
import com.greentree.view.Main.MainJFrameController;
import com.greentree.view.Main.MainJFrameDesktop;
//import com.greentree.view.Main.MainJFrameContainer;

/**
 * Loads an instance of <code>{@link AuthJFrame}</code> and adds it to a <code>
 * {@link AuthController}</code>.
 * 
 * @author david.dietrich
 *
 */
public class ViewDriver {
	/**
	 * Builds a new <code>{@link AuthJFrame}</code> and registers handlers for it by passing it 
	 * into a new <code>{@link AuthController}</code>
	 */
	public ViewDriver() {
		new MainJFrameController(new MainJFrameDesktop());
	}
	
	/**
	 * Builds a new <code>{@link ViewDriver}</code> object
	 * 
	 * @param args String[] - not used
	 */
	 public static void main(String[] args) {
	   new ViewDriver();
	 }
}
