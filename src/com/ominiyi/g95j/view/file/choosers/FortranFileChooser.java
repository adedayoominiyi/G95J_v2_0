package com.ominiyi.g95j.view.file.choosers;

import com.ominiyi.g95j.view.file.filters.Fortran2003FileFilter;
import com.ominiyi.g95j.view.file.filters.Fortran77FileFilter;
import com.ominiyi.g95j.view.file.filters.Fortran90FileFilter;
import com.ominiyi.g95j.view.file.filters.Fortran95FileFilter;
import javax.swing.JFileChooser;

/**
 *
 * @author Adedayo Ominniyi
 */
public class FortranFileChooser extends JFileChooser {
    
    public FortranFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
        
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        Fortran95FileFilter fortran95FileFilter = new Fortran95FileFilter();
        Fortran77FileFilter fortran77FileFilter = new Fortran77FileFilter();
        Fortran90FileFilter fortran90FileFilter = new Fortran90FileFilter();
        Fortran2003FileFilter fortran2003FileFilter = new Fortran2003FileFilter();
        addChoosableFileFilter(fortran77FileFilter);
        addChoosableFileFilter(fortran90FileFilter);
        addChoosableFileFilter(fortran95FileFilter);
        addChoosableFileFilter(fortran2003FileFilter);
        setFileFilter(fortran95FileFilter);
    }
}
