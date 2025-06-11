package org.example.jaquejaguarfx.motor.tiposPieza;
public abstract class Pieza {
    private final char letra;
    protected Color color;

    final String parametroVerde = "\u001B[38;2;55;139;71m";
    final String parametroNegrita = "\033[0;1m";
    final String resetTipografia = "\u001B[0m";

    private int numMovimientos;

    public Pieza(char letra, Color color) {
        this.letra = letra;
        this.color = color;
        numMovimientos=0;
    }

    public Color getColor(){
        return color;
    }

    protected int getNumMovimientos(){
        return numMovimientos;
    }

    //Defino este metodo abstracto para obligar a quee todos los hijos implementen este metodo
    public abstract boolean movimientoValido(int movimientoHorizontal, int movimientoVertical);

    /**
     * Sobre carga del metodo movimiento valido para poder haceptar un vector en un array
     * y hacer así un codigo mas limpio
     */
    public boolean movimientoValido(int[] vectorMovimiento){
        return movimientoValido(vectorMovimiento[0], vectorMovimiento[1]);
    }

    /**
     * Este metod valida el movimiento para un caso en el que el destino contiene una pieza distinta de nulo
     * (es decir donde la casilla no esta vacia)
     * Esto sirve para casos concretos como cuando un peon solo puede moverse en diagonal Si y solo si puede comer una pieza
     * @param vectorMovimiento aui va la vectorizacion de su movimiento
     * @param piezaAComer y aquí va la pieza de la casilla destino para ver si es del color opuesto
     * @return Devuleve true en caso de validar el movimiento
     */
    public boolean movimientoValido(int[] vectorMovimiento, Pieza piezaAComer){
        if(this instanceof Peon)
            //Esta linea ejecuta el metodo de la clase hija peon
            return ((Peon) this).peonPuedeComer(vectorMovimiento, piezaAComer);
        else
            return this.movimientoValido(vectorMovimiento);
    }

    public void incrementarNumMovimientos(){
        numMovimientos++;
    }

    public boolean esDeColor(Color color){
        return this.color == color;
    }

    public boolean mismoColor(Pieza piezaEntrada){
        return piezaEntrada != null && piezaEntrada.getColor() == color;
    }

    public boolean distintoColor(Pieza piezaEntrada){
        return piezaEntrada == null || piezaEntrada.getColor() != color;
    }

    public boolean distintoColor(Color color){
        return color == null || this.color != color;
    }

    public boolean mismoTipoPieza(Pieza pieza){
        if(pieza != null)
            return this.getClass() == pieza.getClass();
        else
            return false;
    }

    public String toString(){
        return String.valueOf(letra);
    }

}
