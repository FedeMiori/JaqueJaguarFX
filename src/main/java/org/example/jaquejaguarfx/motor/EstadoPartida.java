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

    public EstadoPartida(Jugador[] jugadores, Tablero tablero){
        this.jugadores = jugadores;
        tieneTurno = 0;
        this.tablero = tablero;
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
            System.out.println("JAQUE.................");
            colorGanador = tablero.getPiezaEnCasilla(posicionAmenazante).getColor();
            return jaqueJaguar(posicionAmenazante, posicionRey) || perteneceAJugadorConTurno(posicionAmenazante);
        }
        else
            return false;
    }

    private Posicion buscarJaque(Posicion posicionRey){
        for (int i = 0; i < tablero.ANCHO_TABLERO; i++) {
            for (int j = 0; j < tablero.ALTO_TABLERO; j++) {
                Posicion potencialJaque = new Posicion(i, j);
                if (tablero.movimientoPosible(potencialJaque, posicionRey))
                    return potencialJaque;
            }
        }
        return null;
    }

    //Comprueba Jaque Mate
    private boolean jaqueJaguar(Posicion posicionAmenazante, Posicion posicionRey){
        List<Posicion> listaPosiblesBloqueos = tablero.getListaPosicionesIntermedias(posicionAmenazante,posicionRey);
        Color colorDelRey = tablero.getPiezaEnCasilla(posicionRey).getColor();
        Boolean bloqueoEncontrado = false, comerAlAtacante = false, huidaDelRey = false;
        int i=0, j;
        while(i < tablero.ANCHO_TABLERO){
            j=0;
            while(j < tablero.ALTO_TABLERO && !bloqueoEncontrado && !comerAlAtacante && !huidaDelRey){
                Pieza posibleDefensora = tablero.getPiezaEnCasilla(i,j);
                if(posibleDefensora != null && posibleDefensora.esDeColor(colorDelRey)) {
                    comerAlAtacante = tablero.movimientoPosible(new Posicion(i,j),posicionAmenazante);
                    bloqueoEncontrado = puedeDefender(new Posicion(i, j), listaPosiblesBloqueos);
                    huidaDelRey = calcularHuidaRey(posicionRey);
                }
                j++;
            }
            i++;
        }
        return !bloqueoEncontrado && !comerAlAtacante && !huidaDelRey;
    }

    private boolean puedeDefender(Posicion posicionPosibleDefensora , List<Posicion> posiblesBloqueos){
        boolean puedeDefender = false;
        for(Posicion bloqueo: posiblesBloqueos)
            if(tablero.movimientoPosible(posicionPosibleDefensora,bloqueo))
                puedeDefender = true;
        return puedeDefender;
    }

    private boolean perteneceAJugadorConTurno(Posicion posicion){
        return tablero.getPiezaEnCasilla(posicion).getColor() == quienTieneTurno().getColor();
    }

    private boolean calcularHuidaRey(Posicion posicionRey){
        int[] posicion = new int[] {posicionRey.getPosX(), posicionRey.getPosY()};
        boolean puedeMoverse = false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Posicion posicionDestino = new Posicion(posicion[0] + i, posicion[1] +j);
                if(tablero.movimientoPosible(posicionRey,posicionDestino))
                    puedeMoverse = true;
            }
        }
        return puedeMoverse;
    }
}
