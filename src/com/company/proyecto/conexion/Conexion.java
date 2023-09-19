package com.company.proyecto.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conexion {
    static String usuario = "root";
    static String contrasenia = "12345";
    static String bd = "inventario_poo_sas";
    static String ip = "localhost";
    static String puerto = "3306";
    static String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;
    static Connection con;

    public static Connection estableceConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(cadena, usuario, contrasenia);
            //JOptionPane.showMessageDialog(null, "La conexión se ha realizado con éxito");
            System.out.println("La conexión se ha realizado con éxito");
            return con;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos, error: " + e.toString());
            System.out.println("Error al conectarse a la base de datos, error: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
