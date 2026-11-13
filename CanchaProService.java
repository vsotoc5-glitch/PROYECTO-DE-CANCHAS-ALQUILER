
package com.programador.canchapro.servicio;

import com.programador.canchapro.modelo.Cancha;
import com.programador.canchapro.modelo.Cliente;
import com.programador.canchapro.modelo.EstadoCancha;
import com.programador.canchapro.modelo.Pago;
import com.programador.canchapro.modelo.Reserva;
import com.programador.canchapro.modelo.ServicioAdicional;
import com.programador.canchapro.persistencia.RepositorioArchivos;
import com.programador.canchapro.util.ListaObjetos;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CanchaProService {
    private final String RUTA_CLIENTES = "clientes.csv";
    private final String RUTA_CANCHAS = "canchas.csv";
    private final String RUTA_SERVICIOS = "servicios.csv";
    private final String RUTA_RESERVAS = "reservas.csv";
    private final String RUTA_PAGOS = "pagos.csv";

    // Colecciones de objetos en memoria (Arreglos nativos con ListaObjetos)
    private ListaObjetos clientes;
    private ListaObjetos canchas;
    private ListaObjetos servicios;
    private ListaObjetos reservas;

    public CanchaProService() {
        cargarDatosDelSistema();
    }

//carga y guarda
    public void cargarDatosDelSistema() {
        this.clientes = RepositorioArchivos.cargarClientes(RUTA_CLIENTES);
        this.canchas = RepositorioArchivos.cargarCanchas(RUTA_CANCHAS);
        this.servicios = RepositorioArchivos.cargarServicios(RUTA_SERVICIOS);
        
        
        if (this.canchas.tamanio() == 0) {
            inicializarCanchasPorDefecto();
        }

       
        this.reservas = RepositorioArchivos.cargarReservas(RUTA_RESERVAS, clientes, canchas);
       
        RepositorioArchivos.cargarPagos(RUTA_PAGOS, reservas);
    }

    public void guardarTodo() {
        RepositorioArchivos.guardarClientes(RUTA_CLIENTES, clientes);
        RepositorioArchivos.guardarCanchas(RUTA_CANCHAS, canchas);
        RepositorioArchivos.guardarServicios(RUTA_SERVICIOS, servicios);
        RepositorioArchivos.guardarReservas(RUTA_RESERVAS, reservas);
        RepositorioArchivos.guardarPagos(RUTA_PAGOS, reservas);
    }

    private void inicializarCanchasPorDefecto() {
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(23, 0);
        canchas.agregar(new Cancha("C01", "La Bombonera", "Fútbol 5", 10, EstadoCancha.DISPONIBLE, apertura, cierre));
        canchas.agregar(new Cancha("C02", "El Maracaná", "Fútbol 5", 10, EstadoCancha.DISPONIBLE, apertura, cierre));
        canchas.agregar(new Cancha("C03", "El Monumental", "Fútbol 7", 14, EstadoCancha.DISPONIBLE, apertura, cierre));
        RepositorioArchivos.guardarCanchas(RUTA_CANCHAS, canchas);
    }

  //cliente
    public Cliente registrarCliente(String codigo, String nombre, String telefono, String correo) throws Exception {
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (buscarClientePorCodigo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el código: " + codigo);
        }

//duplicados
        if (buscarClientePorTelefono(telefono) != null) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con el teléfono: " + telefono);
        }

        Cliente nuevo = new Cliente(codigo.trim(), nombre.trim(), telefono.trim(), correo, LocalDate.now());
        clientes.agregar(nuevo);
        RepositorioArchivos.guardarClientes(RUTA_CLIENTES, clientes);
        return nuevo;
    }

 
public Cliente buscarClientePorCodigo(String codigo) {

    if (codigo == null || codigo.trim().isEmpty()) {
        return null;
    }

    for (int i = 0; i < clientes.tamanio(); i++) {

        Cliente c = (Cliente) clientes.obtener(i);

        if (c.getCodigo().equalsIgnoreCase(codigo.trim())) {
            return c;
        }
    }

    return null;
}

    public Cliente buscarClientePorTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) return null;
        for (int i = 0; i < clientes.tamanio(); i++) {
            Cliente c = (Cliente) clientes.obtener(i);
            if (c.getTelefono().equalsIgnoreCase(telefono.trim())) {
                return c;
            }
        }
        return null;
    }

    // ==========================================
    // DISPONIBILIDAD Y CONSULTA DE CANCHAS
    // ==========================================
    public ListaObjetos consultarCanchasDisponibles(LocalDate fecha, LocalTime horaInicio, int duracionHoras, String tipoFiltro) {
        ListaObjetos disponibles = new ListaObjetos();

        for (int i = 0; i < canchas.tamanio(); i++) {
            Cancha c = (Cancha) canchas.obtener(i);

            // Filtro por tipo si se especifica (Fútbol 5 o Fútbol 7)
            if (tipoFiltro != null && !tipoFiltro.equalsIgnoreCase("TODAS") && !c.getTipo().equalsIgnoreCase(tipoFiltro)) {
                continue;
            }

            // Validación de traslapes y horarios usando el motor
            if (MotorDisponibilidad.estaCanchaDisponible(c, fecha, horaInicio, duracionHoras, reservas)) {
                disponibles.agregar(c);
            }
        }
        return disponibles;
    }

       
