package org.example.jaquejaguarfx.motor;

import org.example.jaquejaguarfx.motor.tiposPieza.Pieza;

public class Casilla {
    private Posicion posicion;
    private Pieza pieza;
    private static final String espacioIdeografico = "\u3000";


    public Casilla(Posicion posicion) {
        this.posicion=posicion;
    }

    public Pieza getPieza() {return pieza;}

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public Posicion getPosicion(){
        return posicion;
    }

    public String toString(){
        if(pieza ==null)
            return espacioIdeografico;
        else
            return pieza.toString();
    }
}
