package com.ominiyi.g95j.main;

import com.ominiyi.g95j.view.MainWindow;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Adedayo Ominiyi
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                } catch (ClassNotFoundException ex) {
                    // do nothing
                } catch (InstantiationException ex) {
                    // do nothing
                } catch (IllegalAccessException ex) {
                    // do nothing
                } catch (UnsupportedLookAndFeelException ex) {
                    // do nothing
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainWindow frame = MainWindow.instanceOf();
                frame.setVisible(true);
            }
        });
    }
}