public Reserva crearReserva(String codigoReserva,
        String codigoCliente, String codigoCancha,
        LocalDate fecha, LocalTime horaInicio,
        int duracionHoras, ListaObjetos serviciosSeleccionados,
        double anticipo, String metodoPago) throws Exception {

    // Validar código de reserva
    if (codigoReserva == null
            || codigoReserva.trim().isEmpty()) {

        throw new IllegalArgumentException(
            "El código de la reserva es obligatorio."
        );
    }

    // Validar anticipo
    if (anticipo < 0 || Double.isNaN(anticipo)
            || Double.isInfinite(anticipo)) {

        throw new IllegalArgumentException(
            "El anticipo debe ser un monto válido."
        );
    }

    // Buscar cliente registrado
    Cliente cliente = buscarClientePorCodigo(codigoCliente);

    if (cliente == null) {
        throw new IllegalArgumentException(
            "El cliente no existe."
        );
    }


        Cancha cancha = buscarCanchaPorCodigo(codigoCancha);
        if (cancha == null) {
            throw new IllegalArgumentException("La cancha con código " + codigoCancha + " no existe.");
        }

        if (buscarReservaPorCodigo(codigoReserva) != null) {
            throw new IllegalArgumentException("Ya existe una reserva con el código: " + codigoReserva);
        }

        // Validar disponibilidad antes de crear (Regla de oro del caso de estudio)
        if (!MotorDisponibilidad.estaCanchaDisponible(cancha, fecha, horaInicio, duracionHoras, reservas)) {
            throw new IllegalStateException("La cancha seleccionada ya no está disponible en el horario solicitado.");
        }

        // Cálculo de tarifa según franja horaria y fin de semana
        double costoCancha = MotorDisponibilidad.calcularTarifaBase(fecha, horaInicio, duracionHoras);

        Reserva reserva = new Reserva(codigoReserva, cliente, cancha, fecha, horaInicio, duracionHoras, costoCancha);

        // Agregar servicios adicionales si los hay
        if (serviciosSeleccionados != null) {
            for (int i = 0; i < serviciosSeleccionados.tamanio(); i++) {
                ServicioAdicional s = (ServicioAdicional) serviciosSeleccionados.obtener(i);
                reserva.agregarServicio(s);
            }
        }

        // Registro de anticipo inicial si el cliente pagó algo
        if (anticipo > 0) {
            String codPago = "PAG-" + codigoReserva + "-01";
            Pago p = new Pago(codPago, codigoReserva, LocalDateTime.now(), anticipo, metodoPago, "Anticipo inicial");
            reserva.registrarPago(p);
        }

        reservas.agregar(reserva);
        guardarTodo();
        return reserva;
    }

    public void registrarPagoAReserva(String codigoReserva, double monto, String metodo, String observacion) throws Exception {
        Reserva res = buscarReservaPorCodigo(codigoReserva);
        if (res == null) {
            throw new IllegalArgumentException("Reserva no encontrada: " + codigoReserva);
        }

        int siguienteIndice = res.getPagos().tamanio() + 1;
        String codPago = "PAG-" + codigoReserva + "-" + siguienteIndice;
        Pago p = new Pago(codPago, codigoReserva, LocalDateTime.now(), monto, metodo, observacion);

        res.registrarPago(p); // La reserva valida que no sea mayor al saldo pendiente
        guardarTodo();
    }

    public void cancelarReserva(String codigoReserva, String motivo, String usuario) throws Exception {
        Reserva res = buscarReservaPorCodigo(codigoReserva);
        if (res == null) {
            throw new IllegalArgumentException("Reserva no encontrada: " + codigoReserva);
        }
        res.cancelar(motivo, usuario);
        guardarTodo();
    }

    public Reserva buscarReservaPorCodigo(String codigo) {
        for (int i = 0; i < reservas.tamanio(); i++) {
            Reserva r = (Reserva) reservas.obtener(i);
            if (r.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return r;
            }
        }
        return null;
    }

    public Cancha buscarCanchaPorCodigo(String codigo) {
        for (int i = 0; i < canchas.tamanio(); i++) {
            Cancha c = (Cancha) canchas.obtener(i);
            if (c.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return c;
            }
        }
        return null;
    }


    public ListaObjetos getClientes() { return clientes; }
    public ListaObjetos getCanchas() { return canchas; }
    public ListaObjetos getServicios() { return servicios; }
    public ListaObjetos getReservas() { return reservas; }
}


