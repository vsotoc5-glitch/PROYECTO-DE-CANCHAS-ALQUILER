
package com.programador.canchapro.servicio;

import com.programador.canchapro.modelo.Cancha;
import com.programador.canchapro.modelo.EstadoCancha;
import com.programador.canchapro.modelo.EstadoReserva;
import com.programador.canchapro.modelo.Reserva;
import com.programador.canchapro.util.ListaObjetos;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class MotorDisponibilidad {

    // Verificar si dos horarios se cruzan
    public static boolean hayConflicto(
            LocalTime iniNuevo,
            LocalTime finNuevo,
            LocalTime iniExistente,
            LocalTime finExistente) {

        return iniNuevo.isBefore(finExistente)
                && finNuevo.isAfter(iniExistente);
    }

    // Comprobar disponibilidad de una cancha
    public static boolean estaCanchaDisponible(
            Cancha cancha,
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionHoras,
            ListaObjetos reservasTotales) {

        // Validar datos recibidos
        if (cancha == null || fecha == null
                || horaInicio == null
                || reservasTotales == null) {
            return false;
        }

        // La duración debe ser positiva
        if (duracionHoras <= 0 || duracionHoras > 24) {
            return false;
        }

        // Validar estado de cancha
        if (cancha.getEstado() != EstadoCancha.DISPONIBLE) {
            return false;
        }

        // Calcular hora final
        LocalTime horaFin = horaInicio.plusHours(duracionHoras);
        if (!horaFin.isAfter(horaInicio)) {
            return false;
        }

        if (horaInicio.isBefore(cancha.getHoraApertura())
                || horaFin.isAfter(cancha.getHoraCierre())) {
            return false;
        }


        for (int i = 0; i < reservasTotales.tamanio(); i++) {

            Reserva res =
                    (Reserva) reservasTotales.obtener(i);

//verifica feca y cancha 
            if (res.getCancha().getCodigo()
                    .equalsIgnoreCase(cancha.getCodigo())
                    && res.getFecha().equals(fecha)) {
//reserva cancelada
                if (res.getEstado()
                        == EstadoReserva.CANCELADA) {
                    continue;
                }

//horario cruzado
                if (hayConflicto(
                        horaInicio,
                        horaFin,
                        res.getHoraInicio(),
                        res.getHoraFin())) {

                    return false;
                }
            }
        }

        return true;
    }

 //tarifa
    public static double calcularTarifaBase(
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionHoras) {

        if (fecha == null || horaInicio == null
                || duracionHoras <= 0) {
            throw new IllegalArgumentException(
                    "Datos inválidos para calcular la tarifa."
            );
        }

        DayOfWeek dia = fecha.getDayOfWeek();

        double total = 0.0;
        LocalTime horaActual = horaInicio;

        for (int i = 0; i < duracionHoras; i++) {
            if (dia == DayOfWeek.SATURDAY
                    || dia == DayOfWeek.SUNDAY) {

                total += 250.0;

            } else {

                if (horaActual.isBefore(
                        LocalTime.of(17, 0))) {

                    total += 150.0;

                } else {
                    total += 225.0;
                }
            }

            horaActual = horaActual.plusHours(1);
        }

        return total;
    }
}
