package com.ominiyi.common.utility.view;

import javax.swing.Action;
import javax.swing.JButton;

/**
 *
 * @author Adedayo Ominiyi
 */
public class ToolBarButton extends JButton {
    
  
    public ToolBarButton(Action action) {
        super(action);
        if (getIcon() != null) {
            setText(""); // an icon-only button
        }
    }
}
