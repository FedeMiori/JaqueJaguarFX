module org.example.jaquejaguarfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens org.example.jaquejaguarfx to javafx.fxml;
    exports org.example.jaquejaguarfx;
}