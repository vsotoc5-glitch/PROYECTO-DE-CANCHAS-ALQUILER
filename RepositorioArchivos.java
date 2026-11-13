
package com.programador.canchapro.persistencia;

import com.programador.canchapro.modelo.Cancha;
import com.programador.canchapro.modelo.Cliente;
import com.programador.canchapro.modelo.EstadoCancha;
import com.programador.canchapro.modelo.EstadoReserva;
import com.programador.canchapro.modelo.Pago;
import com.programador.canchapro.modelo.Reserva;
import com.programador.canchapro.modelo.ServicioAdicional;
import com.programador.canchapro.util.ListaObjetos;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;


public class RepositorioArchivos {
    


public static ListaObjetos cargarClientes(String ruta) {

    ListaObjetos lista = new ListaObjetos();
    File archivo = new File(ruta);

    if (!archivo.exists()) {
        return lista;
    }

    try (Scanner lector = new Scanner(archivo)) {

        int numeroLinea = 0;

        while (lector.hasNextLine()) {

            numeroLinea++;
            String linea = lector.nextLine().trim();

            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
            }

            try {
                String[] datos = linea.split(";", -1);

                if (datos.length != 5) {
                    throw new IllegalArgumentException(
                        "Cantidad de campos incorrecta"
                    );
                }

                Cliente cliente = new Cliente(
                    datos[0],
                    datos[1],
                    datos[2],
                    datos[3],
                    LocalDate.parse(datos[4])
                );

                lista.agregar(cliente);

            } catch (Exception e) {
                System.err.println(
                    "Error en cliente, línea "
                    + numeroLinea + ": " + e.getMessage()
                );
            }
        }

    } catch (Exception e) {
        System.err.println(
            "Error al cargar clientes: " + e.getMessage()
        );
    }

    return lista;
}

    public static void guardarClientes(String ruta, ListaObjetos clientes) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(ruta, false));
            for (int i = 0; i < clientes.tamanio(); i++) {
                Cliente c = (Cliente) clientes.obtener(i);
                escritor.println(c.aFormatoCSV());
            }
        } catch (Exception e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
        } finally {
            if (escritor != null) escritor.close();
        }
    }

//cancha
    public static ListaObjetos cargarCanchas(String ruta) {
        ListaObjetos lista = new ListaObjetos();
        File archivo = new File(ruta);
        if (!archivo.exists()) return lista;

        Scanner lector = null;
        try {
            lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] p = linea.split(";");
                if (p.length >= 7) {
                    String codigo = p[0];
                    String nombre = p[1];
                    String tipo = p[2];
                    int capacidad = Integer.parseInt(p[3]);
                    EstadoCancha estado = EstadoCancha.valueOf(p[4]);
                    LocalTime apertura = LocalTime.parse(p[5]);
                    LocalTime cierre = LocalTime.parse(p[6]);

                    lista.agregar(new Cancha(codigo, nombre, tipo, capacidad, estado, apertura, cierre));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar canchas: " + e.getMessage());
        } finally {
            if (lector != null) lector.close();
        }
        return lista;
    }

    public static void guardarCanchas(String ruta, ListaObjetos canchas) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(ruta, false));
            for (int i = 0; i < canchas.tamanio(); i++) {
                Cancha c = (Cancha) canchas.obtener(i);
                escritor.println(c.aFormatoCSV());
            }
        } catch (Exception e) {
            System.err.println("Error al guardar canchas: " + e.getMessage());
        } finally {
            if (escritor != null) escritor.close();
        }
    }
