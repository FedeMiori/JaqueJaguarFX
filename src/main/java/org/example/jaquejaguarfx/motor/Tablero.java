package org.example.jaquejaguarfx.motor;



import org.example.jaquejaguarfx.motor.tiposPieza.*;

import java.util.LinkedList;
import java.util.List;

public class Tablero {
    public final int ALTO_TABLERO = 8;
    public final int ANCHO_TABLERO = 8;

    private Casilla[][] casillas = new Casilla[ALTO_TABLERO][ANCHO_TABLERO];

    public Tablero(){
        for (int i = 0; i < ALTO_TABLERO; i++)
            for (int j = 0; j < ANCHO_TABLERO; j++)
                casillas[i][j] = new Casilla( new Posicion(i,j) );
    }

    public Casilla getCasilla(char letra, int numero){
        String input = String.valueOf(letra) + String.valueOf(numero);
        return getCasilla( new Posicion( input ) );
    }

    public Casilla getCasilla(Posicion posicion){
        Casilla casilla = null;
        if( posicion.dentroLimites(ALTO_TABLERO, ANCHO_TABLERO))
            casilla = casillas[posicion.getPosX()][posicion.getPosY()];
        return casilla;
    }

    public List<Casilla> getCasillasConPieza(){
        List<Casilla> casillasConPieza = new LinkedList<>();
        for (int i = 0; i < ANCHO_TABLERO; i++)
            for (int j = 0; j < ALTO_TABLERO; j++)
                if(!casillaVacia(i,j))
                    casillasConPieza.add(casillas[i][j]);

        return casillasConPieza;
    }

    public boolean casillaVacia(Posicion posicion){
        return getCasilla(posicion).getPieza() == null;
    }

    public boolean casillaVacia(int posX, int posY){
        return casillaVacia(new Posicion(posX,posY));
    }

    public Pieza getPiezaEnCasilla(int posX, int posY){
        return getPiezaEnCasilla(new Posicion(posX,posY));
    }

    public Pieza getPiezaEnCasilla(Posicion posicion){
        if(posicion.dentroLimites(ALTO_TABLERO,ANCHO_TABLERO))
            return getCasilla(posicion).getPieza();
        else
            return null;
    }

    public void inicializar(){
        //Colocamos peones
        for (int i = 0; i < ANCHO_TABLERO; i++) {
            casillas[i][1].setPieza( new Peon(Color.BLANCO) );
            casillas[i][6].setPieza( new Peon(Color.NEGRO) );
        }

        //Colocamos torres:
        getCasilla('a',1).setPieza( new Torre(Color.BLANCO) );
        getCasilla('h',1).setPieza( new Torre(Color.BLANCO) );
        getCasilla('a',8).setPieza( new Torre(Color.NEGRO) );
        getCasilla('h',8).setPieza( new Torre(Color.NEGRO) );

        //Colocamos caballos:
        getCasilla('b',1).setPieza( new Caballo(Color.BLANCO) );
        getCasilla('g',1).setPieza( new Caballo(Color.BLANCO) );
        getCasilla('b',8).setPieza( new Caballo(Color.NEGRO) );
        getCasilla('g',8).setPieza( new Caballo(Color.NEGRO) );

        //Alfiles
        getCasilla('c',1).setPieza( new Alfil(Color.BLANCO) );
        getCasilla('f',1).setPieza( new Alfil(Color.BLANCO) );
        getCasilla('c',8).setPieza( new Alfil(Color.NEGRO) );
        getCasilla('f',8).setPieza( new Alfil(Color.NEGRO) );

        //Reyes
        getCasilla('e',1).setPieza( new Rey(Color.BLANCO) );
        getCasilla('e',8).setPieza( new Rey(Color.NEGRO) );

        //Reinas
        getCasilla('d',1).setPieza( new Reina(Color.BLANCO) );
        getCasilla('d',8).setPieza( new Reina(Color.NEGRO) );
    }

    public boolean moverPieza(Posicion posicionOrigen, Posicion posicionDestino, Color colorJugador){
        Pieza piezaEnOrigen = getPiezaEnCasilla(posicionOrigen);
        if(piezaEnOrigen != null && piezaEnOrigen.esDeColor(colorJugador))
            return moverPieza(posicionOrigen,posicionDestino);
        else
            return false;
    }

    /**
     * Metodo que mueve la pieza situalda en posicionOrigen a la posicionDestino
     * Tiene en cuenta toda la casuistica para ver si se puede realizar ese movimiento
     */
    private boolean moverPieza(Posicion posicionOrigen, Posicion posicionDestino){
        Pieza piezaAMover = getPiezaEnCasilla(posicionOrigen);
        if(movimientoPosible(posicionOrigen,posicionDestino)){
            piezaAMover.incrementarNumMovimientos();
            getCasilla(posicionOrigen).setPieza(null);
            getCasilla(posicionDestino).setPieza(piezaAMover);
            return true;
        }
        else
            return false;
    }

