package org.example.jaquejaguarfx;

import org.example.jaquejaguarfx.motor.EstadoPartida;
import org.example.jaquejaguarfx.motor.Jugador;
import org.example.jaquejaguarfx.motor.Tablero;
import org.example.jaquejaguarfx.motor.tiposPieza.Color;
import org.example.jaquejaguarfx.registroPartidas.RegistrarPartida;

public class Motor implements Runnable{
    private VentanaAjedrez gestorventana;
    private Jugador[] jugadores;
    private EstadoPartida estadoPartida;
    private Tablero tablero;
    private RegistrarPartida registro;

    public Motor(VentanaAjedrez gestorventana, String[] nombres){
        this.gestorventana = gestorventana;
        tablero = new Tablero();
        tablero.inicializar();
        jugadores = new Jugador[] {
                new Jugador(nombres[0], Color.BLANCO),
                new Jugador(nombres[1], Color.NEGRO)
        };
        estadoPartida = new EstadoPartida(jugadores,tablero);
        registro = new RegistrarPartida();
        registro.registrarAtributos(jugadores);
        registro.aniadirEstadoTablero(tablero);
    }

    public Tablero getTablero(){return tablero;}

    @Override
    public void run() {
        Jugador jugadorConTurno;
        do{
            jugadorConTurno = estadoPartida.quienTieneTurno();
            gestorventana.anunciarTurno(jugadorConTurno.getColorPlural());
            while(!jugadorConTurno.moverPieza(gestorventana, tablero));
            registro.aniadirEstadoTablero(tablero);
            estadoPartida.siguienteTurno();
        }while(!estadoPartida.finPartida());
        gestorventana.mensajeFinPartida(estadoPartida.quienHaGanado().toString());
        registro.enviarPartida();
    }
}
