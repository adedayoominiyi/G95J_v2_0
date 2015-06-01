package com.ominiyi.g95j.view.model;

public class SearchOptionInfo {

    private String textToFind = null;
    private String textToUseForReplacment = null;
    private boolean matchCase = false;
    private boolean matchEntireWord = false;
    private boolean moveBackwards = false;

    public boolean isMatchCase() {
        return this.matchCase;
    }

    public void setMatchCase(boolean matchCase) {
        this.matchCase = matchCase;
    }

    public boolean isMatchEntireWord() {
        return this.matchEntireWord;
    }

    public void setMatchEntireWord(boolean matchEntireWord) {
        this.matchEntireWord = matchEntireWord;
    }

    public boolean isMoveBackwards() {
        return this.moveBackwards;
    }

    public void setMoveBackwards(boolean moveBackwards) {
        this.moveBackwards = moveBackwards;
    }

    public String getTextToFind() {
        return this.textToFind;
    }

    public void setTextToFind(String textToFind) {
        this.textToFind = textToFind;
    }

    public String getTextToUseForReplacment() {
        return this.textToUseForReplacment;
    }

    public void setTextToUseForReplacment(String textToUseForReplacment) {
        this.textToUseForReplacment = textToUseForReplacment;
    }
}