    public boolean movimientoPosible(Posicion posicionOrigen, Posicion posicionDestino){
        boolean puedeMoverse = false;
        Pieza piezaAMover = getPiezaEnCasilla(posicionOrigen);
        Pieza piezaEnDestino = getPiezaEnCasilla(posicionDestino);
        int[] vectorMovimiento = posicionOrigen.getVector( posicionDestino );

        //Comprueba que las posiciones estén dentro del tablero
        if(posicionOrigen.dentroLimites(ALTO_TABLERO, ANCHO_TABLERO) && posicionDestino.dentroLimites(ALTO_TABLERO, ANCHO_TABLERO)){
            //Consulta a la pieza si puede hacer ese movimiento (Aprovechando el polimorfismo)
            if(piezaAMover != null && piezaAMover.movimientoValido(vectorMovimiento) && !(piezaAMover instanceof Peon)) {
                //Comprueba que no haya ninguna ficha entre las 2 posiciones excepto que sea un caballo
                if (caminoDespejado(posicionOrigen, posicionDestino) || piezaAMover instanceof Caballo)
                    //Comprueba que la casilla destino esté vacia o que contenga una ficha adversaria
                    if (piezaEnDestino == null || piezaEnDestino.distintoColor(piezaAMover))
                        puedeMoverse = true;
            }

            //Aquí se tiene en cuenta la situacion particular en la que el peon puede comer moviendose en diagonal
            else if(piezaAMover instanceof Peon && piezaAMover.movimientoValido(vectorMovimiento, piezaEnDestino))
                puedeMoverse = true;
        }

        return puedeMoverse;
    }

    /**
     * Método que indica si las casillas intermedias entre dos posiciones están vacías
     * sirve para ver si una pieza que no sea un caballo tiene el camino despejado para autorizar el movimiento
     * @param origen posición origen del movimiento
     * @param destino posición destino del movimiento
     * @return devuelve true si la trayectoria entre los dos puntos no tiene piezas
     */
    public boolean caminoDespejado(Posicion origen, Posicion destino){
        List<Posicion> lista = getListaPosicionesIntermedias(origen,destino);
        boolean sinObstaculos = true;
        for(Posicion posicion : lista)
            if(!casillaVacia(posicion))
                sinObstaculos = false;
        return sinObstaculos;
    }

    public List<Posicion> getListaPosicionesIntermedias(Posicion origen, Posicion destino){
        int[] vector = origen.getVector(destino);
        int[] vectorUnitario = getVectorUnitario(vector);
        List<Posicion> listaPosiciones = new LinkedList<>();

        Posicion posicionIteradora = new Posicion( origen.getPosX(), origen.getPosY() );

        //Aquí se comprueba que el vector sea diagonal, horizontal o diagonal
        if( vector[0]*vector[1] == 0 || Math.abs(vector[0]) == Math.abs(vector[1]) ){
            //a la posicion iteradora le sumo el vector unitario
            posicionIteradora.sumarVector( vectorUnitario );
            while( ! posicionIteradora.equals(destino)
                    && posicionIteradora.dentroLimites(ANCHO_TABLERO,ALTO_TABLERO)){
                listaPosiciones.add(posicionIteradora.generarCopia());
                posicionIteradora.sumarVector(vectorUnitario);
            }
        }
        return listaPosiciones;
    }

    private static int[] getVectorUnitario(int[] vector){
        int[] vectorUnitario= new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            if(vector[i] < 0)
                vectorUnitario[i] = -1;
            else if(vector[i] > 0)
                vectorUnitario[i] = 1;
            else
                vectorUnitario[i] = 0;
        }
        return vectorUnitario;
    }

    /**
     * OJO! Busca una pieza igual (mismo tipo y color) NO BUSCA LA MISMA INSTANCIA
     */
    public Posicion buscarPieza(Pieza pieza){
        int i=0, j;
        Posicion resultadoBusqueda = null;
        while(i < ANCHO_TABLERO){
            j=0;
            while(j < ALTO_TABLERO && resultadoBusqueda == null){
                Pieza piezaAComprobar = getPiezaEnCasilla(i,j);
                if(pieza.mismoTipoPieza(piezaAComprobar) && pieza.mismoColor(piezaAComprobar))
                    resultadoBusqueda = new Posicion(i,j);
                j++;
            }
            i++;
        }
        return resultadoBusqueda;
    }
}
