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
        boolean exito = false;
        Posicion[] movimiento = ventana.registrarMovimiento();
        System.out.println(movimiento[0].getNotacionAlgebraica() + " -> " + movimiento[1].getNotacionAlgebraica());
        if(tablero.moverPieza(movimiento[0], movimiento[1],this.color)) {
            ventana.moverPieza((PosicionGrafica) movimiento[0], (PosicionGrafica) movimiento[1]);
            exito = true;
        }
//        Posicion origen = Posicion.pedirUsuario("Posicion de pieza a mover: ");
//        Posicion destino = Posicion.pedirUsuario("Posicion destino de la pieza");
//        return tablero.moverPieza(origen,destino,this.color);
        return exito;
    }

    public boolean esDeColor(Color color){
        return this.color == color;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
