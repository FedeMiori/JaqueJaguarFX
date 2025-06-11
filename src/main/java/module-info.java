module org.example.jaquejaguarfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.jaquejaguarfx to javafx.fxml;
    exports org.example.jaquejaguarfx;
}