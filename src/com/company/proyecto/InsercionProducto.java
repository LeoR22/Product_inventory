package com.company.proyecto;

import com.company.proyecto.conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsercionProducto {

    public static void insertarProducto(String nombre, double precio, int cantidad) {
        Conexion conexion = new Conexion();
        Connection con = conexion.estableceConexion();

        if (con != null) {
            try {
                // Sentencia SQL para la inserción
                String consulta = "INSERT INTO productos (nombre_pro, precio, cantidad) VALUES (?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(consulta);
                statement.setString(1, nombre);
                statement.setDouble(2, precio);
                statement.setInt(3, cantidad);

                // Ejecutar la inserción
                int filasInsertadas = statement.executeUpdate();

                if (filasInsertadas > 0) {
                    System.out.println("Producto insertado con éxito.");
                } else {
                    System.out.println("Error al insertar el producto.");
                }

                // Cerrar la conexión
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se pudo establecer la conexión.");
        }
    }
}