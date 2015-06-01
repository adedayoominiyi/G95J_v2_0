package com.ominiyi.g95j.view;

import com.ominiyi.common.utility.view.GuiUtility;
import com.ominiyi.g95j.utility.ExecutableRunner;
import com.ominiyi.g95j.utility.G95JUtility;
import com.ominiyi.g95j.view.file.choosers.FortranFileChooser;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import com.ominiyi.g95j.view.model.CompilerSettingsInfo;
import com.ominiyi.g95j.view.tabbedPane.ButtonTabComponent;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;

/**
 *
 * @author Adedayo Ominiyi
 */
public class FortranEditorsTabbedPane extends JTabbedPane {

    private MainWindow mainWindow = null;
    private FindReplaceDialog findReplaceDialog = null;
    private static final int G95_SUCCESS_EXIT_VALUE = 0;
    private Process runningProcess = null;

    public FortranEditorsTabbedPane(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        addMouseListener(new TabbedPaneMouseListener());
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    class TabbedPaneMouseListener extends MouseAdapter {

        private JPopupMenu popup = null;

        @Override
        public void mousePressed(MouseEvent e) {
            popup = createTabbedPanePopupMenu(e);
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            popup = createTabbedPanePopupMenu(e);
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    };

    private JPopupMenu createTabbedPanePopupMenu(MouseEvent e) {
        JMenuItem menuItem = null;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Close File");
        menuItem.setActionCommand("Close File");
        menuItem.addActionListener(new TabbedPanePopupMenuListener(e));
        popup.add(menuItem);
        menuItem = new JMenuItem("Tabs on top");
        menuItem.setActionCommand("Tabs on top");
        menuItem.addActionListener(new TabbedPanePopupMenuListener(e));
        popup.add(menuItem);
        menuItem = new JMenuItem("Tabs at bottom");
        menuItem.setActionCommand("Tabs at bottom");
        menuItem.addActionListener(new TabbedPanePopupMenuListener(e));
        popup.add(menuItem);
        return popup;
    }

    class TabbedPanePopupMenuListener implements ActionListener {

        private MouseEvent evt = null;

        public TabbedPanePopupMenuListener(MouseEvent evt) {
            this.evt = evt;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();

            if (actionCommand.equalsIgnoreCase("Close File")) {
                int index = indexAtLocation(evt.getX(), evt.getY());
                closeTab(index);
            } else if (actionCommand.equalsIgnoreCase("Tabs on top")) {
                setTabPlacement(JTabbedPane.TOP);
            } else if (actionCommand.equalsIgnoreCase("Tabs at bottom")) {
                setTabPlacement(JTabbedPane.BOTTOM);
            }
        }
    }

    public void addTabToTabbedPane(String tabTitle, Component componentToAdd) {
        insertTab(tabTitle, null, componentToAdd, tabTitle, getTabCount());
        setTabComponentAt((getTabCount() - 1), new ButtonTabComponent(this));
    }

    public void closeTab() {
        Component selectedComponent = getSelectedComponent();

        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            closeTab(selectedComponent);
        }
    }

    public void closeTab(int selectIndex) {
        Component selectedComponent = getComponentAt(selectIndex);
        closeTab(selectedComponent);
    }

    public void closeTab(Component selectedComponent) {
        remove(selectedComponent);
        this.mainWindow.getTreeScrollPane().removeChildNode(selectedComponent);
    }

    public void printFile() throws PrinterException {
        Component selectedComponent = getSelectedComponent();

        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
            areaScrollPane.printTextAreaContent();
        }
    }

