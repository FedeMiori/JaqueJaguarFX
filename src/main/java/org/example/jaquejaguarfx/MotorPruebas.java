package org.example.jaquejaguarfx;

import org.example.jaquejaguarfx.motor.Tablero;

public class MotorPruebas implements Runnable{
    private VentanaAjedrez gestorventana;
    private Tablero tablero;


    public MotorPruebas(VentanaAjedrez gestorventana){
        this.gestorventana = gestorventana;
        this.tablero = new Tablero();
        tablero.inicializar();
    }

    public Tablero getTablero(){
        return tablero;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace(); // o puedes usar: System.err.println("Hilo interrumpido");
            }
            System.out.println(gestorventana.registrarMovimiento());
        }
//        gestorventana.moverPieza(new PosicionGrafica(3,1), new PosicionGrafica(3,3));
        //gestorventana.deletePiece(new PosicionGrafica(1,1));
    }

    public void holaMundo(){
        System.out.println("Hola Mundo");
    }
}
