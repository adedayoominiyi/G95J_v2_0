package com.ominiyi.g95j.view.actions;

import com.ominiyi.common.utility.MyUtility;
import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;


/**
 *
 * @author Adedayo Ominiyi
 */
public class OpenFileAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public OpenFileAction(MainWindow frame) {
        super("Open File...", new ImageIcon(MyUtility.getResourceFromAbsolutePath("/com/ominiyi/g95j/view/images/toolbar/Open24.gif")));
        putValue(SHORT_DESCRIPTION, "Click to open a file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.selectFileToOpen();
    }
}