package org.example.jaquejaguarfx;

import org.example.jaquejaguarfx.motor.EstadoPartida;
import org.example.jaquejaguarfx.motor.Jugador;
import org.example.jaquejaguarfx.motor.Tablero;
import org.example.jaquejaguarfx.motor.tiposPieza.Color;
import org.example.jaquejaguarfx.registroPartidas.PartidasWrapper;
import org.example.jaquejaguarfx.registroPartidas.RegistrarPartida;

import java.util.Map;

public class Motor implements Runnable{
    private VentanaAjedrez gestorventana;
    private Jugador[] jugadores;
    private EstadoPartida estadoPartida;
    private Tablero tablero;
    private RegistrarPartida registrarPartida;

    public Motor(VentanaAjedrez gestorventana){
        this.gestorventana = gestorventana;
        this.tablero = new Tablero();
        tablero.inicializar();
        jugadores = new Jugador[] {
                new Jugador("Jugador Blancas", Color.BLANCO),
                new Jugador("Jugador Negras", Color.NEGRO)
        };
        estadoPartida = new EstadoPartida(jugadores,tablero);
        registrarPartida = new RegistrarPartida();
        registrarPartida.registrarAtributos(jugadores);
        registrarPartida.aniadirEstadoTablero(tablero);
    }

    public Tablero getTablero(){
        return tablero;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    @Override
    public void run() {
        Jugador jugadorConTurno;

        do{
            jugadorConTurno = estadoPartida.quienTieneTurno();
            gestorventana.anunciarTurno(jugadorConTurno);
            while(!jugadorConTurno.moverPieza(gestorventana, tablero));
            registrarPartida.aniadirEstadoTablero(tablero);
            estadoPartida.siguienteTurno();
        }while(!estadoPartida.finPartida());
        registrarPartida.enviarPartida();
        gestorventana.mensajeFinPartida(estadoPartida.quienHaGanado().toString());
    }
}
