package com.ominiyi.g95j.view;

import com.ominiyi.g95j.view.enums.SearchOptionConstants;
import com.ominiyi.g95j.view.model.SearchOptionInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;


public class FindReplaceDialog extends JDialog {

    private static final long serialVersionUID = 1234L;
    private FortranEditorsTabbedPane fortranEditorsTabbedPane = null;
    private JPanel mainPanel = null;
    // main panel components
    private Border redLine = null;
    private TitledBorder directionPanelTitleBorder = null;
    private JLabel findLabel = null;
    private JLabel replaceWithLabel = null;
    private JTextField findField = null;
    private JTextField replaceWithField = null;
    private JButton findButton = null;
    private JButton replaceButton = null;
    private JButton replaceAllButton = null;
    private JPanel directionPanel = null;
    private ButtonGroup directionButtonGroup = null;
    private JRadioButton forwardRadioButton = null;
    private JRadioButton backwardRadioButton = null;
    private JCheckBox matchCaseCheckbox = null;
    private JCheckBox matchEntireWordCheckbox = null;

    public FindReplaceDialog(FortranEditorsTabbedPane fortranEditorsTabbedPane) {
        super(fortranEditorsTabbedPane.getMainWindow(), false);

        setTitle("Find / Replace");
        setResizable(false);
        setLocationRelativeTo(fortranEditorsTabbedPane);
        setSize(435, 223);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.fortranEditorsTabbedPane = fortranEditorsTabbedPane;
        mainPanel = new JPanel();
        initMainPanelComponents();
        addToMainPanelComponents();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, "Center");
    }

    private void initMainPanelComponents() {
        redLine = BorderFactory.createLineBorder(Color.red);
        directionPanelTitleBorder = BorderFactory.createTitledBorder(redLine, "Direction");
        directionPanelTitleBorder.setTitleJustification(TitledBorder.CENTER);
        // find label
        findLabel = new JLabel("Find:");
        findLabel.setSize(90, 20);
        findLabel.setLocation(10, 10);
        // replace With Label
        replaceWithLabel = new JLabel("Replace With:");
        replaceWithLabel.setSize(90, 20);
        replaceWithLabel.setLocation(10, 50);
        // find Field
        findField = new JTextField();
        findField.setSize(130, 20);
        findField.setLocation(120, 10);
        // replace With Field
        replaceWithField = new JTextField();
        replaceWithField.setSize(130, 20);
        replaceWithField.setLocation(120, 50);
        // find Button
        findButton = new JButton("Find");
        findButton.setSize(100, 20);
        findButton.setLocation(280, 10);
        findButton.setActionCommand("Find");
        findButton.addActionListener(new JButtonActionListener());
        // replace Button
        replaceButton = new JButton("Replace");
        replaceButton.setSize(100, 20);
        replaceButton.setLocation(280, 40);
        replaceButton.setActionCommand("Replace");
        replaceButton.addActionListener(new JButtonActionListener());
        // replace all Button
        replaceAllButton = new JButton("Replace All");
        replaceAllButton.setSize(100, 20);
        replaceAllButton.setLocation(280, 70);
        replaceAllButton.setActionCommand("Replace All");
        replaceAllButton.addActionListener(new JButtonActionListener());
        // direction Panel
        directionPanel = new JPanel();
        directionPanel.setBorder(directionPanelTitleBorder);
        directionPanel.setSize(120, 70);
        directionPanel.setLocation(280, 100);
        // direction button group
        directionButtonGroup = new ButtonGroup();
        // forward RadioButton
        forwardRadioButton = new JRadioButton("Forward");
        forwardRadioButton.setSize(90, 20);
        forwardRadioButton.setLocation(10, 13);
        forwardRadioButton.setSelected(true);
        // backward RadioButton
        backwardRadioButton = new JRadioButton("Backward");
        backwardRadioButton.setSize(90, 20);
        backwardRadioButton.setLocation(10, 40);
        // match Case Checkbox
        matchCaseCheckbox = new JCheckBox("Match Case");
        matchCaseCheckbox.setSize(100, 20);
        matchCaseCheckbox.setLocation(30, 100);
        // match Entire Word Checkbox
        matchEntireWordCheckbox = new JCheckBox("Match Entire Word");
        matchEntireWordCheckbox.setSize(139, 20);
        matchEntireWordCheckbox.setLocation(140, 100);
    }

    private void addToMainPanelComponents() {
        mainPanel.setLayout(null);

        mainPanel.add(findLabel);
        mainPanel.add(replaceWithLabel);
        mainPanel.add(findField);
        mainPanel.add(replaceWithField);
        mainPanel.add(findButton);
        mainPanel.add(replaceButton);
        mainPanel.add(replaceAllButton);
        mainPanel.add(directionPanel);
        mainPanel.add(matchCaseCheckbox);
        mainPanel.add(matchEntireWordCheckbox);
        directionPanel.setLayout(null);
        directionPanel.add(forwardRadioButton);
        directionPanel.add(backwardRadioButton);
        directionButtonGroup.add(forwardRadioButton);
        directionButtonGroup.add(backwardRadioButton);
    }

    class JButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("Find")) {
                findText(SearchOptionConstants.FIND);
            } else if (e.getActionCommand().equalsIgnoreCase("Replace")) {
                findText(SearchOptionConstants.REPLACE);
            } else if (e.getActionCommand().equalsIgnoreCase("Replace All")) {
                findText(SearchOptionConstants.REPLACE_ALL);
            }
        }
    }

    private void findText(SearchOptionConstants searchOptionConstants) {
        SearchOptionInfo searchOptionInfo = new SearchOptionInfo();

        searchOptionInfo.setTextToFind(this.findField.getText());
        searchOptionInfo.setTextToUseForReplacment(this.replaceWithField.getText());
        searchOptionInfo.setMatchCase(this.matchCaseCheckbox.isSelected());
        searchOptionInfo.setMatchEntireWord(this.matchEntireWordCheckbox.isSelected());
        searchOptionInfo.setMoveBackwards(this.backwardRadioButton.isSelected());
        
        Component selectedComponent = fortranEditorsTabbedPane.getSelectedComponent();
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranEditorScrollPane fortranEditorScrollPane = (FortranEditorScrollPane) selectedComponent;
            try {
                fortranEditorScrollPane.findOrReplaceText(searchOptionInfo, searchOptionConstants);
            } catch (BadLocationException ex) {
                JOptionPane.showMessageDialog(fortranEditorsTabbedPane.getMainWindow(), 
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}