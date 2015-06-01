package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class AboutAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public AboutAction(MainWindow frame) {
        super("About...");

        putValue(SHORT_DESCRIPTION, "Click to view copyright information");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.showAboutDialog();
    }
}