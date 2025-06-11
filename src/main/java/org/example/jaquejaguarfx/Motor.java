package org.example.jaquejaguarfx;

import org.example.jaquejaguarfx.motor.EstadoPartida;
import org.example.jaquejaguarfx.motor.Jugador;
import org.example.jaquejaguarfx.motor.Tablero;
import org.example.jaquejaguarfx.motor.tiposPieza.Color;

import java.sql.SQLOutput;

public class Motor implements Runnable{
    private VentanaAjedrez gestorventana;
    private Jugador[] jugadores;
    private EstadoPartida estadoPartida;
    private Tablero tablero;


    public Motor(VentanaAjedrez gestorventana){
        this.gestorventana = gestorventana;
        this.tablero = new Tablero();
        tablero.inicializarDEBUG();
        jugadores = new Jugador[] {
                new Jugador("Jugador Blancas", Color.BLANCO),
                new Jugador("Jugador Negras", Color.NEGRO)
        };
        estadoPartida = new EstadoPartida(jugadores,tablero);
    }

    public Tablero getTablero(){
        return tablero;
    }

    @Override
    public void run() {
        Jugador jugadorConTurno;
        do{
            System.out.println("TURNO DE: "+estadoPartida.quienTieneTurno());
            jugadorConTurno = estadoPartida.quienTieneTurno();
            gestorventana.anunciarTurno(jugadorConTurno);
            while(!jugadorConTurno.moverPieza(gestorventana, tablero));
            estadoPartida.siguienteTurno();
        }while(!estadoPartida.finPartida());
        //gestorventana.mensajeFinPartida(estadoPartida.quienHaGanado().toString());
    }

    public void holaMundo(){
        System.out.println("Hola Mundo");
    }
}
