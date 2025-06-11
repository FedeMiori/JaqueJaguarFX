package org.example.jaquejaguarfx.motor.tiposPieza;

public class Rey extends Pieza {
    public Rey(Color color) {
        super(color == Color.BLANCO ? '♔' : '♚', color);
    }

    @Override
    public boolean movimientoValido(int movimientoHorizontal, int movimientoVertical){
        boolean puede=false;
        if(movimientoVertical != 0 || movimientoHorizontal != 0)
            if(Math.abs(movimientoVertical) <= 1 && Math.abs(movimientoHorizontal) <= 1)
                puede=true;
        return puede;
    }
}
