package com.greentree.view.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * provides options to register, authenticate, view a log, and add a message.
 *
 * @author david5MX53G
 *
 */
public class MainJFrameDesktop extends JFrame {

    /**
     * Eclipse generated this long so the class can implement
     * {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 5392257149460338497L;

    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

    /**
     * {@link JDesktopPane} that contains {@link JInternalFrame} objects added
     * by {@link MainJFrameController}
     */
    JDesktopPane dkPane = new JDesktopPane();

    /**
     * menu items used by {@link MainJFrameController}
     */
    JMenuItem regMI = new JMenuItem("Register");
    JMenuItem authMI = new JMenuItem("Authenticate");
    JMenuItem logMI = new JMenuItem("View Log");
    JMenuItem newMI = new JMenuItem("Add Message");

    /**
     * implements a new {@link MainJInternalFrame} object without a controller.
     * Pass the new <code>
     * MainJFrame</code> to a {@link MainJFrameController} to assign event
     * handlers.
     */
    public MainJFrameDesktop() {
        // build a basic JFrame with a content pane and menu bar
        super("Greentree");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(dkPane);
        setJMenuBar(createMenuBar());

        // position the JFrame
        int inset = 25;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width - inset * 2,
            screenSize.height - inset * 2);

        logger.debug("instantiated successfully");
    }

    /**
     * @return {@link JDesktopPane} contained by this object
     */
    public JDesktopPane getDesktopPane() {
        return this.dkPane;
    }
    
    /**
     * @return {@link JMenuItem} "New" for adding new messages
     */
    public JMenuItem getNewMI() {
        return this.newMI;
    }

    /**
     * @return {@link JMenuItem} "Register" for generating a new {@link com.greentree.model.
     * domain.Token}
     */
    public JMenuItem getRegMI() {
        return this.regMI;
    }

    /**
     * @return {@link JMenuItem} "Authenticate" for authenticating a {@link com.greentree.model.
     * domain.Token}
     */
    public JMenuItem getAuthMI() {
        return this.authMI;
    }

    /**
     * @return {@link JMenuItem} "Open" for opening a log to view messages
     * therein
     */
    public JMenuItem getLogMI() {
        return this.logMI;
    }

    /**
     * set up an empty {@link JMenuBar} with a {@link JMenu}, add
     * {@link JMenuItem} objects to it, and return the result. Each menu item
     * has an action command {@link String} referenced in the
     * {@link MainJFrameController}.
     *
     * @return menu bar with register, authenticate, view log, and add messages
     */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        this.authMI.setActionCommand("auth");
        menu.add(this.authMI);

        this.regMI.setActionCommand("reg");
        menu.add(this.regMI);

        this.logMI.setActionCommand("log");
        menu.add(this.logMI);

        this.newMI.setActionCommand("new");
        menu.add(this.newMI);

        return menuBar;
    }
}
