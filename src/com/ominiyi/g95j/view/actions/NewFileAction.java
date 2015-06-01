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
public class NewFileAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public NewFileAction(MainWindow frame) {
        super("New File", new ImageIcon(MyUtility.getResourceFromAbsolutePath("/com/ominiyi/g95j/view/images/toolbar/New24.gif")));

        putValue(SHORT_DESCRIPTION, "Click to create a new file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.createNewFile();
    }
}