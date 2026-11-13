package com.programador.canchapro.modelo;

import java.time.LocalTime;

public class Cancha {
    private String codigo;
    private String nombre;
    private String tipo;          
    private int capacidad;
    private EstadoCancha estado;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    public Cancha(String codigo, String nombre, String tipo, int capacidad, EstadoCancha estado, LocalTime horaApertura, LocalTime horaCierre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estado = estado;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    public String getCodigo() { 
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo; 
    }

    public String getNombre() {
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getTipo() {
        return tipo; 
    }
    public void setTipo(String tipo) {
        this.tipo = tipo; 
    }

    public int getCapacidad() {
        return capacidad; 
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad; 
    }

    public EstadoCancha getEstado() {
        return estado; 
    }
    public void setEstado(EstadoCancha estado) { 
        this.estado = estado; 
    }

    public LocalTime getHoraApertura() { 
        return horaApertura; 
    }
    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura; 
    }

    public LocalTime getHoraCierre() { 
        return horaCierre; 
    }
    public void setHoraCierre(LocalTime horaCierre) { 
        this.horaCierre = horaCierre; 
    }

    public String aFormatoCSV() {
        return codigo + ";" + nombre + ";" + tipo + ";" + capacidad + ";" 
                + estado.name() + ";" + horaApertura + ";" + horaCierre;
    }
}