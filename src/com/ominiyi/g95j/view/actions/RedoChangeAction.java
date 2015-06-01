package com.ominiyi.g95j.view.actions;

import com.ominiyi.common.utility.MyUtility;
import com.ominiyi.g95j.view.MainWindow;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;


/**
 *
 * @author Adedayo Ominiyi
 */
public class RedoChangeAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public RedoChangeAction(MainWindow frame) {
        super("Redo", new ImageIcon(MyUtility.getResourceFromAbsolutePath("/com/ominiyi/g95j/view/images/toolbar/Redo24.gif")));

        putValue(SHORT_DESCRIPTION, "Click to redo undone changes");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.invokeTextEditorFeature(G95JTextAreaConstants.REDO);
    }
}