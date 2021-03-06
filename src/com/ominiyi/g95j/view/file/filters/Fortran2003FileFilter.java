package com.ominiyi.g95j.view.file.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class Fortran2003FileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String fileName = file.getName();
        if (fileName.toLowerCase().endsWith(".f03")) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Fortran 2003 Files";
    }
}