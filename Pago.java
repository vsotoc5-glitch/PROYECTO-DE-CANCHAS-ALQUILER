
package com.programador.canchapro.modelo;

import java.time.LocalDateTime;



public class Pago {
    private String codigo;
    private String codigoReserva;
    private LocalDateTime fechaHora;
    private double monto;
    private String metodoPago;     // Efectivo, Transferencia, Tarjeta
    private String observacion;

    public Pago(String codigo, String codigoReserva, LocalDateTime fechaHora, 
                double monto, String metodoPago, String observacion) {
        this.codigo = codigo;
        this.codigoReserva = codigoReserva;
        this.fechaHora = fechaHora;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.observacion = observacion;
    }

    public String getCodigo() { 
        return codigo; 
    }
    public String getCodigoReserva() {
        return codigoReserva; 
    }
    public LocalDateTime getFechaHora() { 
        return fechaHora; 
    }
    public double getMonto() {
        return monto;
    }
    public String getMetodoPago() {
        return metodoPago; 
    }
    public String getObservacion() { 
        return observacion;
    }

    public String FormatoCSV() {
        return codigo + ";" + codigoReserva + ";" + fechaHora + ";" + monto + ";" 
                + metodoPago + ";" + (observacion != null ? observacion : "");
    }
}


