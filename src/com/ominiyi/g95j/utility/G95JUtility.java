package com.ominiyi.g95j.utility;

import com.ominiyi.g95j.view.model.CompilerSettingsInfo;
import com.ominiyi.g95j.view.model.EditorSettingsInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 *
 * @author Adedayo Ominiyi
 */
public class G95JUtility {

    public static boolean setCompilerSettingsInfo(CompilerSettingsInfo compilerSettingsInfo)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream = null;
        try {
            File folderName = new File("resources");

            if ((!folderName.exists()) || (!folderName.isDirectory())) {
                boolean folderMade = folderName.mkdirs();
                if (folderMade == false) {
                    return false;
                }
            }
            File fileName = new File(folderName, "compilerSettings.properties");
            fileOutputStream = new FileOutputStream(fileName);
            Properties properties = new Properties();
            properties.setProperty("g95_path", compilerSettingsInfo.getPathToG95Compiler());
            properties.setProperty("output_folder_path", compilerSettingsInfo.getPathToCompilationOutputFolder());
            properties.setProperty("overwrite_previous_output", String.valueOf(compilerSettingsInfo.isOverWritePreviousOutput()));
            properties.storeToXML(fileOutputStream, "--Compiler Settings--");
            return true;
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public static boolean setEditorSettingsInfo(EditorSettingsInfo editorSettingsInfo)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream = null;
        try {
            File folderName = new File("resources");

            if ((!folderName.exists()) || (!folderName.isDirectory())) {
                boolean folderMade = folderName.mkdirs();
                if (folderMade == false) {
                    return false;
                }
            }
            File fileName = new File(folderName, "editorSettings.properties");
            fileOutputStream = new FileOutputStream(fileName);
            Properties properties = new Properties();
            properties.setProperty("editor_font_name", editorSettingsInfo.getEditorFontName());
            properties.setProperty("editor_font_size", String.valueOf(editorSettingsInfo.getEditorFontSize()));
            properties.setProperty("editor_font_style", String.valueOf(editorSettingsInfo.getEditorFontStyle()));
            properties.storeToXML(fileOutputStream, "--Editor Settings--");
            return true;
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public static CompilerSettingsInfo getCompilerSettingsInfo() throws InvalidPropertiesFormatException,
            IOException, ClassNotFoundException {
        File fileName = new File("resources", "compilerSettings.properties");

        if (!fileName.exists()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            Properties properties = new Properties();
            properties.loadFromXML(fileInputStream);
            String g95Path = properties.getProperty("g95_path");
            String outputFolderPath = properties.getProperty("output_folder_path");
            String overwritePreviousOutput = properties.getProperty("overwrite_previous_output");
            CompilerSettingsInfo compilerSettingsInfo = new CompilerSettingsInfo();
            compilerSettingsInfo.setPathToG95Compiler(g95Path);
            compilerSettingsInfo.setPathToCompilationOutputFolder(outputFolderPath);
            if ((overwritePreviousOutput != null)
                    && (!overwritePreviousOutput.trim().equals(""))) {
                compilerSettingsInfo.setOverWritePreviousOutput(Boolean.parseBoolean(overwritePreviousOutput.trim()));
            }

            return compilerSettingsInfo;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public static EditorSettingsInfo getEditorSettingsInfo() throws InvalidPropertiesFormatException,
            IOException, ClassNotFoundException {
        File fileName = new File("resources", "editorSettings.properties");

        if (!fileName.exists()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            Properties properties = new Properties();
            properties.loadFromXML(fileInputStream);
            String editorFontName = properties.getProperty("editor_font_name");
            String editorFontSize = properties.getProperty("editor_font_size");
            String editorFontStyle = properties.getProperty("editor_font_style");
            EditorSettingsInfo editorSettingsInfo = new EditorSettingsInfo();
            editorSettingsInfo.setEditorFontName(editorFontName);
            if ((editorFontSize != null) && (!editorFontSize.trim().equals(""))) {
                try {
                    editorSettingsInfo.setEditorFontSize(Integer.parseInt(editorFontSize.trim()));
                } catch (NumberFormatException ex) {
                    // do nothing
                }
            }
            if ((editorFontStyle != null) && (!editorFontStyle.trim().equals(""))) {
                try {
                    editorSettingsInfo.setEditorFontStyle(Integer.parseInt(editorFontStyle.trim()));
                } catch (NumberFormatException ex) {
                    // do nothing
                }
            }

            return editorSettingsInfo;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }
}
