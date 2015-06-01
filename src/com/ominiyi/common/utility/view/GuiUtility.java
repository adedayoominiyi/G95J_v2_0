package com.ominiyi.common.utility.view;

import java.awt.GraphicsEnvironment;

/**
 *
 * @author Adedayo Ominiyi
 */
public class GuiUtility {

    public static String[] getAvailableFonts() {
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] envfonts = gEnv.getAvailableFontFamilyNames();
        return envfonts;
    }

    public static String newLine() {
        return System.getProperty("line.separator");
    }
}
