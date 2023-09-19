package com.company.proyecto;

import com.company.proyecto.conexion.Conexion;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Vector;


public class Producto extends  JFrame {

    private JButton btnGuardar;
    private JPanel VENTANA;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTable table1;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTextField textField4;

    public Producto(){

        add(VENTANA);
        setSize(700,500);
        setLocationRelativeTo(null);
        setTitle("Producto");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener datos de los campos de texto
                String nombre = textField1.getText();
                double precio = Double.parseDouble(textField2.getText());
                int cantidad = Integer.parseInt(textField3.getText());

                // Llamar al método para insertar el producto
                InsercionProducto.insertarProducto(nombre, precio, cantidad);

                // Limpiar los campos de texto después de la inserción
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");
                //Cargar tabla
                cargarDatosEnTabla();
            }
        });

        cargarDatosEnTabla();
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String valorBuscado = textField4.getText(); // Obtener el valor ingresado por el usuario

                    // Realizar la consulta SQL para buscar por el ID
                    String consulta = "SELECT nombre_pro, precio, cantidad FROM productos WHERE idproductos = ?";
                    Connection con = Conexion.estableceConexion();
                    PreparedStatement pst = con.prepareStatement(consulta);

                    // Utilizar el valor ingresado por el usuario como ID de búsqueda
                    pst.setString(1, valorBuscado);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        // Se encontró el registro, mostrarlo en los campos de texto
                        String nombre = rs.getString("nombre_pro");
                        double precio = rs.getDouble("precio");
                        int cantidad = rs.getInt("cantidad");

                        textField1.setText(nombre);
                        textField2.setText(String.valueOf(precio));
                        textField3.setText(String.valueOf(cantidad));
                    } else {
                        // No se encontraron resultados, puedes mostrar un mensaje
                        textField1.setText("");
                        textField2.setText("");
                        textField3.setText("");
                        JOptionPane.showMessageDialog(null, "No se encontró ningún registro con el ID proporcionado.");
                    }

                    // Cierra la conexión y el ResultSet
                    rs.close();
                    pst.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = textField1.getText();
                    double precio = Double.parseDouble(textField2.getText());
                    int cantidad = Integer.parseInt(textField3.getText());

                    // Determina cuáles campos se van a actualizar en la base de datos
                    String consulta = "UPDATE productos SET ";
                    boolean seActualizoAlgo = false;

                    if (!nombre.isEmpty()) {
                        consulta += "nombre_pro = ?, ";
                        seActualizoAlgo = true;
                    }
                    if (precio > 0) {
                        consulta += "precio = ?, ";
                        seActualizoAlgo = true;
                    }
                    if (cantidad > 0) {
                        consulta += "cantidad = ?, ";
                        seActualizoAlgo = true;
                    }

                    // Quita la última coma y espacio de la consulta
                    consulta = consulta.substring(0, consulta.length() - 2);

                    // Agrega la condición para la actualización basada en el nombre del producto
                    consulta += " WHERE nombre_pro = ?";

                    if (seActualizoAlgo) {
                        Connection con = Conexion.estableceConexion();
                        PreparedStatement pst = con.prepareStatement(consulta);

                        int contadorParametros = 1;

                        if (!nombre.isEmpty()) {
                            pst.setString(contadorParametros, nombre);
                            contadorParametros++;
                        }
                        if (precio > 0) {
                            pst.setDouble(contadorParametros, precio);
                            contadorParametros++;
                        }
                        if (cantidad > 0) {
                            pst.setInt(contadorParametros, cantidad);
                            contadorParametros++;
                        }

                        // El último parámetro es siempre el nombre
                        pst.setString(contadorParametros, nombre);

                        int filasActualizadas = pst.executeUpdate();

                        if (filasActualizadas > 0) {
                            // La actualización fue exitosa
                            JOptionPane.showMessageDialog(null, "Registro actualizado con éxito.");
                            cargarDatosEnTabla(); // Actualizar la tabla con los datos actualizados
                            textField1.setText("");
                            textField2.setText("");
                            textField3.setText("");
                            textField1.requestFocus();
                        } else {
                            // No se actualizó ningún registro, es posible que el nombre no exista
                            JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro. Verifica el nombre del producto.");
                        }

                        // Cierra la conexión
                        pst.close();
                        con.close();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se especificaron campos para actualizar.");
                    }
                } catch (SQLException | NumberFormatException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al actualizar el registro. Verifica los datos ingresados.");
                }
            }
        });


        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = textField1.getText(); // Supongamos que quieres eliminar por nombre

                    // Realiza la eliminación en la base de datos basada en el nombre del producto
                    String consulta = "DELETE FROM productos WHERE nombre_pro = ?";
                    Connection con = Conexion.estableceConexion();
                    PreparedStatement pst = con.prepareStatement(consulta);

                    pst.setString(1, nombre);

                    int filasEliminadas = pst.executeUpdate();

                    if (filasEliminadas > 0) {
                        // La eliminación fue exitosa
                        JOptionPane.showMessageDialog(null, "Registro eliminado con éxito.");
                        cargarDatosEnTabla(); // Actualizar la tabla con los datos actualizados
                        textField1.setText("");
                        textField2.setText("");
                        textField3.setText("");
                        textField1.requestFocus();
                    } else {
                        // No se eliminó ningún registro, es posible que el nombre no exista
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el registro. Verifica el nombre del producto.");
                    }

                    // Cierra la conexión
                    pst.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al eliminar el registro. Verifica los datos ingresados.");
                }
            }
        });


    }

    // Método para cargar datos desde un ResultSet en la tabla
    private void cargarDatosEnTabla() {
        try {
            // Realiza la consulta SQL para obtener todos los registros de la tabla "products"
            String consulta = "SELECT * FROM productos";
            Connection con = Conexion.estableceConexion(); // Utiliza tu clase o esta misma de conexión
            PreparedStatement pst = con.prepareStatement(consulta);
            ResultSet rs = pst.executeQuery();

            // Utiliza el método buildTableModel para crear el modelo de tabla
            DefaultTableModel model = buildTableModel(rs);

            // Establece el modelo en la tabla1
            table1.setModel(model);

            // Cierra la conexión y el ResultSet
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) {
        try {
            // Obtiene los metadatos del ResultSet
            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            // Obtiene el número de columnas en el ResultSet
            int columnCount = metaData.getColumnCount();
            // Crea un vector para los nombres de las columnas
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }
            // Crea un vector de vectores para los datos de la tabla
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row.add(rs.getObject(columnIndex));
                }
                data.add(row);
            }
            // Crea y devuelve el modelo de tabla
            return new DefaultTableModel(data, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
