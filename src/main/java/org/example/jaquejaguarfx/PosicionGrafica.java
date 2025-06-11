package org.example.jaquejaguarfx;

import org.example.jaquejaguarfx.motor.Posicion;

public class PosicionGrafica extends Posicion {
    public static final int TAMANIO_CASILLA = VentanaAjedrez.DIMENSION_CASILLA;

    public PosicionGrafica(int posX, int posY) {
        super(posX, posY);
    }

    public static PosicionGrafica generarPosicionPorPixeles(int pixelesX, int pixelesY){
        int posicionX = pixelesX / TAMANIO_CASILLA;
        int posicionY = pixelesY / TAMANIO_CASILLA;
        return new PosicionGrafica(posicionX, posicionY);
    }

    public int[] getCoordenadasPixeles(){
        return new int[] {
                getPosX() * TAMANIO_CASILLA, //+ TAMANIO_CASILLA/2,
                getPosY() * TAMANIO_CASILLA //+ TAMANIO_CASILLA/2
        };
    }

    public static void main(String[] args) {
//        PosicionGrafica posicion = new PosicionGrafica(2,1);
//        int[] coordenadas = posicion.getCoordenadasPixeles();
//        System.out.println(coordenadas[0] + "," + coordenadas[1]);

        PosicionGrafica posicionGrafica = generarPosicionPorPixeles(10000,80);
        System.out.println(posicionGrafica.getNotacionAlgebraica());
    }
}
