package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class ExitApplicationAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public ExitApplicationAction(MainWindow frame) {
        super("Exit");
        putValue(SHORT_DESCRIPTION, "Click to exit the application");
       
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.exitApplication();
    }
}