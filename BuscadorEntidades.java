
package com.programador.canchapro.persistencia;

import com.programador.canchapro.modelo.Cancha;
import com.programador.canchapro.modelo.Cliente;
import com.programador.canchapro.modelo.Reserva;
import com.programador.canchapro.util.ListaObjetos;

public class BuscadorEntidades {


    public static Cliente buscarCliente(ListaObjetos clientes, String codigo) {
        for (int i = 0; i < clientes.tamanio(); i++) {
            Cliente c = (Cliente) clientes.obtener(i);
            
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }

    public static Cancha buscarCancha(ListaObjetos canchas, String codigo) {
        for (int i = 0; i < canchas.tamanio(); i++) {
            Cancha c = (Cancha) canchas.obtener(i);
            
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }

    public static Reserva buscarReserva(ListaObjetos reservas, String codigo) {
        for (int i = 0; i < reservas.tamanio(); i++) {
            Reserva r = (Reserva) reservas.obtener(i);
            
            if (r.getCodigo().equalsIgnoreCase(codigo)) {
                return r;
            }
        }
        return null;
    }
}
    

