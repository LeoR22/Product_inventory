package com.company.proyecto;

import javax.swing.*;

public class Principal {

    public static void main(String[]args ){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Producto PRIMER = new Producto();
                PRIMER.setVisible(true);



            }
        });
    }
}
