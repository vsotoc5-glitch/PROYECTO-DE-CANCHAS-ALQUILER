
package com.programador.canchapro.modelo;

import com.programador.canchapro.util.ListaObjetos;
import java.time.LocalDate;
import java.time.LocalTime;


public class Reserva {
   
    private String codigo;
    private Cliente cliente;
    private Cancha cancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private int duracionHoras;
    private LocalTime horaFin;
    private double costoCancha;
    private double costoServicios;
    private double total;
    private EstadoReserva estado; 
    private ListaObjetos servicios; 
    private ListaObjetos pagos;
    private LocalDate fechaCancelacion;
    private String motivoCancelacion;
    private String usuarioResponsable;

    public Reserva(String codigo, Cliente cliente, Cancha cancha, LocalDate fecha, 
                   LocalTime horaInicio, int duracionHoras, double costoCancha) {
         if (duracionHoras <= 0) {
             throw new IllegalArgumentException("La duración debe ser mayor a cero.");
         }
        this.codigo = codigo;
        this.cliente = cliente;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionHoras = duracionHoras;
        this.horaFin = horaInicio.plusHours(duracionHoras);
        this.costoCancha = costoCancha;
        this.costoServicios = 0.0;
        this.total = costoCancha;
        this.estado = EstadoReserva.PENDIENTE;
        this.servicios = new ListaObjetos();
        this.pagos = new ListaObjetos();
    }

  
    public void agregarServicio(ServicioAdicional servicio) {
    if (servicio == null) {
        throw new IllegalArgumentException(
            "Debe seleccionar un servicio."
        );
    }
    if (estado == EstadoReserva.CANCELADA
            || estado == EstadoReserva.FINALIZADA) {
        throw new IllegalStateException(
            "No se pueden agregar servicios a esta reserva."
        );
    }
    servicios.agregar(servicio);
    costoServicios += servicio.getPrecio();
    total = costoCancha + costoServicios;
}



public void registrarPago(Pago pago) throws Exception {

    if (estado == EstadoReserva.CANCELADA) {
        throw new IllegalStateException(
            "No se pueden registrar pagos en una reserva cancelada."
        );
    }

    if (estado == EstadoReserva.FINALIZADA) {
        throw new IllegalStateException(
            "No se pueden registrar pagos en una reserva finalizada."
        );
    }

    if (pago == null) {
        throw new IllegalArgumentException(
            "El pago no puede estar vacío."
        );
    }

    if (pago.getMonto() <= 0) {
        throw new IllegalArgumentException(
            "El monto debe ser mayor a cero."
        );
    }

    double saldoActual = getSaldoPendiente();

    if (pago.getMonto() > saldoActual + 0.001) {
        throw new IllegalArgumentException(
            "El pago supera el saldo pendiente de Q"
            + saldoActual
        );
    }

    pagos.agregar(pago);

    if (getSaldoPendiente() <= 0.001
            && estado == EstadoReserva.PENDIENTE) {
        estado = EstadoReserva.CONFIRMADA;
    }
}

    public double getTotalPagado() {
        double acumulado = 0.0;
        for (int i = 0; i < pagos.tamanio(); i++) {
            Pago p = (Pago) pagos.obtener(i);
            acumulado += p.getMonto();
        }
        return acumulado;
    }

    public double getSaldoPendiente() {
        return total - getTotalPagado();
    }

    public void cancelar(String motivo, String usuario) throws Exception {
        if (estado == EstadoReserva.FINALIZADA) {
            throw new IllegalStateException("Una reserva finalizada no puede cancelarse.");
        }
        if (estado == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva ya está cancelada.");
        }
        this.estado = EstadoReserva.CANCELADA;
        this.motivoCancelacion = motivo;
        this.usuarioResponsable = usuario;
        this.fechaCancelacion = LocalDate.now();
    }

    // Configuración para restaurar cancelaciones desde el archivo CSV
    public void setDatosCancelacion(LocalDate fecha, String motivo, String usuario) {
        this.fechaCancelacion = fecha;
        this.motivoCancelacion = motivo;
        this.usuarioResponsable = usuario;
    }

    // Getters y Setters
    public String getCodigo() { 
        return codigo; 
    }
    
    public Cliente getCliente() { 
        return cliente; 
    }
    
    public Cancha getCancha() { 
        return cancha; 
    }
    
    public LocalDate getFecha() { 
        return fecha; 
    }
    
    public LocalTime getHoraInicio() { 
        return horaInicio; 
    }
    
    public LocalTime getHoraFin() { 
        return horaFin; 
    }
    
    public int getDuracionHoras() { 
        return duracionHoras; 
    }
    
    public double getCostoCancha() {
        return costoCancha;
    }

    public double getCostoServicios() {
        return costoServicios;
    }
    
    public double getTotal() { 
        return total; 
    }
    
    public EstadoReserva getEstado() { 
        return estado; 
    }
    
    public void setEstado(EstadoReserva estado) { 
        this.estado = estado; 
    }
    
    public ListaObjetos getPagos() { 
        return pagos; 
    }
    
    public ListaObjetos getServicios() { 
        return servicios; 
    }

    public LocalDate getFechaCancelacion() {
        return fechaCancelacion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }
}