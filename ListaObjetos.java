
package com.programador.canchapro.util;

public class ListaObjetos {

    private Object[] elementos;
    private int cantidad;

    public ListaObjetos() {
        this.elementos = new Object[10];
        this.cantidad = 0;
    }

    public ListaObjetos(int capacidadInicial) {
        this.elementos = new Object[capacidadInicial];
        this.cantidad = 0;
    }

    public void agregar(Object elemento) {
        if (cantidad == elementos.length) {
            redimensionar();
        }
        elementos[cantidad++] = elemento;
    }

private void redimensionar() {
    int nuevaCapacidad = elementos.length * 2;

    if (nuevaCapacidad == 0) {
        nuevaCapacidad = 10;
    }

    Object[] nuevo = new Object[nuevaCapacidad];

    for (int i = 0; i < cantidad; i++) {
        nuevo[i] = elementos[i];
    }

    elementos = nuevo;
}


    public Object obtener(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        return elementos[indice];
    }

    public int tamanio() {
        return cantidad;
    }

    public void limpiar() {
        this.elementos = new Object[10];
        this.cantidad = 0;
    }
}
    
    

