package org.example.jaquejaguarfx.registroPartidas;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Conexion {

    private static Conexion instance;
    private static final String URL_CONEXION = "federicop.duckdns.org";

    private MongoClient cliente;
    MongoDatabase db;

    private Conexion() {
        cliente = MongoClients.create("mongodb://" + URL_CONEXION);
        db = cliente.getDatabase("datos");
    }

    public MongoClient getCliente() {
        return cliente;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public static Conexion getInstance() {
        if (instance==null) {
            instance = new Conexion();
        }
        return instance;
    }
}
