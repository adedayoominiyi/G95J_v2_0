package com.ominiyi.common.utility;

import java.net.URL;

/**
 *
 * @author Adedayo Ominiyi
 */
public class MyUtility {

    public static URL getResourceFromAbsolutePath(String absolutePath) {
        return MyUtility.class.getResource(absolutePath);
    }

    public static URL getResourceFromRelativePath(Class c, String relativePath) {
        return c.getResource(relativePath);
    }

    public static String invertCase(String oldString) {
        if (oldString == null) {
            return null;
        }

        if (oldString.trim().equals("")) {
            // if old string is empty, return old string
            return oldString;
        }

        //String newString = "";
        int oldStringLength = oldString.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < oldStringLength; i++) {
            char ch = oldString.charAt(i);
            if (Character.isLowerCase(ch)) {
                stringBuilder.append(String.valueOf(ch).toUpperCase());
            } else if (Character.isUpperCase(ch)) {
                stringBuilder.append(String.valueOf(ch).toLowerCase());
            } else {
                stringBuilder.append(String.valueOf(ch));
            }
            /*if ((oldString.charAt(i) >= 65) && (oldString.charAt(i) <= 90)) {
             // uppercase convert to lowercase
             newString = newString + String.valueOf(oldString.charAt(i)).toLowerCase();
             } else if ((oldString.charAt(i) >= 97) && (oldString.charAt(i) <= 122)) {
             // lowercase convert to uppercase
             newString = newString + String.valueOf(oldString.charAt(i)).toUpperCase();
             }*/
        }
        //return newString;
        return stringBuilder.toString();
    }

    public static String toTitleCase(String s) {
        if (s == null) {
            return null;
        }

        if (s.length() <= 1) {
            return s.toUpperCase();
        } else if (s.length() > 1) {
            return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1).toLowerCase();
        }
        return null;
    }
}
