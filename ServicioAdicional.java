
package com.programador.canchapro.modelo;

public class ServicioAdicional {
    private String codigo;
    private String nombre;
    private double precio;

    public ServicioAdicional(String codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    public String FormatoCSV() {
        return codigo + ";" + nombre + ";" + precio;
    }

    public boolean aFormatoCSV() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
