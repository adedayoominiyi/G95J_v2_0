package com.ominiyi.g95j.view;

import com.ominiyi.common.utility.MyUtility;
import com.ominiyi.g95j.utility.G95JUtility;
import com.ominiyi.g95j.view.enums.G95JTextAreaConstants;
import com.ominiyi.g95j.view.enums.SearchOptionConstants;
import com.ominiyi.g95j.view.model.EditorSettingsInfo;
import com.ominiyi.g95j.view.model.SearchOptionInfo;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author Adedayo Ominiyi
 */
public class FortranEditorScrollPane extends RTextScrollPane {

    private String scrollPaneTitle = null;
    private RSyntaxTextArea fortranEditorTextArea = new RSyntaxTextArea();
    private UndoManager undo = null;

    private FortranEditorScrollPane(String scrollPaneTitle) {
        this.fortranEditorTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        this.fortranEditorTextArea.setHighlightCurrentLine(true);
        this.fortranEditorTextArea.setMarkOccurrences(true);
        this.fortranEditorTextArea.setTabSize(4);

        getViewport().setOpaque(true);
        setViewportView(fortranEditorTextArea);
        this.scrollPaneTitle = scrollPaneTitle;
        setLineNumbersEnabled(true);

        this.undo = new UndoManager();
        this.fortranEditorTextArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                FortranEditorScrollPane.this.undo.addEdit(e.getEdit());
            }
        });
    }

    @Override
    public String toString() {
        return this.scrollPaneTitle;
    }

    public void printTextAreaContent() throws PrinterException {
        if (!this.fortranEditorTextArea.getText().trim().equals("")) {
            MessageFormat footer = new MessageFormat("Page - {0}");
            this.fortranEditorTextArea.print(null, footer, true, null, null, true);
        }
    }

    public void invokeTextEditorFeature(G95JTextAreaConstants textAreaFeatureToInvoke) {
        this.fortranEditorTextArea.requestFocus();
        switch (textAreaFeatureToInvoke) {
            case UNDO:
                undoChange();
                break;
            case REDO_ALL:
                redoAllChange();
                break;
            case COPY:
                this.fortranEditorTextArea.copy();
                break;
            case CUT:
                this.fortranEditorTextArea.cut();
                break;
            case SELECT_ALL:
                this.fortranEditorTextArea.selectAll();
                break;
            case PASTE:
                this.fortranEditorTextArea.paste();
                break;
            case UNDO_ALL:
                undoAllChange();
                break;
            case REDO:
                redoChange();
                break;
            case DELETE:
                this.fortranEditorTextArea.replaceSelection(null);
                break;
            case LOWERCASE:
                changeCaseOfSelectedText(textAreaFeatureToInvoke);
                break;
            case UPPERCASE:
                changeCaseOfSelectedText(textAreaFeatureToInvoke);
                break;
            case TITLE_CASE:
                changeCaseOfSelectedText(textAreaFeatureToInvoke);
                break;
            case INVERT_CASE:
                changeCaseOfSelectedText(textAreaFeatureToInvoke);
                break;
            default:
                throw new IllegalArgumentException();

        }
    }

    private void changeCaseOfSelectedText(G95JTextAreaConstants caseToChangeTo) {
        String selectedText = this.fortranEditorTextArea.getSelectedText();
        if (selectedText != null) {
            String newText = null;
            switch (caseToChangeTo) {
                case LOWERCASE:
                    newText = selectedText.toLowerCase();
                    break;
                case UPPERCASE:
                    newText = selectedText.toUpperCase();
                    break;
                case TITLE_CASE:
                    newText = MyUtility.toTitleCase(selectedText);
                    break;
                case INVERT_CASE:
                    newText = MyUtility.invertCase(selectedText);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            this.fortranEditorTextArea.replaceSelection(newText);
            int caretPosition = this.fortranEditorTextArea.getCaretPosition();
            int selectionStart = caretPosition - selectedText.length();
            this.fortranEditorTextArea.select(selectionStart, caretPosition);
        }
    }

    private void redoChange() {
        if (this.undo.canRedo()) {
            this.undo.redo();
        } else if (!this.undo.canRedo()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void undoAllChange() {
        if (!this.undo.canUndo()) {
            Toolkit.getDefaultToolkit().beep();
        } else if (this.undo.canUndo()) {
            while (this.undo.canUndo()) {
                this.undo.undo();
            }
        }
    }

    private void undoChange() {
        if (this.undo.canUndo()) {
            this.undo.undo();
        } else if (!this.undo.canUndo()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void redoAllChange() {
        if (!this.undo.canRedo()) {
            Toolkit.getDefaultToolkit().beep();
        } else if (this.undo.canRedo()) {
            while (this.undo.canRedo()) {
                this.undo.redo();
            }
        }
    }

    public void appendStringToTextArea(String stringToAppend) {
        //this.fortranEditorTextArea.append(stringToAppend + System.getProperty("line.separator"));// it seems using the system's line separator is a bad idea
        this.fortranEditorTextArea.append(stringToAppend + "\n");

        // hack to prevent newly opened files from having undo/redo functionality
        this.undo.discardAllEdits();
        this.fortranEditorTextArea.discardAllEdits();
    }

    public boolean isNewFile() {
        File file = new File(this.scrollPaneTitle);
        return !(file.exists() && file.isFile());
    }

    public String getScrollPaneTitle() {
        return this.scrollPaneTitle;
    }

    /*public void setScrollPaneTitle(String newScrollPaneTitle) {
    this.scrollPaneTitle = newScrollPaneTitle;
    }*/
    /*public void findOrReplaceText(SearchOptionInfo searchOptionInfo,
    SearchOptionConstants searchOption) {
    SearchEngine.find(this.fortranEditorTextArea, searchOptionInfo.getTextToFind(), 
    !searchOptionInfo.isMoveBackwards(), searchOptionInfo.isMatchCase(), searchOptionInfo.isMatchEntireWord(), false);
    }*/
    public void findOrReplaceText(SearchOptionInfo searchOptionInfo,
            SearchOptionConstants searchOption) throws BadLocationException {
        String textToFind = searchOptionInfo.getTextToFind();
        if ((textToFind == null) || (textToFind.trim().equals(""))) {
            return;
        }

        Document fortranEditorDocument = this.fortranEditorTextArea.getDocument();
        Element fortranEditorDocumentElement = fortranEditorDocument.getDefaultRootElement();

        int rowCount = fortranEditorDocumentElement.getElementCount();
        this.fortranEditorTextArea.requestFocus();
        if (searchOptionInfo.isMoveBackwards() == false) {
            // always start search at caret position
            int caretPosition = this.fortranEditorTextArea.getCaretPosition();
            int firstRowNumber = this.fortranEditorTextArea.getLineOfOffset(caretPosition);
            boolean matchFound = false;
            loop:
            for (int rowNumber = firstRowNumber; rowNumber < rowCount;
                    rowNumber++) {
                matchFound = false;
                Element row = fortranEditorDocumentElement.getElement(rowNumber);
                int firstColumnInRow = row.getStartOffset();
                int lastColumnInRow = row.getEndOffset();
                String editorText = null;
                // read only the necessary text for the first row
                if (rowNumber == firstRowNumber) {
                    editorText = fortranEditorDocument.getText(caretPosition,
                            (lastColumnInRow - caretPosition));
                    matchFound = matchText(searchOptionInfo, editorText,
                            caretPosition, false);
                } else if (rowNumber != firstRowNumber) {
                    editorText = this.fortranEditorTextArea.getDocument().getText(firstColumnInRow,
                            (lastColumnInRow - firstColumnInRow));
                    matchFound = matchText(searchOptionInfo, editorText, firstColumnInRow,
                            false);
                }
                if (matchFound == true) {
                    switch (searchOption) {
                        case FIND:
                            break loop;
                        case REPLACE:
                            this.fortranEditorTextArea.requestFocus();
                            this.fortranEditorTextArea.replaceSelection(searchOptionInfo.getTextToUseForReplacment());
                            break loop;
                        case REPLACE_ALL:
                            this.fortranEditorTextArea.requestFocus();
                            this.fortranEditorTextArea.replaceSelection(searchOptionInfo.getTextToUseForReplacment());
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }
            }
            if (matchFound == false) {
                Toolkit.getDefaultToolkit().beep();
            }
        } else if (searchOptionInfo.isMoveBackwards() == true) {

            // caret position is the end of a selection, 
            // so selection start is used for backward search
            int selectionStart = this.fortranEditorTextArea.getSelectionStart();
            int firstRowNumber = this.fortranEditorTextArea.getLineOfOffset(selectionStart);
            boolean matchFound = false;
            loop:
            for (int rowNumber = firstRowNumber; rowNumber >= 0; rowNumber--) {
                matchFound = false;
                Element row = fortranEditorDocumentElement.getElement(rowNumber);
                int firstColumnInRow = row.getStartOffset();
                int lastColumnInRow = row.getEndOffset();
                String editorText = null;
                if (rowNumber == firstRowNumber) {
                    editorText = fortranEditorDocument.getText(firstColumnInRow,
                            (selectionStart - firstColumnInRow));
                    matchFound = matchText(searchOptionInfo, editorText,
                            firstColumnInRow, true);
                } else if (rowNumber != firstRowNumber) {
                    editorText = this.fortranEditorTextArea.getDocument().getText(firstColumnInRow,
                            (lastColumnInRow - firstColumnInRow));
                    matchFound = matchText(searchOptionInfo, editorText,
                            firstColumnInRow, false);
                }
                if (matchFound == true) {
                    switch (searchOption) {
                        case FIND:
                            break loop;
                        case REPLACE:
                            this.fortranEditorTextArea.requestFocus();
                            this.fortranEditorTextArea.replaceSelection(searchOptionInfo.getTextToUseForReplacment());
                            break loop;
                        case REPLACE_ALL:
                            this.fortranEditorTextArea.requestFocus();
                            this.fortranEditorTextArea.replaceSelection(searchOptionInfo.getTextToUseForReplacment());
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }
            }
            if (matchFound == false) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private boolean matchText(SearchOptionInfo soi,
            String editorText, int rowTextPosition,
            boolean isUpwards) {
        if ((soi == null) || (editorText == null)) {
            return false;
        }

        String findText = soi.getTextToFind();
        if (findText == null) {
            return false;
        }
        if (soi.isMatchCase() == false) {
            editorText = editorText.toLowerCase();
            findText = findText.toLowerCase();
        }
        // replace carriage return / new line xters
        editorText = editorText.replaceAll("\r\n", "");
        editorText = editorText.replaceAll("\r", "");
        editorText = editorText.replaceAll("\n", "");
        int indexOfFindText = -1;
        int selectionStartIndex = 0;
        int selectionEndIndex = 0;
        if (soi.isMatchEntireWord() == false) {
            indexOfFindText = matchTextAllOccurrence(editorText, findText,
                    isUpwards);
        } else if (soi.isMatchEntireWord() == true) {
            indexOfFindText = matchTextEntireWord(editorText, findText,
                    isUpwards);
        }
        if (indexOfFindText >= 0) {
            requestFocus();
            // calculate the selection start and stop based on the 
            // first column in row extracted value
            selectionStartIndex = rowTextPosition + indexOfFindText;
            selectionEndIndex = selectionStartIndex + findText.length();
            this.fortranEditorTextArea.select(selectionStartIndex, selectionEndIndex);
            return true;
        }
        return false;
    }

    private int matchTextAllOccurrence(String editorText, String findText,
            boolean isUpwards) {
        if (isUpwards == false) {
            return editorText.indexOf(findText);
        } else if (isUpwards == true) {
            return editorText.lastIndexOf(findText);
        }
        return -1;
    }

    private int matchTextEntireWord(String editorText, String findText,
            boolean isUpwards) {
        // modify editor string to take note of words at the end of each line
        if (isUpwards == false) {
            return (" " + editorText + " ").indexOf((" " + findText + " "));
        } else if (isUpwards == true) {
            return (" " + editorText + " ").lastIndexOf((" " + findText + " "));
        }
        return -1;
    }

    public void gotoLineNumber(int lineNumberToGoTo) throws BadLocationException {
        if ((lineNumberToGoTo < 0) || (lineNumberToGoTo > (this.fortranEditorTextArea.getLineCount() - 1))) {
            return;
        }
        this.fortranEditorTextArea.requestFocus();
        int startingCaretForLine = this.fortranEditorTextArea.getLineStartOffset(lineNumberToGoTo);
        this.fortranEditorTextArea.setCaretPosition(startingCaretForLine);
    }

    public void setFortranEditorFont(Font font) {
        this.fortranEditorTextArea.setFont(font);
    }

    public static FortranEditorScrollPane createNew(String scrollPaneTitle) {
        FortranEditorScrollPane fortranEditorScrollPane = new FortranEditorScrollPane(scrollPaneTitle);
        try {
            EditorSettingsInfo editorSettingsInfo = G95JUtility.getEditorSettingsInfo();
            Font font = new Font(editorSettingsInfo.getEditorFontName(),
                    editorSettingsInfo.getEditorFontStyle(), editorSettingsInfo.getEditorFontSize());
            fortranEditorScrollPane.setFont(font);
        } catch (Exception ex) {
            // do othing
        }
        return fortranEditorScrollPane;
    }

    public static Font getFortranEditorDefaultFont() {
        FortranEditorScrollPane fortranEditorScrollPane = new FortranEditorScrollPane("");

        return fortranEditorScrollPane.fortranEditorTextArea.getFont();
    }
    
    private boolean running = false;
    public boolean isRunning() {
        return this.running;
    }
}
