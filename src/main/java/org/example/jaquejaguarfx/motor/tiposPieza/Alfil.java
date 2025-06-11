package org.example.jaquejaguarfx.motor.tiposPieza;

import static java.lang.Math.abs;

public class Alfil extends Pieza {
    public Alfil(Color color) {
        super(color == Color.BLANCO ? '♗' : '♝', color);
    }


    @Override
    public boolean movimientoValido(int movimientoHorizontal, int movimientoVertical){
        boolean puede=false;
        if(abs(movimientoHorizontal) == abs(movimientoVertical) && movimientoHorizontal != 0)
            puede=true;
        return puede;
    }
}
