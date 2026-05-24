package com.mycompany.tokoberkahjaya;

import javax.swing.SwingUtilities;
import ui.LoginForm;

public class TokoBerkahJaya {
    public static void main(String[] args) {
        // Menjalankan antarmuka GUI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}