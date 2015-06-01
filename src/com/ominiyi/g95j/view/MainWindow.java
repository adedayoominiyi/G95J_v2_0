package com.ominiyi.g95j.view;

import com.ominiyi.common.utility.view.ToolBarButton;
import com.ominiyi.g95j.utility.ExecutableRunner;
import com.ominiyi.g95j.view.actions.AboutAction;
import com.ominiyi.g95j.view.actions.ChangeToLowerCaseAction;
import com.ominiyi.g95j.view.actions.ChangeToTitleCaseAction;
import com.ominiyi.g95j.view.actions.ChangeToUpperCaseAction;
import com.ominiyi.g95j.view.actions.CloseFileAction;
import com.ominiyi.g95j.view.actions.CompilerAndRunAction;
import com.ominiyi.g95j.view.actions.CopySelectionAction;
import com.ominiyi.g95j.view.actions.CutSelectionAction;
import com.ominiyi.g95j.view.actions.DeleteSelectionAction;
import com.ominiyi.g95j.view.actions.ExitApplicationAction;
import com.ominiyi.g95j.view.actions.GoToLineAction;
import com.ominiyi.g95j.view.actions.HelpAction;
import com.ominiyi.g95j.view.actions.InvertCaseAction;
import com.ominiyi.g95j.view.actions.NewFileAction;
import com.ominiyi.g95j.view.actions.OpenFileAction;
import com.ominiyi.g95j.view.actions.PasteSelectionAction;
import com.ominiyi.g95j.view.actions.PrintFileAction;
import com.ominiyi.g95j.view.actions.RedoAllChangeAction;
import com.ominiyi.g95j.view.actions.RedoChangeAction;
import com.ominiyi.g95j.view.actions.SaveAsFileAction;
import com.ominiyi.g95j.view.actions.SaveFileAction;
import com.ominiyi.g95j.view.actions.SearchFileAction;
import com.ominiyi.g95j.view.actions.SelectAllAction;
import com.ominiyi.g95j.view.actions.SettingsAction;
import com.ominiyi.g95j.view.actions.UndoAllChangeAction;
import com.ominiyi.g95j.view.actions.UndoChangeAction;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Adedayo Ominiyi
 */
public class MainWindow extends JFrame {

    private int newFileCounter = 0;
    private FortranEditorsTabbedPane fortranEditorsTabbedPane = null;
    private TreeScrollPane treeScrollPane = null;
    private G95JResultTabbedPane resultTabbedPane = null;
    
    private static class MainWindowHelper {
        public static final MainWindow mainWindow = new MainWindow();
    }
    
