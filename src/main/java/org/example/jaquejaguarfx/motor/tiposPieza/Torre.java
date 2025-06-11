package org.example.jaquejaguarfx.motor.tiposPieza;

public class Torre extends Pieza {
    public Torre(Color color) {
        super(color == Color.BLANCO ? '♖' : '♜', color);
    }

    @Override
    public boolean movimientoValido(int movimientoHorizontal, int movimientoVertical){
        boolean puede = false;
        if(movimientoVertical == 0 && movimientoHorizontal != 0)
            puede = true;
        if(movimientoHorizontal == 0 && movimientoVertical != 0)
            puede = true;
        return puede;
    }
}
