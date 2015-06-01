package com.ominiyi.g95j.view;

import com.ominiyi.common.utility.view.JConsole;
import com.ominiyi.g95j.utility.ExecutableRunner;
import java.awt.Font;
import javax.swing.JTabbedPane;

/**
 *
 * @author Adedayo Ominiyi
 */
public class G95JResultTabbedPane extends JTabbedPane {

    private G95JResultScrollPane compileResultScrollPane = null;
    //private G95JResultScrollPane runResultScrollPane = null;
    private JConsole runResultScrollPane = null;

    public G95JResultTabbedPane() {
        compileResultScrollPane = new G95JResultScrollPane();
        addTab("Compile Output", compileResultScrollPane);

        //runResultScrollPane = new G95JResultScrollPane();
        runResultScrollPane = new JConsole() {

            @Override
            public void enterKeyWasPressed(String userEntry) {
                ExecutableRunner executableRunner = ExecutableRunner.instanceOf(
                        MainWindow.instanceOf());
                executableRunner.processInput(userEntry);
            }
        };
        runResultScrollPane.setFont(new Font("Monospaced", Font.BOLD, 14));
        //runResultScrollPane.setTextAreaEditable(true);
        addTab("Run Output", runResultScrollPane);
    }

    public void appendStringToCompileTextArea(String stringToAppend) {
        this.compileResultScrollPane.appendStringToTextArea(stringToAppend);
        setSelectedComponent(this.compileResultScrollPane);
    }

    public void appendStringToRunTextAreaWithNewLine(String stringToAppend) {
        this.runResultScrollPane.print(">> " + stringToAppend + "\n");
        setSelectedComponent(this.runResultScrollPane);
        this.runResultScrollPane.requestFocus();
    }
    
    public void appendStringToRunTextArea(String stringToAppend) {
        this.runResultScrollPane.print(">> " + stringToAppend);
        setSelectedComponent(this.runResultScrollPane);
        this.runResultScrollPane.requestFocus();
    }

    public void clearCompileTextArea() {
        this.compileResultScrollPane.clearTextArea();
        setSelectedComponent(this.compileResultScrollPane);
    }

    public void clearRunTextArea() {
        this.runResultScrollPane.clearText();
        setSelectedComponent(this.runResultScrollPane);
    }
}