    public void invokeTextEditorFeature(G95JTextAreaConstants textAreaFeatureToInvoke) {
        Component selectedComponent = getSelectedComponent();
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
            areaScrollPane.invokeTextEditorFeature(textAreaFeatureToInvoke);
        }
    }

    public FortranEditorScrollPane saveSelectedComponent() throws IOException {
        Component selectedComponent = getSelectedComponent();
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
            if (areaScrollPane.isNewFile() == true) {
                // show save dialog
                return showSaveDialog(areaScrollPane);
            } else if (areaScrollPane.isNewFile() == false) {
                // simply save the file
                File fileToSaveTo = new File(areaScrollPane.getScrollPaneTitle());
                return saveSelectedComponentToFile(areaScrollPane, fileToSaveTo);
            }
        }
        throw new IllegalArgumentException();
    }

    private FortranEditorScrollPane showSaveDialog(Component selectedComponent) throws IOException {
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranFileChooser fc = new FortranFileChooser(System.getProperty("user.dir"));
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == FortranFileChooser.APPROVE_OPTION) {
                File fileToSaveTo = fc.getSelectedFile();
                String absoluteFileName = fileToSaveTo.getAbsolutePath();

                if ((!absoluteFileName.toLowerCase().endsWith(".f"))
                        && (!absoluteFileName.toLowerCase().endsWith(".for"))
                        && (!absoluteFileName.toLowerCase().endsWith(".f90"))
                        && (!absoluteFileName.toLowerCase().endsWith(".f95"))
                        && (!absoluteFileName.toLowerCase().endsWith(".f03"))) {
                    String description = fc.getFileFilter().getDescription();
                    if (description.equalsIgnoreCase("Fortran 77 Files")) {
                        absoluteFileName = absoluteFileName + ".f";
                    } else if (description.equalsIgnoreCase("Fortran 90 Files")) {
                        absoluteFileName = absoluteFileName + ".f90";
                    } else if (description.equalsIgnoreCase("Fortran 95 Files")) {
                        absoluteFileName = absoluteFileName + ".f95";
                    } else if (description.equalsIgnoreCase("Fortran 2003 Files")) {
                        absoluteFileName = absoluteFileName + ".f03";
                    }
                }
                fileToSaveTo = new File(absoluteFileName);
                if (fileToSaveTo.exists()) {
                    int answer = JOptionPane.showConfirmDialog(this, "Overwrite existing file", "Information",
                            JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        return saveSelectedComponentToFile(selectedComponent, fileToSaveTo);
                    } else if (answer == JOptionPane.NO_OPTION) {
                        showSaveDialog(selectedComponent);
                    }
                } else if (!fileToSaveTo.exists()) {
                    return saveSelectedComponentToFile(selectedComponent, fileToSaveTo);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private FortranEditorScrollPane saveSelectedComponentToFile(Component selectedComponent, File fileToSaveTo) throws IOException {
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
            JTextArea textArea = areaScrollPane.getTextArea();
            try {
                textArea.setEditable(false);
                String textAreaContent = textArea.getText();
                //SaveSelectedComponentToFileTask saveSelectedComponentToFileTask = new SaveSelectedComponentToFileTask(textAreaContent, fileToSaveTo, areaScrollPane);
                //saveSelectedComponentToFileTask.execute();
                FileUtils.writeStringToFile(fileToSaveTo, textAreaContent, "UTF-8");
                // hack: simply updating the components does not seem to work well
                // so i had to recreate them
                int indexOfComponent = FortranEditorsTabbedPane.this.indexOfComponent(areaScrollPane);

                if (indexOfComponent >= 0) {
                    FortranEditorsTabbedPane.this.remove(areaScrollPane);
                    String newTitle = fileToSaveTo.getAbsolutePath();
                    FortranEditorScrollPane newAreaScrollPane = FortranEditorScrollPane.createNew(newTitle);
                    newAreaScrollPane.getTextArea().setText(textAreaContent);
                    FortranEditorsTabbedPane.this.addTabToTabbedPaneAt(indexOfComponent, newTitle, newAreaScrollPane);
                    TreeScrollPane treeScrollPane = FortranEditorsTabbedPane.this.mainWindow.getTreeScrollPane();
                    int indexOfOldComponent = treeScrollPane.getIndexOfNode(areaScrollPane);
                    treeScrollPane.removeChildNode(areaScrollPane);
                    treeScrollPane.addChildNodeAt(indexOfOldComponent, newAreaScrollPane);
                    FortranEditorsTabbedPane.this.setSelectedComponent(newAreaScrollPane);
                    return newAreaScrollPane;
                }
            } finally {
                textArea.setEditable(true);
            }
        }
        throw new IllegalArgumentException();
    }

    /*private class SaveSelectedComponentToFileTask extends SwingWorker<Void, Void> {
    
    private String textAreaContent = null;
    private File fileToSaveTo = null;
    private FortranEditorScrollPane areaScrollPane = null;
    
    public SaveSelectedComponentToFileTask(String textAreaContent,
    File fileToSaveTo, FortranEditorScrollPane areaScrollPane) {
    this.textAreaContent = textAreaContent;
    this.fileToSaveTo = fileToSaveTo;
    this.areaScrollPane = areaScrollPane;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
    FileUtils.writeStringToFile(fileToSaveTo, textAreaContent, "UTF-8");
    return null;
    }
    
    @Override
    public void done() {
    // hack: simply updating the components does not seem to work well
    // so i had to recreate them
    int indexOfComponent = FortranEditorsTabbedPane.this.indexOfComponent(areaScrollPane);
    
    if (indexOfComponent >= 0) {
    FortranEditorsTabbedPane.this.remove(areaScrollPane);
    String newTitle = fileToSaveTo.getAbsolutePath();
    FortranEditorScrollPane newAreaScrollPane = FortranEditorScrollPane.createNew(newTitle);
    newAreaScrollPane.getTextArea().setText(textAreaContent);
    FortranEditorsTabbedPane.this.addTabToTabbedPaneAt(indexOfComponent, newTitle, newAreaScrollPane);
    TreeScrollPane treeScrollPane = FortranEditorsTabbedPane.this.mainWindow.getTreeScrollPane();
    int indexOfOldComponent = treeScrollPane.getIndexOfNode(areaScrollPane);
    treeScrollPane.removeChildNode(areaScrollPane);
    treeScrollPane.addChildNodeAt(indexOfOldComponent, newAreaScrollPane);
    FortranEditorsTabbedPane.this.setSelectedComponent(newAreaScrollPane);
    }
    
    //int indexOfComponent = EditorsTabbedPane.this.indexOfComponent(areaScrollPane);
    //if (indexOfComponent >= 0) {
    //EditorsTabbedPane.this.setTitleAt(indexOfComponent, newTitle);
    //EditorsTabbedPane.this.setToolTipTextAt(indexOfComponent, newTitle);
    //areaScrollPane.setScrollPaneTitle(newTitle);
    //}
    }
    }*/
    public void addTabToTabbedPaneAt(int addAtIndex, String tabTitle, Component componentToAdd) {
        insertTab(tabTitle, null, componentToAdd, tabTitle, addAtIndex);
        setTabComponentAt((getTabCount() - 1), new ButtonTabComponent(this));
    }

    public void saveSelectedComponentAs() throws IOException {
        Component selectedComponent = getSelectedComponent();
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
            // always show save dialog
            showSaveDialog(areaScrollPane);
        }
    }

    public void selectFileToOpen() throws Exception {
        FortranFileChooser fc = new FortranFileChooser(System.getProperty("user.dir"));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == FortranFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            int indexOfTab = indexOfTab(file.getAbsolutePath());
            if (indexOfTab >= 0) {
                setSelectedIndex(indexOfTab);
            } else if (indexOfTab < 0) {
                new OpenFileInEditorTask(file).execute();
            }
        }
    }

    private class OpenFileInEditorTask extends SwingWorker<Void, String> {

        private FortranEditorScrollPane areaScrollPane = null;
        private String tabbedPaneTitle = null;
        private LineIterator lineIterator = null;

        public OpenFileInEditorTask(File fileToOpen) throws Exception {
            this.tabbedPaneTitle = fileToOpen.getAbsolutePath();
            this.areaScrollPane = FortranEditorScrollPane.createNew(this.tabbedPaneTitle);
            this.lineIterator = FileUtils.lineIterator(fileToOpen, "UTF-8");
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                while (this.lineIterator.hasNext()) {
                    String line = this.lineIterator.nextLine();
                    publish(line);
                }
            } finally {
                LineIterator.closeQuietly(this.lineIterator);
            }

            return null;
        }

        @Override
        protected void process(List<String> readStrings) {
            for (String readString : readStrings) {
                //readString = readString.replaceAll("\r\n", "");
                //readString = readString.replaceAll("\r", "");
                //readString = readString.replaceAll("\n", "");
                this.areaScrollPane.appendStringToTextArea(readString);
            }
        }

        @Override
        public void done() {
            FortranEditorsTabbedPane.this.addTabToTabbedPane(this.tabbedPaneTitle,
                    this.areaScrollPane);
            FortranEditorsTabbedPane.this.mainWindow.getTreeScrollPane().addChildNode(this.areaScrollPane);
            FortranEditorsTabbedPane.this.setSelectedComponent(this.areaScrollPane);
            this.areaScrollPane.getTextArea().moveCaretPosition(0);// move caret to top
            this.areaScrollPane.getTextArea().select(0, 0);// hack: needed to highlight the first line after caret is moved
            this.areaScrollPane.getTextArea().requestFocus();// hack: needed to show the blinking caret
        }
    }

    public void showFindReplaceDialog() {
        Component selectedComponent = getSelectedComponent();
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            if (this.findReplaceDialog == null) {
                this.findReplaceDialog = new FindReplaceDialog(this);
            }
            this.findReplaceDialog.setVisible(true);
        }
    }

    public MainWindow getMainWindow() {
        return this.mainWindow;
    }

    public void gotoTextAreaLineNumber() throws BadLocationException {
        Component selectedComponent = getSelectedComponent();
        if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
            String lineNumberString = JOptionPane.showInputDialog(this.mainWindow, "Goto Line: ",
                    "Goto Line...", JOptionPane.PLAIN_MESSAGE); // yes - returns blank, no returns null
            if (lineNumberString != null) {
                try {
                    int lineNumber = Integer.parseInt(lineNumberString.trim());
                    FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
                    if ((lineNumber <= 0) || (lineNumber > areaScrollPane.getTextArea().getLineCount())) {
                        JOptionPane.showMessageDialog(this, "Line Number is invalid",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        gotoTextAreaLineNumber();
                    } else {
                        areaScrollPane.gotoLineNumber((lineNumber - 1));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Line Number is not a valid number",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    gotoTextAreaLineNumber();
                }
            }
        }
    }

    public void updateAllEditorFonts(Font font) {
        int numberOfAvailableTabs = getTabCount();
        for (int index = 0; index < numberOfAvailableTabs; index++) {
            Component selectedComponent = getComponentAt(index);
            if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
                FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
                areaScrollPane.setFortranEditorFont(font);
            }
        }
    }

    public void compileAndRunFile() {
        try {
            CompilerSettingsInfo compilerSettingsInfo = G95JUtility.getCompilerSettingsInfo();
            if (compilerSettingsInfo == null) {
            } else if (compilerSettingsInfo != null) {
                String pathToG95Compiler = compilerSettingsInfo.getPathToG95Compiler();
                if ((pathToG95Compiler == null) || (pathToG95Compiler.trim().equals(""))) {
                    String errorMessage = "g95 compiler cannot be found."
                            + GuiUtility.newLine() + "Probably you haven't set the path in the "
                            + "Settings submenu of the Configure menu.";
                    JOptionPane.showMessageDialog(this.mainWindow, errorMessage,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else if ((pathToG95Compiler != null) && (!pathToG95Compiler.trim().equals(""))) {
                    Component selectedComponent = getSelectedComponent();
                    if ((selectedComponent != null) && (selectedComponent instanceof FortranEditorScrollPane)) {
                        FortranEditorScrollPane areaScrollPane = (FortranEditorScrollPane) selectedComponent;
                        compileAndRunFile(areaScrollPane, compilerSettingsInfo);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.mainWindow, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void compileAndRunFile(FortranEditorScrollPane areaScrollPane,
            CompilerSettingsInfo compilerSettingsInfo) throws IOException {
        boolean isNewFile = areaScrollPane.isNewFile();
        if (isNewFile == true) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "Changes need to be saved." + GuiUtility.newLine() + "Save File?",
                    "Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                areaScrollPane = saveSelectedComponent();
                compileAndRunFile(areaScrollPane, compilerSettingsInfo);
            }
        } else if (isNewFile == false) {
            compileFile(areaScrollPane, compilerSettingsInfo);
        }
    }

    private void compileFile(final FortranEditorScrollPane areaScrollPane,
            final CompilerSettingsInfo compilerSettingsInfo) {
        final File fileToCompile = new File(areaScrollPane.getScrollPaneTitle());
        if (!fileToCompile.exists()) {
            return;
        }
        if (this.runningProcess != null) {
            runningProcess.destroy();
        }

        FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                "Compiling File " + fileToCompile.getAbsolutePath());
        FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                "");
        Map map = new HashMap();
        map.put("fileToCompile", fileToCompile);
        File pathToG95Compiler = new File(compilerSettingsInfo.getPathToG95Compiler());
        String g95DirectoryName = pathToG95Compiler.getParentFile().getParent();
        //System.out.println("g95DirectoryName: " + g95DirectoryName);
        String outputFilename = FilenameUtils.getBaseName(fileToCompile.getAbsolutePath());// remove the extension as well so G95 can create the proper executable
        //System.out.println("outputFilename: " + outputFilename);

        CommandLine cmdLine = new CommandLine(pathToG95Compiler);
        cmdLine.addArgument("-B" + g95DirectoryName + File.separator + "bin" + File.separator, true);
        cmdLine.addArgument("-B" + (g95DirectoryName + File.separator + "lib" + File.separator), true);
        cmdLine.addArgument("-o" + outputFilename, true);
        cmdLine.addArgument("${fileToCompile}", true);
        cmdLine.setSubstitutionMap(map);
        //System.out.println("cmdLine: " + cmdLine);

        //Takes System.out for dumping the output and System.err for Error
        final ByteArrayOutputStream outputBaos = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorBaos = new ByteArrayOutputStream();
        final PumpStreamHandler streamHandler = new PumpStreamHandler(outputBaos, errorBaos);

        //Result Handler for executing the process in a Asynch way
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {

            @Override
            public void onProcessComplete(int exitValue) {
                // seems this method is only called when exit value is success
                //System.out.println("exitValue: " + exitValue);
                if (exitValue == G95_SUCCESS_EXIT_VALUE) {
                    FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                            "The file has been successfully compiled");
                    FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                            new String(outputBaos.toByteArray()));
                    try {
                        runFile(areaScrollPane, compilerSettingsInfo);
                    } catch (IOException ex) {
                        FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                                ex.getMessage());
                    }
                }
            }

            @Override
            public void onProcessFailed(ExecuteException ex) {
                //int exitValue = ex.getExitValue();
                //System.out.println("exitValue: " + exitValue);
                //ex.printStackTrace();
                FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                        ex.getMessage());
                FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                        new String(errorBaos.toByteArray()));
            }
        };

        //This is used to end the process when the JVM exits
        ShutdownHookProcessDestroyer processDestroyer = new ShutdownHookProcessDestroyer();

        //Infinite timeout
        ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setWatchdog(watchdog);
        executor.setProcessDestroyer(processDestroyer);
        executor.setStreamHandler(streamHandler);

        try {
            executor.execute(cmdLine, resultHandler);
        } catch (Exception ex) {
            //ex.printStackTrace();
            FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToCompileTextArea(
                    ex.getMessage());
        }
    }

    /*private void runFile(FortranEditorScrollPane areaScrollPane,
    CompilerSettingsInfo compilerSettingsInfo) throws IOException {
    FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().clearRunTextArea();
    final File selectedComponentFile = new File(areaScrollPane.getScrollPaneTitle());
    if (!selectedComponentFile.exists()) {
    return;
    }
    String filenameToRun = FilenameUtils.getBaseName(selectedComponentFile.getAbsolutePath());// remove the extension as well so the right executable can be used
    //System.out.println("filenameToRun: " + filenameToRun);
    String pathToCompilationOutputFolder = compilerSettingsInfo.getPathToCompilationOutputFolder();
    if ((pathToCompilationOutputFolder != null) && (!pathToCompilationOutputFolder.trim().equals(""))) {
    if (!new File(pathToCompilationOutputFolder).exists()) {
    pathToCompilationOutputFolder = System.getProperty("user.dir");
    }
    } else {
    pathToCompilationOutputFolder = System.getProperty("user.dir");
    }
    File executableFileDirectory = new File(pathToCompilationOutputFolder);
    //System.out.println("executableFileDirectory.getAbsolutePath(): " + executableFileDirectory.getAbsolutePath());
    String executableFilePath = executableFileDirectory.getAbsolutePath() + File.separator + filenameToRun;
    //System.out.println("executableFilePath: " + executableFilePath);
    //System.out.println("executable file exists: " + new File(executableFilePath).exists());
    FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextArea(
    "Running File " + executableFilePath);
    FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextArea(
    "");
    
    CommandLine cmdLine = new CommandLine(executableFilePath);
    
    //Takes System.out for dumping the output and System.err for Error
    //final ByteArrayOutputStream outputBaos = new ByteArrayOutputStream();
    final PipedOutputStream outputPipe = new PipedOutputStream() {
    
    @Override
    public void write(byte[] b) throws IOException {
    super.write(b);
    flush();
    //close();
    }
    
    @Override
    public void write(int b) throws IOException {
    super.write(b);
    flush();
    //close();
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
    super.write(b, off, len);
    flush();
    //close();
    }
    };
    //final PipedInputStream inputPipe = new PipedInputStream(outputPipe);
    final PumpStreamHandler streamHandler = new PumpStreamHandler(outputPipe, outputPipe);
    streamHandler.setProcessInputStream(outputPipe);
    
    //This is used to end the process when the JVM exits
    ShutdownHookProcessDestroyer processDestroyer = new ShutdownHookProcessDestroyer();
    
    DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValue(0);
    executor.setProcessDestroyer(processDestroyer);
    executor.setStreamHandler(streamHandler);
    
    try {
    DataInputStream is = new DataInputStream(new PipedInputStream(outputPipe));
    PipeParser p = new PipeParser(is);
    p.start();
    executor.execute(cmdLine);
    } catch (Exception ex) {
    //ex.printStackTrace();
    FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextArea(
    ex.getMessage());
    }
    }
    
    class PipeParser extends Thread {
    
    private InputStream is;
    
    public PipeParser(InputStream is) {
    this.is = is;
    }
    
    @Override
    public void run() {
    try {
    BufferedReader bufferedReader = new BufferedReader(
    new InputStreamReader(is));
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
    System.out.println("line: " + line);
    }
    } catch (Exception ex) {
    ex.printStackTrace();
    }
    }
    }*/
    
    private void runFile(FortranEditorScrollPane areaScrollPane,
            CompilerSettingsInfo compilerSettingsInfo) throws IOException {
        FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().clearRunTextArea();
        final File selectedComponentFile = new File(areaScrollPane.getScrollPaneTitle());

        if (!selectedComponentFile.exists()) {
            return;
        }
        String filenameToRun = FilenameUtils.getBaseName(selectedComponentFile.getAbsolutePath());// remove the extension as well so the right executable can be used
        //System.out.println("filenameToRun: " + filenameToRun);
        String pathToCompilationOutputFolder = compilerSettingsInfo.getPathToCompilationOutputFolder();
        if ((pathToCompilationOutputFolder != null) && (!pathToCompilationOutputFolder.trim().equals(""))) {
            if (!new File(pathToCompilationOutputFolder).exists()) {
                pathToCompilationOutputFolder = System.getProperty("user.dir");
            }
        } else {
            pathToCompilationOutputFolder = System.getProperty("user.dir");
        }
        File executableFileDirectory = new File(pathToCompilationOutputFolder);
        //System.out.println("executableFileDirectory.getAbsolutePath(): " + executableFileDirectory.getAbsolutePath());
        String executableFilePath = executableFileDirectory.getAbsolutePath() + File.separator + filenameToRun;
        //System.out.println("executableFilePath: " + executableFilePath);
        //System.out.println("executable file exists: " + new File(executableFilePath).exists());
        FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextAreaWithNewLine(
                "Running File " + executableFilePath);
        FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextAreaWithNewLine(
                "");

        CommandLine cmdLine = new CommandLine(executableFilePath);// take advantage of commons exec command parser
        Runtime rt = Runtime.getRuntime();
        // pad command with inverted commas
        //final Process process = rt.exec("\"" + executableFilePath + "\"");
        //final Process runningProcess = rt.exec(cmdLine.toString());
        this.runningProcess = rt.exec(cmdLine.toString());
        ExecutableRunner executableRunner = ExecutableRunner.instanceOf(this.mainWindow);
        synchronized (executableRunner) {
            executableRunner.execute(this.runningProcess);
        }
        try {
            int exitValue = this.runningProcess.waitFor();
            System.out.println("exitValue: " + exitValue);
            if (exitValue == G95_SUCCESS_EXIT_VALUE) {
                FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextArea(
                                "Program has successfully finished executing");
            } else {
                FortranEditorsTabbedPane.this.mainWindow.getResultTabbedPane().appendStringToRunTextArea(
                                "Program Terminated with error value " + exitValue);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            this.runningProcess.destroy();
        } finally {
            // see http://bugs.sun.com/view_bug.do?bug_id=6420270
            // see https://issues.apache.org/jira/browse/EXEC-46
            // Process.waitFor should clear interrupt status when throwing InterruptedException
            // but we have to do that manually
            Thread.interrupted();
        }

    }
}
