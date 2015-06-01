package com.ominiyi.g95j.view.model;

/**
 *
 * @author Adedayo Ominiyi
 */
public class EditorSettingsInfo {
    private String editorFontName = null;
    private int editorFontSize = 0;
    private int editorFontStyle = -1;
    
    public String getEditorFontName() {
        if (editorFontName != null) {
            editorFontName = editorFontName.trim();
        }
        return editorFontName;
    }

    public int getEditorFontSize() {
        return editorFontSize;
    }

    public int getEditorFontStyle() {
        return editorFontStyle;
    }
    
    public void setEditorFontName(String value) {
        if ((value != null) && (value.trim().equals(""))) {
            value = "";
        }
        this.editorFontName = value;
    }

    public void setEditorFontSize(int value) {
        this.editorFontSize = value;
    }

    public void setEditorFontStyle(int value) {
        this.editorFontStyle = value;
    }
}
