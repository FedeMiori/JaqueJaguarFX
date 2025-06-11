package org.example.jaquejaguarfx.motor.tiposPieza;

import static java.lang.Math.abs;

public class Reina extends Pieza {
    public Reina(Color color) {
        super(color == Color.BLANCO ? '♕' : '♛', color);
    }

    @Override
    public boolean movimientoValido(int movimientoHorizontal, int movimientoVertical){
        boolean puede = false;
        //Movimiento horizontal
        if(movimientoVertical == 0 && movimientoHorizontal != 0)
            puede = true;
        //vertical
        if(movimientoHorizontal == 0 && movimientoVertical != 0)
            puede = true;
        //Diagonales
        if(abs(movimientoHorizontal) == abs(movimientoVertical) && movimientoHorizontal != 0)
            puede=true;
        return puede;
    }
}
