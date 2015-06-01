package com.ominiyi.g95j.view.actions;

import com.ominiyi.common.utility.MyUtility;
import com.ominiyi.g95j.view.MainWindow;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;


/**
 *
 * @author Adedayo Ominiyi
 */
public class SaveAsFileAction extends AbstractAction {

    private static final long serialVersionUID = 1234L;
    private MainWindow frame = null;

    public SaveAsFileAction(MainWindow frame) {
        super("Save File As...", new ImageIcon(MyUtility.getResourceFromAbsolutePath("/com/ominiyi/g95j/view/images/toolbar/SaveAs24.gif")));
        putValue(SHORT_DESCRIPTION, "Click to save the currently selected file with another filename");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.saveSelectedComponentAs();
    }
}