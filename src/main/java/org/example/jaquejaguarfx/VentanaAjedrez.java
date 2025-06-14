package org.example.jaquejaguarfx;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.jaquejaguarfx.motor.Posicion;
import org.example.jaquejaguarfx.motor.Tablero;

public class VentanaAjedrez extends Application {

    public static final int DIMENSION_CASILLA = 80;
    private static final int ANCHO = 8;
    private static final int ALTO = 8;
    private static final String TITULO_VENTANA = "JAQUE JAGUAR";
    private Motor motorJuego;
    private Stage primaryStage;

    private final Casilla[][] TableroGrafico = new Casilla[ANCHO][ALTO];
    private final Pane pieceLayer = new Pane();

    private boolean sePuedeMover = false;

    private PosicionGrafica ultimaPosicionOrigen = null;
    private PosicionGrafica ultimaPosicionDestino = null;

    public synchronized PosicionGrafica getUltimaPosicionDestino() {
        return ultimaPosicionDestino;
    }

    public synchronized void setUltimaPosicionDestino(PosicionGrafica ultimaPosicionDestino) {
        this.ultimaPosicionDestino = ultimaPosicionDestino;
    }

    public synchronized PosicionGrafica getUltimaPosicionOrigen() {
        return ultimaPosicionOrigen;
    }

