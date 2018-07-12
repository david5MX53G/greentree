/*
 * The MIT License
 *
 * Copyright 2018 david5MX53G
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.greentree.view;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author david5MX53G
 */
public class InternalMessageDiag extends JInternalFrame implements ActionListener {
    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();
    
    /** {@link Container}, {@link GridBagLayout}, and {@link GridBagConstraints} members */
    Container cPane = getContentPane();
    GridBagLayout gBagL = new GridBagLayout();
    GridBagConstraints gBagC = new GridBagConstraints();

    /** {@link JButton} and {@link JTextArea} */
    JButton okBtn = new JButton("OK");
    JTextArea textArea = new JTextArea();
    
    public InternalMessageDiag(String title, String msg) {
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okBtn) {
            dispose();
        }
    }
}
