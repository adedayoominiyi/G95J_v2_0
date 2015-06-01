package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class RedoAllChangeAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public RedoAllChangeAction(MainWindow frame) {
        super("Redo All");
        
        putValue(SHORT_DESCRIPTION, "Click to redo all change(s)");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.invokeTextEditorFeature(G95JTextAreaConstants.REDO_ALL);
    }
}