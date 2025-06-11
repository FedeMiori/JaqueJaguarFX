package org.example.jaquejaguarfx.motor;

import org.example.jaquejaguarfx.motor.tiposPieza.Color;

public class Juego {
    private Jugador[] jugadores;
    private EstadoPartida estadoPartida;
    private Tablero tablero;

    public Juego(){
        tablero = new Tablero();
        jugadores = new Jugador[] {
                new Jugador("Jugador Blancas", Color.BLANCO),
                new Jugador("Jugador Negras", Color.NEGRO)
        };
        estadoPartida = new EstadoPartida(jugadores,tablero);
    }

    public void jugar() {
        tablero.inicializar();
        Jugador jugadorConTurno;
        do{
            jugadorConTurno = estadoPartida.quienTieneTurno();
            System.out.println("TURNO DE: "+jugadorConTurno.toString().toUpperCase());
            tablero.printTablero();
            while(!jugadorConTurno.moverPieza(null,null));
            estadoPartida.siguienteTurno();
        }while(!finPartida());
        System.out.println("FIN PARTIDA");
        tablero.printTablero();
        Jugador jugadorGanador = estadoPartida.quienHaGanado();
        System.out.println("GANADOR : "+jugadorGanador.toString().toUpperCase());
    }

    private boolean finPartida(){ return estadoPartida.finPartida(); }

    public static void main(String[] args) {
        Juego juego=new Juego();
        juego.jugar();
    }
}
