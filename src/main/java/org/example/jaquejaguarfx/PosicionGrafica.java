package org.example.jaquejaguarfx;

import org.example.jaquejaguarfx.motor.Posicion;

public class PosicionGrafica extends Posicion {
    public static final int TAMANIO_CASILLA = VentanaAjedrez.DIMENSION_CASILLA;

    public PosicionGrafica(int posX, int posY) {
        super(posX, posY);
    }

    public int[] getCoordenadasPixeles(){
        return new int[] {
                getPosX() * TAMANIO_CASILLA,
                getPosY() * TAMANIO_CASILLA
        };
    }
}
