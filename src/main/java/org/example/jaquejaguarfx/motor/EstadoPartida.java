package org.example.jaquejaguarfx.motor;

import org.example.jaquejaguarfx.motor.tiposPieza.Color;
import org.example.jaquejaguarfx.motor.tiposPieza.Pieza;
import org.example.jaquejaguarfx.motor.tiposPieza.Rey;

import java.util.List;

public class EstadoPartida {
    private Jugador[] jugadores;
    private int tieneTurno;
    private Tablero tablero;
    private Color colorGanador;

    private final int ALTO_TABLERO;
    private final int ANCHO_TABLERO;

    public EstadoPartida(Jugador[] jugadores, Tablero tablero){
        this.jugadores = jugadores;
        tieneTurno = 0;
        this.tablero = tablero;
        ALTO_TABLERO = tablero.ALTO_TABLERO;
        ANCHO_TABLERO = tablero.ANCHO_TABLERO;
    }

    public Jugador quienTieneTurno(){
        return jugadores[tieneTurno];
    }

    public void siguienteTurno(){
        tieneTurno = (tieneTurno + 1) % jugadores.length;
    }

    public Jugador getJugadorConColor(Color color){
        Jugador encontrado = null;
        for (int i = 0; i < jugadores.length; i++) {
            if(jugadores[i].esDeColor(color))
                encontrado = jugadores[i];
        }
        return encontrado;
    }

    public Jugador quienHaGanado(){
        return getJugadorConColor(colorGanador);
    }

    public boolean finPartida(){return comprobarGanador(Color.BLANCO) || comprobarGanador(Color.NEGRO);}

    public boolean comprobarGanador(Color color){
        Posicion posicionRey = tablero.buscarPieza(new Rey(color));
        Posicion posicionAmenazante = buscarJaque(posicionRey);
        if(posicionAmenazante != null) {
            colorGanador = tablero.getPiezaEnCasilla(posicionAmenazante).getColor();
            return perteneceAJugadorConTurno(posicionAmenazante) || jaqueJaguar(posicionAmenazante, posicionRey);
        }
        else
            return false;
    }

    private Posicion buscarJaque(Posicion posicionRey){
        for (int i = 0; i < ANCHO_TABLERO; i++) {
            for (int j = 0; j < ALTO_TABLERO; j++) {
                Posicion potencialJaque = new Posicion(i, j);
                if (tablero.movimientoPosible(potencialJaque, posicionRey))
                    return potencialJaque;
            }
        }
        return null;
    }

    //Comprueba Jaque Mate
    private boolean jaqueJaguar(Posicion posicionAmenazante, Posicion posicionRey){
        boolean jaqueJaguar = true;
        List<Posicion> listaPosiblesBloqueos = tablero.getListaPosicionesIntermedias(posicionAmenazante,posicionRey);
        Color colorDelRey = tablero.getPiezaEnCasilla(posicionRey).getColor();
        Boolean bloqueoEncontrado, comerAlAtacante, huidaDelRey;
        int i=0, j;
        while(i < ANCHO_TABLERO){
            j=0;
            while(j < ALTO_TABLERO && jaqueJaguar){
                Pieza posibleDefensora = tablero.getPiezaEnCasilla(i,j);
                if(posibleDefensora != null && posibleDefensora.esDeColor(colorDelRey)) {
                    comerAlAtacante = tablero.movimientoPosible(new Posicion(i,j),posicionAmenazante);
                    bloqueoEncontrado = puedeDefender(new Posicion(i, j), listaPosiblesBloqueos);
                    huidaDelRey = calcularHuidaRey(posicionRey);
                    jaqueJaguar &= !bloqueoEncontrado && !comerAlAtacante && !huidaDelRey;
                }
                j++;
            }
            i++;
        }
        return jaqueJaguar;
    }

    private boolean puedeDefender(Posicion posicionPosibleDefensora , List<Posicion> posiblesBloqueos){
        Pieza piezaDefensora = tablero.getPiezaEnCasilla(posicionPosibleDefensora);
        if (piezaDefensora instanceof Rey)
            return false;
        for(Posicion bloqueo: posiblesBloqueos)
            if(tablero.movimientoPosible(posicionPosibleDefensora,bloqueo))
                return true;
        return false;
    }

    private boolean perteneceAJugadorConTurno(Posicion posicion){
        return tablero.getPiezaEnCasilla(posicion).getColor() == quienTieneTurno().getColor();
    }

    private boolean calcularHuidaRey(Posicion posicionRey){
        int[] posicion = new int[] {posicionRey.getPosX(), posicionRey.getPosY()};
        Color colorRey = tablero.getPiezaEnCasilla(posicionRey).getColor();
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++) {
                Posicion posicionDestino = new Posicion(posicion[0] + i, posicion[1] + j);
                if(posicionDestino.dentroLimites(ALTO_TABLERO,ANCHO_TABLERO))
                    if (tablero.movimientoPosible(posicionRey, posicionDestino) && !haySubJaque(posicionDestino, colorRey))
                        return true;
            }
        return false;
    }

    private boolean haySubJaque(Posicion posicionSubJaque, Color color){
        for (int i = 0; i < ANCHO_TABLERO; i++)
            for (int j = 0; j < ALTO_TABLERO; j++) {
                Posicion posicionPorComprobar = new Posicion(i, j);
                if (tablero.movimientoPosible(posicionPorComprobar, posicionSubJaque) && tablero.getPiezaEnCasilla(i, j).distintoColor(color))
                    return true;
            }
        return false;
    }
}
