package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class CompilerAndRunAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public CompilerAndRunAction(MainWindow frame) {
        super("Compile / Run File");

        putValue(SHORT_DESCRIPTION, "Click to compile and run the currently selected file");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.compileAndRunFile();
    }
}