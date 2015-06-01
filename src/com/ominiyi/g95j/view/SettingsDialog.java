package com.ominiyi.g95j.view;

import com.ominiyi.common.utility.view.GuiUtility;
import com.ominiyi.g95j.utility.G95JUtility;
import com.ominiyi.g95j.view.model.CompilerSettingsInfo;
import com.ominiyi.g95j.view.model.EditorSettingsInfo;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class SettingsDialog extends JDialog {

    private static final long serialVersionUID = 1234L;
    private JTabbedPane tabbedPane = null;
    // compiler settings component
    private JPanel compilerSettingsPanel = null;
    private JLabel g95CompilerPathLabel = null;
    private JTextField g95CompilerPathField = null;
    private JButton g95CompilerPathButton = null;
    private JLabel compilationOutputPathLabel = null;
    private JTextField compilationOutputPathField = null;
    private JButton compilationOutputPathButton = null;
    private JCheckBox overwritePreviousOutputBox = null;
    private JButton applyCompilerSettingsButton = null;
    private JButton restoreCompilerSettingsButton = null;
    // editor settings component
    private JPanel editorSettingsPanel = null;
    private JLabel textEditorFontNameLabel = null;
    private JComboBox textEditorFontNameField = null;
    private JLabel textEditorFontSizeLabel = null;
    private JComboBox textEditorFontSizeField = null;
    private JLabel textEditorFontStyleLabel = null;
    private JComboBox textEditorFontStyleField = null;
    private JButton applyEditorSettingsButton = null;
    private JButton restoreEditorSettingsButton = null;
    private MainWindow frame = null;

    public SettingsDialog(MainWindow frame) throws InvalidPropertiesFormatException,
            ClassNotFoundException, IOException {
        super(frame, true);

        this.frame = frame;
        setResizable(false);
        setTitle("Settings");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        createCompilerSettingsPanel();
        createEditorSettingsPanel();
        tabbedPane.addTab("Compile Setting(s)", compilerSettingsPanel);
        tabbedPane.addTab("Editor Setting(s)", editorSettingsPanel);
        getContentPane().add(tabbedPane);
        setSize(520, 223);
        setLocationRelativeTo(frame);
        loadPreviousCompilerSettings();
        loadPreviousEditorSettings();
    }

    private void createCompilerSettingsPanel() {
        compilerSettingsPanel = new JPanel();

        g95CompilerPathLabel = new JLabel("Path to g95 executable:");
        g95CompilerPathLabel.setSize(200, 20);
        g95CompilerPathLabel.setLocation(10, 20);

        g95CompilerPathField = new JTextField();
        g95CompilerPathField.setSize(140, 20);
        g95CompilerPathField.setLocation(220, 20);
        g95CompilerPathField.setEditable(false);

        g95CompilerPathButton = new JButton("Browse");
        g95CompilerPathButton.setSize(80, 20);
        g95CompilerPathButton.setLocation(360, 20);
        g95CompilerPathButton.setToolTipText("Select the path to the g95 compiler executable");
        g95CompilerPathButton.setActionCommand("g95 path");
        g95CompilerPathButton.addActionListener(new JButtonListener());

        compilationOutputPathLabel = new JLabel("Path to compilation output folder:");
        compilationOutputPathLabel.setSize(200, 20);
        compilationOutputPathLabel.setLocation(10, 50);

        compilationOutputPathField = new JTextField();
        compilationOutputPathField.setSize(140, 20);
        compilationOutputPathField.setLocation(220, 50);
        compilationOutputPathField.setEditable(false);

        compilationOutputPathButton = new JButton("Browse");
        compilationOutputPathButton.setSize(80, 20);
        compilationOutputPathButton.setLocation(360, 50);
        compilationOutputPathButton.setToolTipText("Select a folder to store the compiled file(s)");
        compilationOutputPathButton.setActionCommand("output path");
        compilationOutputPathButton.addActionListener(new JButtonListener());

        overwritePreviousOutputBox = new JCheckBox("Overwrite previous compilation output");
        overwritePreviousOutputBox.setSize(249, 21);
        overwritePreviousOutputBox.setLocation(10, 80);
        overwritePreviousOutputBox.setSelected(true);
        overwritePreviousOutputBox.setVisible(false);

        applyCompilerSettingsButton = new JButton("Apply");
        applyCompilerSettingsButton.setSize(70, 20);
        applyCompilerSettingsButton.setLocation(220, 120);
        applyCompilerSettingsButton.setActionCommand("apply compiler settings");
        applyCompilerSettingsButton.addActionListener(new JButtonListener());

        restoreCompilerSettingsButton = new JButton("Restore Default(s)");
        restoreCompilerSettingsButton.setSize(140, 20);
        restoreCompilerSettingsButton.setLocation(300, 120);
        restoreCompilerSettingsButton.setActionCommand("Restore Default Compiler Settings");
        restoreCompilerSettingsButton.addActionListener(new JButtonListener());

        compilerSettingsPanel.setLayout(null);
        compilerSettingsPanel.add(g95CompilerPathLabel);
        compilerSettingsPanel.add(g95CompilerPathField);
        compilerSettingsPanel.add(g95CompilerPathButton);
        compilerSettingsPanel.add(compilationOutputPathLabel);
        compilerSettingsPanel.add(compilationOutputPathField);
        compilerSettingsPanel.add(compilationOutputPathButton);
        compilerSettingsPanel.add(overwritePreviousOutputBox);
        compilerSettingsPanel.add(applyCompilerSettingsButton);
        compilerSettingsPanel.add(restoreCompilerSettingsButton);
    }

    private void createEditorSettingsPanel() {
        editorSettingsPanel = new JPanel();

        textEditorFontNameLabel = new JLabel("Font:");
        textEditorFontNameLabel.setSize(40, 20);
        textEditorFontNameLabel.setLocation(10, 20);

        textEditorFontNameField = loadAvailableFontName();
        textEditorFontNameField.setSize(140, 20);
        textEditorFontNameField.setLocation(60, 20);
        textEditorFontNameField.setToolTipText("Select a font(Note: not all font(s) are supported)");

        textEditorFontSizeLabel = new JLabel("Size:");
        textEditorFontSizeLabel.setSize(40, 20);
        textEditorFontSizeLabel.setLocation(210, 20);

        textEditorFontSizeField = new JComboBox(new Object[]{"8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"});
        textEditorFontSizeField.setSize(60, 20);
        textEditorFontSizeField.setLocation(260, 20);
        textEditorFontSizeField.setToolTipText("Select A Font Size (Note: Some Font Do Not Support Certain Font Sizes.)");

        textEditorFontStyleLabel = new JLabel("Style:");
        textEditorFontStyleLabel.setSize(40, 20);
        textEditorFontStyleLabel.setLocation(330, 20);

        textEditorFontStyleField = new JComboBox(new Object[]{"PLAIN", "BOLD", "ITALIC", "BOLD & ITALIC"});
        textEditorFontStyleField.setSize(110, 20);
        textEditorFontStyleField.setLocation(380, 20);
        textEditorFontStyleField.setToolTipText("Select A Font Style (Note: Some Fonts Do Not Support Certain Font Styles.)");

        applyEditorSettingsButton = new JButton("Apply");
        applyEditorSettingsButton.setSize(70, 20);
        applyEditorSettingsButton.setLocation(270, 80);
        applyEditorSettingsButton.setActionCommand("apply editor settings");
        applyEditorSettingsButton.addActionListener(new JButtonListener());

        restoreEditorSettingsButton = new JButton("Restore Default(s)");
        restoreEditorSettingsButton.setSize(140, 20);
        restoreEditorSettingsButton.setLocation(350, 80);
        restoreEditorSettingsButton.setActionCommand("Restore Editor Default");
        restoreEditorSettingsButton.addActionListener(new JButtonListener());

        editorSettingsPanel.setLayout(null);
        editorSettingsPanel.add(textEditorFontNameLabel);
        editorSettingsPanel.add(textEditorFontNameField);
        editorSettingsPanel.add(textEditorFontSizeLabel);
        editorSettingsPanel.add(textEditorFontSizeField);
        editorSettingsPanel.add(textEditorFontStyleLabel);
        editorSettingsPanel.add(textEditorFontStyleField);
        editorSettingsPanel.add(applyEditorSettingsButton);
        editorSettingsPanel.add(restoreEditorSettingsButton);
        restoreEditorDefaultSettings();
    }

    class JButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();

            if (actionCommand.equalsIgnoreCase("g95 path")) {
                File file = browseComputer(JFileChooser.FILES_ONLY);
                if (file != null) {
                    g95CompilerPathField.setText(file.getAbsolutePath());
                }
            } else if (actionCommand.equalsIgnoreCase("output path")) {
                File file = browseComputer(JFileChooser.DIRECTORIES_ONLY);
                if (file != null) {
                    compilationOutputPathField.setText(file.getAbsolutePath());
                }
            } else if (actionCommand.equalsIgnoreCase("Restore Default Compiler Settings")) {
                restoreCompilerDefaultSettings();
            } else if (actionCommand.equalsIgnoreCase("Restore Editor Default")) {
                restoreEditorDefaultSettings();
            } else if (actionCommand.equalsIgnoreCase("apply compiler settings")) {
                try {
                    applyCompilerSettings();
                } catch (Exception ex) {
                    Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (actionCommand.equalsIgnoreCase("apply editor settings")) {
                try {
                    applyEditorSettings();
                } catch (Exception ex) {
                    Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void restoreCompilerDefaultSettings() {
        g95CompilerPathField.setText("");
        compilationOutputPathField.setText("");
        overwritePreviousOutputBox.setSelected(true);
    }

    private void restoreEditorDefaultSettings() {
        Font font = FortranEditorScrollPane.getFortranEditorDefaultFont();
        String fontName = font.getName();
        if (fontName.equalsIgnoreCase("Courier")) {
            fontName = "Courier New";// hack: my system does not have Courier
        }

        textEditorFontNameField.setSelectedItem(fontName);
        textEditorFontSizeField.setSelectedItem(String.valueOf(font.getSize()));
        textEditorFontStyleField.setSelectedIndex(font.getStyle());
    }

    public File browseComputer(int browsingMode) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

        fc.setFileSelectionMode(browsingMode);
        fc.setApproveButtonText("Select");
        fc.setApproveButtonMnemonic('S');
        // Set the tool tip
        fc.setApproveButtonToolTipText("Click to select");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            return file;
        }
        return null;
    }

    private JComboBox loadAvailableFontName() {
        String[] envfonts = GuiUtility.getAvailableFonts();

        JComboBox c = new JComboBox(envfonts);
        return c;
    }

    private void applyCompilerSettings() throws FileNotFoundException,
            ClassNotFoundException, IOException {
        CompilerSettingsInfo compilerSettingsInfo = new CompilerSettingsInfo();

        compilerSettingsInfo.setPathToG95Compiler(g95CompilerPathField.getText());
        compilerSettingsInfo.setPathToCompilationOutputFolder(compilationOutputPathField.getText());
        compilerSettingsInfo.setOverWritePreviousOutput(overwritePreviousOutputBox.isSelected());

        boolean settingsSaved = G95JUtility.setCompilerSettingsInfo(compilerSettingsInfo);
        if (settingsSaved == true) {
            JOptionPane.showMessageDialog(this.frame, "Compiler Setting(s) have been saved",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (settingsSaved == false) {
            JOptionPane.showMessageDialog(this.frame, "Compiler Setting(s) could not be saved. Try again later",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyEditorSettings() throws FileNotFoundException,
            ClassNotFoundException, IOException {
        EditorSettingsInfo editorSettingsInfo = new EditorSettingsInfo();

        editorSettingsInfo.setEditorFontName((String) textEditorFontNameField.getSelectedItem());
        try {
            editorSettingsInfo.setEditorFontSize(
                    Integer.parseInt((String) textEditorFontSizeField.getSelectedItem()));
        } catch (NumberFormatException ex) {
            // do nothing
        }
        editorSettingsInfo.setEditorFontStyle(textEditorFontStyleField.getSelectedIndex());
        boolean settingsSaved = G95JUtility.setEditorSettingsInfo(editorSettingsInfo);
        if (settingsSaved == true) {
            String editorFontName = editorSettingsInfo.getEditorFontName();
            int editorFontSize = editorSettingsInfo.getEditorFontSize();
            int editorFontStyle = editorSettingsInfo.getEditorFontStyle();
            if ((editorFontName != null) && (!editorFontName.trim().equals(""))
                    && (editorFontSize > 0) && (editorFontStyle >= 0)) {
                Font font = new Font(editorFontName, editorFontStyle, editorFontSize);
                this.frame.updateAllEditorFonts(font);
            }
            JOptionPane.showMessageDialog(this.frame, "Editor Setting(s) have been saved",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (settingsSaved == false) {
            JOptionPane.showMessageDialog(this.frame, 
                    "Editor Setting(s) could not be saved. Try again later",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPreviousCompilerSettings() throws InvalidPropertiesFormatException,
            ClassNotFoundException, IOException {
        CompilerSettingsInfo compilerSettingsInfo = G95JUtility.getCompilerSettingsInfo();

        if (compilerSettingsInfo == null) {
            restoreCompilerDefaultSettings();
        } else if (compilerSettingsInfo != null) {
            String g95Path = compilerSettingsInfo.getPathToG95Compiler();
            if (g95Path != null) {
                g95CompilerPathField.setText(g95Path);
            }
            String outputFolderPath = compilerSettingsInfo.getPathToCompilationOutputFolder();
            if (outputFolderPath != null) {
                compilationOutputPathField.setText(outputFolderPath);
            }
            boolean overwritePreviousOutput = compilerSettingsInfo.isOverWritePreviousOutput();
            overwritePreviousOutputBox.setSelected(overwritePreviousOutput);
        }
    }

    private void loadPreviousEditorSettings() throws InvalidPropertiesFormatException,
            ClassNotFoundException, IOException {
        EditorSettingsInfo editorSettingsInfo = G95JUtility.getEditorSettingsInfo();

        if (editorSettingsInfo == null) {
            restoreEditorDefaultSettings();
        } else if (editorSettingsInfo != null) {
            String editorFontName = editorSettingsInfo.getEditorFontName();
            if (editorFontName != null) {
                textEditorFontNameField.setSelectedItem(editorFontName);
            }
            int editorFontSize = editorSettingsInfo.getEditorFontSize();
            textEditorFontSizeField.setSelectedItem(String.valueOf(editorFontSize));
            int editorFontStyle = editorSettingsInfo.getEditorFontStyle();
            textEditorFontStyleField.setSelectedIndex(editorFontStyle);
        }
    }
}