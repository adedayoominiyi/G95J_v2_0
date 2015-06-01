package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


/**
 *
 * @author Adedayo Ominiyi
 */
public class SelectAllAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public SelectAllAction(MainWindow frame) {
        super("Select All");
        
        putValue(SHORT_DESCRIPTION, "Click to select all text in file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.invokeTextEditorFeature(G95JTextAreaConstants.SELECT_ALL);
    }
}