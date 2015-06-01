package com.ominiyi.g95j.view;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Adedayo Ominiyi
 */
public class G95JResultScrollPane extends JScrollPane {

    private JTextArea textArea = null;
    
    
    public G95JResultScrollPane() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        getViewport().setOpaque(true);
        setViewportView(textArea);
        //textArea.addKeyListener(inputListener);
    }
    
    public void appendStringToTextArea(String stringToAppend) {
        this.textArea.append(stringToAppend + "\n");
    }
    
    public void clearTextArea() {
        this.textArea.setText("");
    }
    
    public void setTextAreaEditable(boolean editable) {
        this.textArea.setEditable(editable);
    }
    
    /*private KeyListener inputListener = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent ev) {
            char c = ev.getKeyChar();
            ExecutableRunner executableRunner = ExecutableRunner.getFortranExecutableRunner(MainWindow.instanceOf());
            executableRunner.processInput(String.valueOf(c));
        }
    };*/
}
