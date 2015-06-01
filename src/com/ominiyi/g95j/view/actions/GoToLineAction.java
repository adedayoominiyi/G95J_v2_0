package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


/**
 *
 * @author Adedayo Ominiyi
 */
public class GoToLineAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public GoToLineAction(MainWindow frame) {
        super("Goto Line...");
        putValue(SHORT_DESCRIPTION, "Click to go to any line in the currently selected editor");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control G"));
       
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.gotoTextAreaLineNumber();
    }
}