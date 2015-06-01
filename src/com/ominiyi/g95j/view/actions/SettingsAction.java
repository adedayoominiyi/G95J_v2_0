package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class SettingsAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public SettingsAction(MainWindow frame) {
        super("Settings...");

        putValue(SHORT_DESCRIPTION, "Click to setup the application");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.showSettingsDialog();
    }
}