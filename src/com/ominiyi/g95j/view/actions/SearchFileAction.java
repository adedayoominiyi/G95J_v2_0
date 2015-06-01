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
public class SearchFileAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public SearchFileAction(MainWindow frame) {
        super("Find / Replace", new ImageIcon(MyUtility.getResourceFromAbsolutePath("/com/ominiyi/g95j/view/images/toolbar/Search24.gif")));

        putValue(SHORT_DESCRIPTION, "Click to find / replace");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F"));
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.showFindReplaceDialog();
    }
}