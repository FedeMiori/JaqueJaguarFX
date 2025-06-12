package org.example.jaquejaguarfx.motor;

import org.example.jaquejaguarfx.PosicionGrafica;
import org.example.jaquejaguarfx.VentanaAjedrez;
import org.example.jaquejaguarfx.motor.tiposPieza.*;

public class Jugador {
    private String nombre;
    private Color color;

    public Jugador(String nombre, Color color){
        this.nombre = nombre;
        this.color = color;
    }

    public Color getColor() {return color;}

    public boolean moverPieza(VentanaAjedrez ventana, Tablero tablero){
        Posicion[] movimiento = ventana.registrarMovimiento();
        PosicionGrafica origen = (PosicionGrafica) movimiento[0], destino = (PosicionGrafica) movimiento[1];
        if(tablero.moverPieza(origen, destino,this.color)) {
            ventana.moverPieza(origen,destino);
            return true;
        }
        else
            ventana.efectoShake(origen);
        return false;
    }

    public boolean esDeColor(Color color){
        return this.color == color;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
