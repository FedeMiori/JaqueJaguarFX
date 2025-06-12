package org.example.jaquejaguarfx.registroPartidas;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.jaquejaguarfx.Motor;
import org.example.jaquejaguarfx.motor.Jugador;
import org.example.jaquejaguarfx.motor.Tablero;
import org.example.jaquejaguarfx.motor.tiposPieza.Pieza;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

public class RegistrarPartida {
    private static final Conexion conexion = Conexion.getInstance();
    MongoDatabase db = conexion.getDb();
    MongoCollection<Document> coleccion = db.getCollection("Partidas");
    PartidasWrapper partidasWrapper = new PartidasWrapper();

    public void aniadirEstadoTablero(Tablero tablero) {
        List<String> estadoTablero = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza pieza = tablero.getPiezaEnCasilla(i, j);
                if (pieza != null) {
                    estadoTablero.add(pieza.toString());
                } else {
                    estadoTablero.add("");
                }
            }
        }
        partidasWrapper.estados_tablero.add(estadoTablero);
    }

    public void registrarAtributos(Jugador[] jugadores) {
        Map<String, String> jugadoresMap = new HashMap<String, String>();
        jugadoresMap.put("blanco", jugadores[0].toString());
        jugadoresMap.put("negro", jugadores[1].toString());
        partidasWrapper.setJugadores(jugadoresMap);

        LocalDateTime fechaActual = LocalDateTime.now();
        int dia = fechaActual.getDayOfMonth();
        String mes = fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int anio = fechaActual.getYear();
        int hora = fechaActual.getHour();
        int minuto = fechaActual.getMinute();
        partidasWrapper.setFecha_hora(dia+" de "+mes+" de "+anio+", "+hora+":"+minuto);
    }

    public boolean enviarPartida() {
        try {
            Document partidaNueva = new Document();
            partidaNueva.put("jugadores", partidasWrapper.getJugadores());
            partidaNueva.put("fecha_hora", partidasWrapper.getFecha_hora());
            partidaNueva.put("estados_tablero", partidasWrapper.getEstados_tablero());
            coleccion.insertOne(partidaNueva);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
