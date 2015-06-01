package com.ominiyi.g95j.view;

import com.ominiyi.g95j.view.tree.G95JMutableTreeNode;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Adedayo Ominiyi
 */
public class TreeScrollPane extends JScrollPane {

    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("File(s)");
    private DefaultTreeModel treeModel = new DefaultTreeModel(this.rootNode);
    private JTree tree = new JTree(treeModel);
    private MainWindow mainWindow = null;

    public TreeScrollPane(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.tree.addMouseListener(new TreeMouseListener());
        this.tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.tree.setShowsRootHandles(true);
        getViewport().add(this.tree);
        /*JViewport treeViewPort = new JViewport();
        treeViewPort.add(this.tree);
        setViewport(treeViewPort);*/
    }

    private class TreeMouseListener extends MouseAdapter {

        private JPopupMenu popup = null;

        @Override
        public void mouseReleased(MouseEvent e) {
            popup = createTreePopupMenu(e);
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                TreePath selectedPath = TreeScrollPane.this.tree.getPathForLocation(e.getX(),
                        e.getY());

                TreeScrollPane.this.tree.setSelectionPath(selectedPath);
                Object node = TreeScrollPane.this.tree.getLastSelectedPathComponent();
                if (node != null) {
                    if (node instanceof G95JMutableTreeNode) {
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        }
    }

    private JPopupMenu createTreePopupMenu(MouseEvent e) {
        JMenuItem menuItem = null;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Open File");
        menuItem.setActionCommand("Open File");
        menuItem.addActionListener(new TreePopupMenuListener());
        popup.add(menuItem);
        menuItem = new JMenuItem("Close File");
        menuItem.setActionCommand("Close File");
        menuItem.addActionListener(new TreePopupMenuListener());
        popup.add(menuItem);
        return popup;
    }

    class TreePopupMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equalsIgnoreCase("Open File")) {
                Object node = tree.getLastSelectedPathComponent();
                if ((node != null) && (node instanceof G95JMutableTreeNode)) {
                    G95JMutableTreeNode g95JMutableTreeNode = (G95JMutableTreeNode) node;
                    TreeScrollPane.this.mainWindow.getFortranEditorsTabbedPane().setSelectedComponent(g95JMutableTreeNode.getSelectedNodeComponent());
                }
            } else if (actionCommand.equalsIgnoreCase("Close File")) {
                Object node = tree.getLastSelectedPathComponent();
                if ((node != null) && (node instanceof G95JMutableTreeNode)) {
                    G95JMutableTreeNode g95JMutableTreeNode = (G95JMutableTreeNode) node;
                    Component component = g95JMutableTreeNode.getSelectedNodeComponent();
                    TreeScrollPane.this.mainWindow.getFortranEditorsTabbedPane().closeTab(component);
                    removeChildNode(component);
                }
            }
        }
    }

    public void addChildNode(Component child) {
        G95JMutableTreeNode childNode = new G95JMutableTreeNode(child);
        treeModel.insertNodeInto(childNode, this.rootNode, this.rootNode.getChildCount());
        this.tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }
    
    public void removeChildNode(Component child) {
        int startRow = 0;

        TreePath path = this.tree.getNextMatch(child.toString(), startRow, Position.Bias.Forward);
        if ((path != null)) {
            G95JMutableTreeNode node = (G95JMutableTreeNode) path.getLastPathComponent();
            if (node != null) {
                this.treeModel.removeNodeFromParent(node);
            }
        }
    }
    
    public int getIndexOfNode(Component child) {
        int startRow = 0;

        TreePath path = this.tree.getNextMatch(child.toString(), startRow, Position.Bias.Forward);
        if ((path != null)) {
            G95JMutableTreeNode node = (G95JMutableTreeNode) path.getLastPathComponent();
            return rootNode.getIndex(node);
        }
        return -1;
    }
    
    public void addChildNodeAt(int addAtIndex, Component child) {
        G95JMutableTreeNode childNode = new G95JMutableTreeNode(child);
        treeModel.insertNodeInto(childNode, this.rootNode, addAtIndex);
        this.tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }

    /*public void renameChildNode(String oldTitle, Object newObject) {
        int startRow = 0;

        TreePath path = tree.getNextMatch(oldTitle, startRow, Position.Bias.Forward);
        if (path != null) {
            G95JMutableTreeNode node = (G95JMutableTreeNode) path.getLastPathComponent();
            node.setUserObject(newObject);
            treeModel.valueForPathChanged(path, newObject);
        }
    }*/
}