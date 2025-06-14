package org.example.jaquejaguarfx.motor;

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

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
     * @return devuelve true si 2 posiciones son iguales
     */
    public boolean equals(Posicion posicionAComprobar) {
        return posicionAComprobar.getPosX() == posX && posicionAComprobar.getPosY() == posY;
    }

    public Posicion generarCopia(){
        return new Posicion(getPosX(),getPosY());
    }
}