//adicionales
    public static ListaObjetos cargarServicios(String ruta) {
        ListaObjetos lista = new ListaObjetos();
        File archivo = new File(ruta);
        if (!archivo.exists()) return lista;

        Scanner lector = null;
        try {
            lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] p = linea.split(";");
                if (p.length >= 3) {
                    lista.agregar(new ServicioAdicional(p[0], p[1], Double.parseDouble(p[2])));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar servicios: " + e.getMessage());
        } finally {
            if (lector != null) lector.close();
        }
        return lista;
    }

    public static void guardarServicios(String ruta, ListaObjetos servicios) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(ruta, false));
            for (int i = 0; i < servicios.tamanio(); i++) {
                ServicioAdicional s = (ServicioAdicional) servicios.obtener(i);
                escritor.println(s.FormatoCSV());
            }
        } catch (Exception e) {
            System.err.println("Error al guardar servicios: " + e.getMessage());
        } finally {
            if (escritor != null) escritor.close();
        }
    }


    public static ListaObjetos cargarReservas(String ruta, ListaObjetos clientes, ListaObjetos canchas) {
        ListaObjetos lista = new ListaObjetos();
        File archivo = new File(ruta);
        if (!archivo.exists()) return lista;

        Scanner lector = null;
        try {
            lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] p = linea.split(";");
                if (p.length >= 9) {
                    String codReserva = p[0];
                    String codCliente = p[1];
                    String codCancha = p[2];
                    LocalDate fecha = LocalDate.parse(p[3]);
                    LocalTime horaInicio = LocalTime.parse(p[4]);
                    int duracion = Integer.parseInt(p[5]);
                    double costoCancha = Double.parseDouble(p[6]);
                    EstadoReserva estado = EstadoReserva.valueOf(p[8]);

                    // Enlace POO: Buscamos las instancias reales
                    Cliente cliente = BuscadorEntidades.buscarCliente(clientes, codCliente);
                    Cancha cancha = BuscadorEntidades.buscarCancha(canchas, codCancha);

                    if (cliente != null && cancha != null) {
                        Reserva res = new Reserva(codReserva, cliente, cancha, fecha, horaInicio, duracion, costoCancha);
                        res.setEstado(estado);

                        // Si la reserva fue cancelada y tiene datos de cancelación
                        if (p.length >= 12 && estado == EstadoReserva.CANCELADA) {
                            if (!p[9].isEmpty()) {
                                res.setDatosCancelacion(LocalDate.parse(p[9]), p[10], p[11]);
                            }
                        }
                        lista.agregar(res);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar reservas: " + e.getMessage());
        } finally {
            if (lector != null) lector.close();
        }
        return lista;
    }

    public static void guardarReservas(String ruta, ListaObjetos reservas) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(ruta, false));
            for (int i = 0; i < reservas.tamanio(); i++) {
                Reserva r = (Reserva) reservas.obtener(i);
                
                String linea = r.getCodigo() + ";"
                        + r.getCliente().getCodigo() + ";"
                        + r.getCancha().getCodigo() + ";"
                        + r.getFecha() + ";"
                        + r.getHoraInicio() + ";"
                        + r.getDuracionHoras() + ";"
                        + r.getCostoCancha() + ";"
                        + r.getTotal() + ";"
                        + r.getEstado().name() + ";"
                        + (r.getFechaCancelacion() 
                        != null ? r.getFechaCancelacion() : "") + ";"
                        + (r.getMotivoCancelacion()
                        != null ? r.getMotivoCancelacion() : "") + ";"
                        + (r.getUsuarioResponsable() 
                        != null ? r.getUsuarioResponsable() : "");

                escritor.println(linea);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar reservas: " + e.getMessage());
        } finally {
            if (escritor != null) escritor.close();
        }
    }

    //reserva
    public static void cargarPagos(String ruta, ListaObjetos reservas) {
        File archivo = new File(ruta);
        if (!archivo.exists()) return;

        Scanner lector = null;
        try {
            lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] p = linea.split(";");
                if (p.length >= 5) {
                    String codPago = p[0];
                    String codReserva = p[1];
                    LocalDateTime fechaHora = LocalDateTime.parse(p[2]);
                    double monto = Double.parseDouble(p[3]);
                    String metodo = p[4];
                    String observacion = p.length > 5 ? p[5] : "";

                    Pago pago = new Pago(codPago, codReserva, fechaHora, monto, metodo, observacion);

              
                    Reserva res = BuscadorEntidades.buscarReserva(reservas, codReserva);
                    if (res != null) {
                        res.getPagos().agregar(pago);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar pagos: " + e.getMessage());
        } finally {
            if (lector != null) lector.close();
        }
    }

    public static void guardarPagos(String ruta, ListaObjetos reservas) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(ruta, false));
            for (int i = 0; i < reservas.tamanio(); i++) {
                Reserva r = (Reserva) reservas.obtener(i);
                ListaObjetos pagosReserva = r.getPagos();
                for (int j = 0; j < pagosReserva.tamanio(); j++) {
                    Pago p = (Pago) pagosReserva.obtener(j);
                    escritor.println(p.FormatoCSV());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al guardar pagos: " + e.getMessage());
        } finally {
            if (escritor != null) escritor.close();
        }
    }
}
    

