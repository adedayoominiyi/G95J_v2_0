package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class CloseFileAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public CloseFileAction(MainWindow frame) {
        super("Close");
        putValue(SHORT_DESCRIPTION, "Click to close the currently selected tab");
       
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.closeTab();
    }
}