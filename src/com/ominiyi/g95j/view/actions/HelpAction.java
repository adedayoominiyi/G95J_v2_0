package com.ominiyi.g95j.view.actions;

import com.ominiyi.common.utility.MyUtility;
import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;


/**
 *
 * @author Adedayo Ominiyi
 */
public class HelpAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public HelpAction(MainWindow frame) {
        super("User Guide", new ImageIcon(MyUtility.getResourceFromAbsolutePath("/com/ominiyi/g95j/view/images/toolbar/Help24.gif")));

        putValue(SHORT_DESCRIPTION, "Click to view user guide");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.showUserGuide();
    }
}