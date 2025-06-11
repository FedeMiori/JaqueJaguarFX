package org.example.jaquejaguarfx.motor;

import java.util.Scanner;

public class Posicion {
    private int posX;
    private int posY;

    public Posicion(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * lee la posicion en notacion algebraica y lo guarda como coordenadas
     * ej: de "A2" lo convierte a (0,1)
     * @param notacionAlgebraica debe tener el formato letra numero ej:a1
     */
    public Posicion(String notacionAlgebraica){
        char letraColumna = notacionAlgebraica.toLowerCase().charAt(0);
        posX = (int) letraColumna - 'a';
        posY = (int) notacionAlgebraica.charAt(1) - 49;
    }

    public Posicion(char letra, int numero){
        this.posX = (int) letra - 'a';
        this.posY = numero - 1;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    //Convierte a notacion algebraica
    // Ej: dado posX=1 y posY=1 devuelve "B2"
    public String getNotacionAlgebraica(){
        char letra = (char) ('a' + posX);
        int numero = posY + 1;
        return ""+letra + numero;
    }

    /**
     * Suma a la posicion el vector pasado como parametro
     * Ej: si a la posicion (1,1) se le suma el vector (0,2) pasa a ser posicion (1,3)
     */
    public void sumarVector(int[] vectorASumar){
        posX += vectorASumar[0];
        posY += vectorASumar[1];
    }

    /**
     * @param limiteVertical
     * @param limiteHorizontal
     * @return devuele true si la posicion esta dentro de los limites
     */
    public boolean dentroLimites(int limiteVertical, int limiteHorizontal){
        return posX >= 0 && posX < limiteVertical && posY >= 0 && posY < limiteHorizontal;
    }

    /**
     * metodo para calcular el vector director entre 2 puntos
     * Ej: si la posicion orijen es (2,4) y la posicion parametro es (2,6)
     * devuelve el vector (0,2) (la diferencia entre las dos posiciones)
     * @param posicionDestino
     * @return devuelve el vector que une la posicion this (esta misma) con la posicionDestino pasada como parametro
     */
    public int[] getVector(Posicion posicionDestino){
        int componenteI = posicionDestino.getPosX() - posX;
        int componenteJ = posicionDestino.getPosY() - posY;
        return new int[] {componenteI, componenteJ};
    }

    /**
     * Funcion estatica que pide una posicion al usuario
     * @param mensaje mensaje a mostrar por pantalla
     * @return devuelve la posicion dada por el usuario
     */
    public static Posicion pedirUsuario(String mensaje){
        if(mensaje != null)
            System.out.println(mensaje);
        Scanner teclado = new Scanner(System.in);
        String entrada=teclado.nextLine();
        if(formatoCorreccto(entrada))
            return new Posicion(entrada);
        else
            return pedirUsuario("ERROR: formato Invalido");
    }

    //Comprueba si el string pasado como parametro tiene el formato [letra][numero] Ej: "b5"
    public static boolean formatoCorreccto(String cadena){
        return cadena.matches("^[a-z][0-9]");
    }

    /**
     * @return devuelve true si 2 posiciones son iguales
     */
    public boolean equals(Posicion posicionAComprobar) {
        return posicionAComprobar.getPosX() == posX && posicionAComprobar.getPosY() == posY;
    }

    public Posicion generarCopia(){
        return new Posicion(getPosX(),getPosY());
    }
}
