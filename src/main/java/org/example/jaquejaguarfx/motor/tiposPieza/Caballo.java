package org.example.jaquejaguarfx.motor.tiposPieza;

import static java.lang.Math.abs;

public class Caballo extends Pieza {
    public Caballo(Color color) {
        super(color == Color.BLANCO ? '♘' : '♞', color);
    }

    @Override
    public boolean movimientoValido(int movimientoHorizontal, int movimientoVertical){
        boolean puede=false;
        if(abs(movimientoVertical) == 2 && abs(movimientoHorizontal) == 1)
            puede = true;
        if(abs(movimientoHorizontal) == 2 && abs(movimientoVertical) == 1)
            puede=true;
        return puede;
    }
}
