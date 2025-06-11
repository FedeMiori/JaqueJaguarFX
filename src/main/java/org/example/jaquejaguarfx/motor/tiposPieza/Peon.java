package org.example.jaquejaguarfx.motor.tiposPieza;

public class Peon extends Pieza {
    public Peon(Color color) {
        super(color == Color.BLANCO ? '♙' : '♟', color);
    }

    @Override
    public boolean movimientoValido(int movimientoHorizontal, int movimientoVertical){
        boolean puede=false;
        if(color == Color.BLANCO){
            if(movimientoHorizontal==0 && movimientoVertical == 1)
                puede =true;
            if(getNumMovimientos()==0 && movimientoHorizontal==0 && movimientoVertical == 2)
                puede=true;
        }
        else if(color == Color.NEGRO){
            if(movimientoHorizontal==0 && movimientoVertical == -1)
                puede =true;
            if(getNumMovimientos()==0 && movimientoHorizontal==0 && movimientoVertical == -2)
                puede=true;
        }
        return puede;
    }

    public boolean peonPuedeComer(int[] vectorMovimiento, Pieza piezaAComer){
        boolean puede = movimientoValido(vectorMovimiento);

        if( piezaAComer != null && this.distintoColor(piezaAComer)
                && movimientoValidoComiendo(vectorMovimiento))
            puede = true;
        return puede;
    }

    public boolean movimientoValidoComiendo(int[] vectorMovimiento){
        boolean puede = false;
        if(Math.abs(vectorMovimiento[0]) == 1){
            if(color == Color.BLANCO && vectorMovimiento[1] == 1)
                puede = true;
            if(color == Color.NEGRO && vectorMovimiento[1] == -1)
                puede=true;
        }
        return puede;
    }
}
