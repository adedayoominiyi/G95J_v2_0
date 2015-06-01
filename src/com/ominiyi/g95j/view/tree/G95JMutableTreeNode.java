package com.ominiyi.g95j.view.tree;

import java.awt.Component;
import javax.swing.tree.DefaultMutableTreeNode;

public class G95JMutableTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1234L;
    
    public G95JMutableTreeNode(Component nodeComponent) {
        super(nodeComponent);
    }
    
    public Component getSelectedNodeComponent() {
        return (Component) this.getUserObject();
    }
}