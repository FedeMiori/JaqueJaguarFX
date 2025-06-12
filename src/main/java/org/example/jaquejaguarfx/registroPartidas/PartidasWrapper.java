package org.example.jaquejaguarfx.registroPartidas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartidasWrapper {
    Map<String, String> jugadores;
    String fecha_hora;
    List<List<String>> estados_tablero = new ArrayList<>();

    public Map<String, String> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Map<String, String> jugadores) {
        this.jugadores = jugadores;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public List<List<String>> getEstados_tablero() {
        return estados_tablero;
    }

    public void setEstados_tablero(List<List<String>> estados_tablero) {
        this.estados_tablero = estados_tablero;
    }
}