    private MainWindow() {
        super("G95J v2.0 - Lightweight Editor for g95");
        Dimension frameSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 30,
                Toolkit.getDefaultToolkit().getScreenSize().height - 80);
        setSize(frameSize);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);

        fortranEditorsTabbedPane = new FortranEditorsTabbedPane(this);
        treeScrollPane = new TreeScrollPane(this);
        resultTabbedPane = new G95JResultTabbedPane();

        getContentPane().add(createDevelopersRegion());
    }

    private JMenuBar createMenuBar() {
        // file menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(new JMenuItem(new NewFileAction(this)));
        fileMenu.add(new JMenuItem(new OpenFileAction(this)));
        fileMenu.add(new JMenuItem(new CloseFileAction(this)));
        fileMenu.add(new JMenuItem(new SaveFileAction(this)));
        fileMenu.add(new JMenuItem(new SaveAsFileAction(this)));
        fileMenu.add(new JMenuItem(new PrintFileAction(this)));
        fileMenu.add(new JMenuItem(new ExitApplicationAction(this)));

        // edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        editMenu.add(new UndoChangeAction(this));
        editMenu.add(new RedoChangeAction(this));
        editMenu.add(new UndoAllChangeAction(this));
        editMenu.add(new RedoAllChangeAction(this));
        editMenu.add(new CutSelectionAction(this));
        editMenu.add(new CopySelectionAction(this));
        editMenu.add(new PasteSelectionAction(this));
        editMenu.add(new DeleteSelectionAction(this));
        editMenu.add(new SelectAllAction(this));
        // change case submenu
        JMenu changeCaseSubmenu = new JMenu("Change Case");
        editMenu.add(changeCaseSubmenu);
        changeCaseSubmenu.add(new ChangeToLowerCaseAction(this));
        changeCaseSubmenu.add(new ChangeToUpperCaseAction(this));
        changeCaseSubmenu.add(new ChangeToTitleCaseAction(this));
        changeCaseSubmenu.add(new InvertCaseAction(this));
        // search menu
        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic('S');
        searchMenu.add(new SearchFileAction(this));
        searchMenu.add(new GoToLineAction(this));
        
        // compiler/run menu
        JMenu compilerAndRunMenu = new JMenu("Compile / Run");
        compilerAndRunMenu.setMnemonic('R');
        compilerAndRunMenu.add(new JMenuItem(new CompilerAndRunAction(this)));

        // settings menu
        JMenu configureMenu = new JMenu("Configure");
        configureMenu.setMnemonic('C');
        configureMenu.add(new SettingsAction(this));

        // help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        helpMenu.add(new HelpAction(this));
        helpMenu.add(new AboutAction(this));

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(searchMenu);
        menuBar.add(compilerAndRunMenu);
        menuBar.add(configureMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JToolBar createToolBar() {
        JToolBar toolbar = new JToolBar();

        JButton newToolItem = new ToolBarButton(new NewFileAction(this));
        toolbar.add(newToolItem);

        JButton openToolItem = new ToolBarButton(new OpenFileAction(this));
        toolbar.add(openToolItem);

        JButton saveToolItem = new ToolBarButton(new SaveFileAction(this));
        toolbar.add(saveToolItem);

        JButton saveAsToolItem = new ToolBarButton(new SaveAsFileAction(this));
        toolbar.add(saveAsToolItem);

        JButton printToolItem = new ToolBarButton(new PrintFileAction(this));
        toolbar.add(printToolItem);

        JButton cutToolItem = new ToolBarButton(new CutSelectionAction(this));
        toolbar.add(cutToolItem);

        JButton copyToolItem = new ToolBarButton(new CopySelectionAction(this));
        toolbar.add(copyToolItem);

        JButton pasteToolItem = new ToolBarButton(new PasteSelectionAction(this));
        toolbar.add(pasteToolItem);

        JButton undoToolItem = new ToolBarButton(new UndoChangeAction(this));
        toolbar.add(undoToolItem);

        JButton redoToolItem = new ToolBarButton(new RedoChangeAction(this));
        toolbar.add(redoToolItem);

        JButton searchToolItem = new ToolBarButton(new SearchFileAction(this));
        toolbar.add(searchToolItem);

        JButton userGuideToolItem = new ToolBarButton(new HelpAction(this));
        toolbar.add(userGuideToolItem);

        return toolbar;
    }

    private JSplitPane createDevelopersRegion() {
        JSplitPane horizontalPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeScrollPane, this.fortranEditorsTabbedPane);
        horizontalPane.setDividerLocation(150);
        horizontalPane.setOneTouchExpandable(true);
        horizontalPane.setBorder(null);

        JSplitPane verticalPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalPane,
                resultTabbedPane);
        verticalPane.setDividerLocation(500);
        verticalPane.setOneTouchExpandable(true);

        return verticalPane;
    }

    public void createNewFile() {
        String tabbedPaneTitle = "Untitled" + ++newFileCounter;

        FortranEditorScrollPane areaScrollPane = FortranEditorScrollPane.createNew(tabbedPaneTitle);
        this.fortranEditorsTabbedPane.addTabToTabbedPane(tabbedPaneTitle, areaScrollPane);
        this.treeScrollPane.addChildNode(areaScrollPane);
        this.fortranEditorsTabbedPane.setSelectedComponent(areaScrollPane);
    }

    public TreeScrollPane getTreeScrollPane() {
        return this.treeScrollPane;
    }

    public FortranEditorsTabbedPane getFortranEditorsTabbedPane() {
        return this.fortranEditorsTabbedPane;
    }

    public void exitApplication() {
        System.exit(0);
    }

    public void printFile() {
        try {
            this.fortranEditorsTabbedPane.printFile();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(MainWindow.this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showUserGuide() {
        JOptionPane.showMessageDialog(MainWindow.this,
                "Help is currently unavailable",
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void invokeTextEditorFeature(G95JTextAreaConstants textAreaFeatureToInvoke) {
        this.fortranEditorsTabbedPane.invokeTextEditorFeature(textAreaFeatureToInvoke);
    }

    public void selectFileToOpen() {
        try {
            this.fortranEditorsTabbedPane.selectFileToOpen();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closeTab() {
        this.fortranEditorsTabbedPane.closeTab();
    }

    public void saveSelectedComponent() {
        try {
            this.fortranEditorsTabbedPane.saveSelectedComponent();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainWindow.this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showAboutDialog() {
        String newLine = System.getProperty("line.separator");
        java.util.Calendar c = java.util.Calendar.getInstance();

        int year = c.get(java.util.Calendar.YEAR);
        int startingYear = 2007;
        String statement1 = "Copyright \u00a9 " + startingYear;
        if (year > startingYear) {
            statement1 = statement1 + " - " + year;
        }
        statement1 = statement1 + ". All Rights Reserved." + newLine;
        String statement2 = "G95J v2.0 - Lightweight Editor for g95" + newLine
                + "Author: Adedayo Ominiyi" + newLine
                + "Email: ominiyi_freeman@yahoo.co.uk";
        String statement = statement1 + statement2;
        JOptionPane.showMessageDialog(this, statement, "About...",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void saveSelectedComponentAs() {
        try {
            this.fortranEditorsTabbedPane.saveSelectedComponentAs();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainWindow.this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void showFindReplaceDialog() {
        this.fortranEditorsTabbedPane.showFindReplaceDialog();
    }
    
    public void gotoTextAreaLineNumber() {
        try {
            this.fortranEditorsTabbedPane.gotoTextAreaLineNumber();
        } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void showSettingsDialog() {
        try {
            SettingsDialog settingsDialog = new SettingsDialog(this);
            settingsDialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     public void updateAllEditorFonts(Font font) {
         this.fortranEditorsTabbedPane.updateAllEditorFonts(font);
     }
     
     public void compileAndRunFile() {
         this.resultTabbedPane.clearCompileTextArea();
         this.resultTabbedPane.clearRunTextArea();
         this.fortranEditorsTabbedPane.compileAndRunFile();
     }

    public G95JResultTabbedPane getResultTabbedPane() {
        return this.resultTabbedPane;
    }
    
    /*public ExecutableRunner getExecutableRunner() {
        return ExecutableRunner.getFortranExecutableRunner(this);
    }*/
    
    public void updateRunOutput() {
        String results = ExecutableRunner.instanceOf(this).getOutput();
        this.resultTabbedPane.appendStringToRunTextArea(results);
    }
    
    public void updateForError(String errors) {
        this.resultTabbedPane.appendStringToRunTextArea(errors);
    }
    
    public static MainWindow instanceOf() {
        MainWindow mainWindow = MainWindowHelper.mainWindow;
        return mainWindow;
    }
}