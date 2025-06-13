package org.example.jaquejaguarfx.registroPartidas;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;

import java.util.Collection;
import java.util.Collections;


public class Conexion {

    private static Conexion instance;
    private static final String URL_CONEXION = "federicop.duckdns.org";
    private static final int PUERTO = 27017;
    private static final String USUARIO = "usuario";
    private static final String ACCESO = "palomaConÑ";
    private static final String BASE_DATOS = "partidas";
    private static final String AUTH_SOURCE = "admin";

    private MongoClient cliente;
    MongoDatabase db;

    private Conexion() {
        MongoCredential credential = MongoCredential.createCredential(
                USUARIO,
                AUTH_SOURCE,
                ACCESO.toCharArray()
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(URL_CONEXION, PUERTO))))
                .credential(credential).build();

        cliente = MongoClients.create(settings);
        db = cliente.getDatabase(BASE_DATOS);
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