    public synchronized void setUltimaPosicionOrigen(PosicionGrafica ultimaPosicionOrigen) {
        this.ultimaPosicionOrigen = ultimaPosicionOrigen;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        StackPane ventana = new StackPane();
        ventana.setStyle("-fx-background-color: beige;");

        String estiloTextFieldNegro =
                "-fx-font-size: 18px;" +
                        "-fx-padding: 10px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: #888;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #222;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #bbb;";

        javafx.scene.control.TextField nombreBlancas = new javafx.scene.control.TextField();
        nombreBlancas.setPromptText("Nombre Jugador Blancas");
        nombreBlancas.setMaxWidth(440);
        nombreBlancas.setStyle(estiloTextFieldNegro);

        javafx.scene.control.TextField nombreNegras = new javafx.scene.control.TextField();
        nombreNegras.setPromptText("Nombre Jugador Negras");
        nombreNegras.setMaxWidth(440);
        nombreNegras.setStyle(estiloTextFieldNegro);

        javafx.scene.control.Button botonEmpezar = new javafx.scene.control.Button("Empezar");
        botonEmpezar.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 20px 30px;" +
                        "-fx-background-color: rgba(30, 30, 30, 0.9);" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, black, 15, 0.4, 0, 2);" +
                        "-fx-font-weight: bold;"
        );
        botonEmpezar.setMinWidth(440);

        javafx.scene.control.Button botonWeb = new javafx.scene.control.Button("Web");
        javafx.scene.control.Button botonSalir = new javafx.scene.control.Button("Salir");

        String estiloBotonPeque =
                "-fx-font-size: 20px;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10px 0px;" +
                        "-fx-background-color: rgba(30, 30, 30, 0.9);" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, black, 10, 0.3, 0, 1);" +
                        "-fx-font-weight: bold;" +
                        "-fx-min-width: 215px;" +
                        "-fx-max-width: 215px;";

        botonWeb.setStyle(estiloBotonPeque);
        botonSalir.setStyle(estiloBotonPeque);

        botonWeb.setOnAction(e -> getHostServices().showDocument("http:\\federicop.duckdns.org"));
        botonSalir.setOnAction(e -> Platform.exit());

        HBox hboxBotonesPeque = new HBox(10, botonWeb, botonSalir);
        hboxBotonesPeque.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vbox = new VBox(20, nombreBlancas, nombreNegras, botonEmpezar, hboxBotonesPeque);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        ventana.getChildren().add(vbox);

        botonEmpezar.setOnAction(e -> {
            iniciarJuego(
                    new String[] {nombreBlancas.getText(), nombreNegras.getText()}
            );
        });

        Scene escenaPrincipal = new Scene(ventana, DIMENSION_CASILLA * ANCHO, DIMENSION_CASILLA * ALTO);
        primaryStage.setTitle(TITULO_VENTANA);
        primaryStage.setScene(escenaPrincipal);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void iniciarJuego(String[] nombres) {
        GridPane grid = new GridPane();
        if (nombres[0].isEmpty()) nombres[0] = "Jugador Blancas";
        if (nombres[1].isEmpty()) nombres[1] = "Jugador Negras";
        motorJuego = new Motor(this, nombres);

        crearTablero(grid);
        inicializarTablero(motorJuego.getTablero());

        StackPane root = new StackPane(grid, pieceLayer);
        Scene scene = new Scene(root, DIMENSION_CASILLA * ANCHO, DIMENSION_CASILLA * ALTO);
        primaryStage.setScene(scene);

        Thread hiloMotor = new Thread(motorJuego);
        hiloMotor.setDaemon(true);
        hiloMotor.start();
    }

    private void crearTablero(GridPane grid){
        for (int y = 0; y < ALTO; y++) {
            for (int x = 0; x < ANCHO; x++) {
                Casilla tile = new Casilla((x + y) % 2 == 0);
                TableroGrafico[x][y] = tile;
                grid.add(tile, x, y);
            }
        }
    }

    private void inicializarTablero(Tablero tablero){
        for(org.example.jaquejaguarfx.motor.Casilla casilla : tablero.getCasillasConPieza()){
            aniadirPieza(casilla.toString(), casilla.getPosicion());
        }
    }

    public void aniadirPieza(String simbolo, Posicion posicion) {
        PiezaGrafica piece = new PiezaGrafica(simbolo);
        piece.relocate(posicion.getPosX() * DIMENSION_CASILLA, posicion.getPosY() * DIMENSION_CASILLA);
        pieceLayer.getChildren().add(piece);
    }

    public void moverPieza(PosicionGrafica posicionOrigen, PosicionGrafica posicionDestino) {
        double origenX = posicionOrigen.getCoordenadasPixeles()[0];
        double origenY = posicionOrigen.getCoordenadasPixeles()[1];;

        PiezaGrafica piezaAMover = null;
        for (javafx.scene.Node node : pieceLayer.getChildren()) {
            if (node instanceof PiezaGrafica) {
                PiezaGrafica piece = (PiezaGrafica) node;
                if (piece.getLayoutX() == origenX && piece.getLayoutY() == origenY) {
                    piezaAMover = piece;
                    break;
                }
            }
        }

        if (piezaAMover != null) {
            eliminarPieza(posicionDestino);
            piezaAMover.relocate(posicionDestino.getCoordenadasPixeles()[0], posicionDestino.getCoordenadasPixeles()[1]);
        } else {
            System.out.println("No se encontró ninguna pieza en la posición de origen.");
        }
    }

    public void eliminarPieza(PosicionGrafica posicion) {
        double targetX = posicion.getCoordenadasPixeles()[0];
        double targetY = posicion.getCoordenadasPixeles()[1];

        for (javafx.scene.Node node : pieceLayer.getChildren()) {
            if (node instanceof PiezaGrafica) {
                PiezaGrafica piece = (PiezaGrafica) node;
                if (piece.getLayoutX() == targetX && piece.getLayoutY() == targetY) {
                    piece.setVisible(false);
                    piece.relocate(-DIMENSION_CASILLA, -DIMENSION_CASILLA);
                    return;
                }
            }
        }
    }

    public Posicion[] registrarMovimiento() {
        setUltimaPosicionOrigen(null);
        setUltimaPosicionDestino(null);
        sePuedeMover = true;
        while (getUltimaPosicionOrigen() == null || getUltimaPosicionDestino() == null);

        sePuedeMover = false;
        return new Posicion[] {ultimaPosicionOrigen,ultimaPosicionDestino};
    }

    public void efectoShake(PosicionGrafica posicion) {
        Platform.runLater(() -> {
            double targetX = posicion.getCoordenadasPixeles()[0];
            double targetY = posicion.getCoordenadasPixeles()[1];

            for (javafx.scene.Node node : pieceLayer.getChildren()) {
                if (node instanceof PiezaGrafica) {
                    PiezaGrafica pieza = (PiezaGrafica) node;
                    if (pieza.getLayoutX() == targetX && pieza.getLayoutY() == targetY) {

                        final int distanciaShake = 10;
                        final Duration duracionShake = Duration.millis(50);

                        TranslateTransition moverIzquierda = new TranslateTransition(duracionShake, pieza);
                        moverIzquierda.setByX(-distanciaShake);

                        TranslateTransition moverDerecha = new TranslateTransition(duracionShake, pieza);
                        moverDerecha.setByX(distanciaShake * 2);

                        TranslateTransition moverAtras = new TranslateTransition(duracionShake, pieza);
                        moverAtras.setByX(-distanciaShake);

                        SequentialTransition shake = new SequentialTransition(
                                moverIzquierda,
                                moverDerecha,
                                moverAtras
                        );

                        shake.play();
                        return;
                    }
                }
            }
        });
    }

    public void anunciarTurno(Object objet){
        String mensajeTurno = " Turno de: " + objet.toString();
        cambiarCabezeraVentana(TITULO_VENTANA + "     " + mensajeTurno);
        mostrarMensaje(mensajeTurno, 0.75);
    }

    public void mensajeFinPartida(String jugadorGanador){
        String mensajeFinPartida = " FIN PARTIDA GANADOR: "+jugadorGanador.toUpperCase();
        cambiarCabezeraVentana(TITULO_VENTANA + mensajeFinPartida);
        mostrarMensaje(mensajeFinPartida, -1);
    }

    public void mostrarMensaje(String mensaje, double segundos) {
        Platform.runLater(() -> {
            Label etiqueta = new Label(mensaje);
            etiqueta.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 10px 16px;" +
                            "-fx-background-color: rgba(30, 30, 30, 0.9);" +
                            "-fx-background-radius: 8;" +
                            "-fx-effect: dropshadow(gaussian, black, 8, 0.3, 0, 1);" +
                            "-fx-font-weight: normal;"
            );

            StackPane contenedorMensaje = new StackPane(etiqueta);
            contenedorMensaje.setMouseTransparent(true);
            contenedorMensaje.setPickOnBounds(false);
            StackPane.setAlignment(etiqueta, javafx.geometry.Pos.CENTER);

            StackPane raiz = (StackPane) primaryStage.getScene().getRoot();
            raiz.getChildren().add(contenedorMensaje);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.4), contenedorMensaje);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.4), contenedorMensaje);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> raiz.getChildren().remove(contenedorMensaje));

            fadeIn.play();

            if (segundos > 0) {
                PauseTransition espera = new PauseTransition(Duration.seconds(segundos));
                espera.setOnFinished(e -> fadeOut.play());
                espera.play();
            }
        });
    }

    private void cambiarCabezeraVentana(String mensaje){
        Platform.runLater(() -> {
            primaryStage.setTitle(mensaje);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Casilla extends StackPane {
        public Casilla(boolean light) {
            Rectangle border = new Rectangle(DIMENSION_CASILLA, DIMENSION_CASILLA);
            border.setFill(light ? Color.BEIGE : Color.BROWN);
            getChildren().add(border);
        }
    }

    private class PiezaGrafica extends StackPane {
        private double mouseX, mouseY;

        public PiezaGrafica(String symbol) {
            setPrefSize(DIMENSION_CASILLA, DIMENSION_CASILLA);

            javafx.scene.text.Text text = new javafx.scene.text.Text(symbol);
            text.setStyle("-fx-font-size: 36;");
            getChildren().add(text);

            setOnMousePressed(e -> {
                if (!sePuedeMover) return;
                mouseX = e.getSceneX();
                mouseY = e.getSceneY();

                int origenX = (int) (getLayoutX()) / DIMENSION_CASILLA;
                int origenY = (int) (getLayoutY()) / DIMENSION_CASILLA;
                ultimaPosicionOrigen = new PosicionGrafica(origenX,origenY);
            });

            setOnMouseDragged(e -> {
                if (!sePuedeMover) return;
                relocate(e.getSceneX() - DIMENSION_CASILLA / 2, e.getSceneY() - DIMENSION_CASILLA / 2);
            });

            setOnMouseReleased(e -> {
                if (!sePuedeMover) return;
                int newX = (int) (getLayoutX() + DIMENSION_CASILLA / 2) / DIMENSION_CASILLA;
                int newY = (int) (getLayoutY() + DIMENSION_CASILLA / 2) / DIMENSION_CASILLA;

                if (newX >= 0 && newX < ANCHO && newY >= 0 && newY < ALTO)
                    ultimaPosicionDestino = new PosicionGrafica(newX,newY);
                else
                    efectoShake(ultimaPosicionOrigen);
                recolocar(ultimaPosicionOrigen);
            });
        }
        private void recolocar(PosicionGrafica posicion){
            int[] coordenadas = posicion.getCoordenadasPixeles();
            relocate(coordenadas[0],coordenadas[1]);
        }
    }
}