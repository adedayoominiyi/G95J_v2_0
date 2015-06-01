package com.ominiyi.g95j.view.actions;

import com.ominiyi.g95j.view.MainWindow;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 *
 * @author Adedayo Ominiyi
 */
public class ChangeToUpperCaseAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public ChangeToUpperCaseAction(MainWindow frame) {
        super("Uppercase");
        
        putValue(SHORT_DESCRIPTION, "Click to change selected text to uppercase");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.invokeTextEditorFeature(G95JTextAreaConstants.UPPERCASE);
    }
}