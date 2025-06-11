package org.example.jaquejaguarfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.jaquejaguarfx.motor.Posicion;
import org.example.jaquejaguarfx.motor.Tablero;

public class VentanaAjedrez extends Application {

    public static final int DIMENSION_CASILLA = 80;
    private static final int ANCHO = 8;
    private static final int ALTO = 8;
    private static final String TITULO_VENTANA = "JAQUE JAGUAR";
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
        GridPane grid = new GridPane();
        Motor motor = new Motor(this);

        crearTablero(grid);

        inicializarTablero(motor.getTablero());

        StackPane root = new StackPane(grid, pieceLayer);
        Scene scene = new Scene(root, DIMENSION_CASILLA * ANCHO, DIMENSION_CASILLA * ALTO);
        primaryStage.setTitle(TITULO_VENTANA);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.primaryStage = primaryStage;

        Thread hiloMotor = new Thread(motor);
        hiloMotor.setDaemon(true); // para que no impida cerrar la aplicación
        hiloMotor.start();
    }

    private void crearTablero(GridPane grid){
        // Crear tablero
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

        // Buscar la pieza en la capa de piezas
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
                    piece.relocate(-DIMENSION_CASILLA, -DIMENSION_CASILLA); // Equivale a (-1, -1)
                    return;
                }
            }
        }
    }

    //No deshace la captura, arreglar si hace falta
    public void deshacerMovimiento(){
        moverPieza(ultimaPosicionDestino,ultimaPosicionOrigen);
    }

    public Posicion[] registrarMovimiento() {
        setUltimaPosicionOrigen(null);
        setUltimaPosicionDestino(null);
        sePuedeMover = true;
        System.out.println("Esperando movimiento");
        while (getUltimaPosicionOrigen() == null || getUltimaPosicionDestino() == null);
        //mientras que uno de los 2 sea nulo

        System.out.println("Movimiento registrado bloqueando interfaz...");
        sePuedeMover = false;
        return new Posicion[] {ultimaPosicionOrigen,ultimaPosicionDestino};
    }

    public void anunciarTurno(Object objet){
        String mensajeTurno = " Turno de: " + objet.toString();
        cambiarCabezeraVentana(TITULO_VENTANA + "     " + mensajeTurno);
        mostrarMensaje(mensajeTurno, 1);
    }

    public void mensajeFinPartida(String jugadorGanador){
        String mensajeFinPartida = " FIN PARTIDA GANADOR: "+jugadorGanador.toUpperCase();
        cambiarCabezeraVentana(TITULO_VENTANA + mensajeFinPartida);
        mostrarMensaje(mensajeFinPartida, -1);
    }

    public void mostrarMensaje(String mensaje, int segundos) {
        Platform.runLater(() -> {
            // Crear una etiqueta con el mensaje
            javafx.scene.control.Label etiqueta = new javafx.scene.control.Label(mensaje);
            etiqueta.setStyle("-fx-font-size: 48px; -fx-text-fill: red; -fx-font-weight: bold; " +
                    "-fx-background-color: rgba(255,255,255,0.75); -fx-padding: 20px;");
            etiqueta.setWrapText(true); // Permitir que el texto se divida en varias líneas si es necesario

            // Crear un contenedor para centrar la etiqueta
            StackPane contenedorMensaje = new StackPane(etiqueta);
            contenedorMensaje.setPickOnBounds(false); // Permite que los clics pasen al contenido de abajo
            contenedorMensaje.setMouseTransparent(true); // No intercepta eventos del ratón

            // Hacer que el contenedor ocupe toda la ventana
            contenedorMensaje.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            StackPane.setAlignment(etiqueta, javafx.geometry.Pos.CENTER); // Centrar el texto

            // Añadir el mensaje a la raíz de la escena
            StackPane raiz = (StackPane) primaryStage.getScene().getRoot();
            raiz.getChildren().add(contenedorMensaje);

            // Si se indica una duración (segundos > 0), programar su desaparición
            if (segundos != -1) {
                javafx.animation.PauseTransition espera = new javafx.animation.PauseTransition(
                        javafx.util.Duration.seconds(segundos)
                );
                espera.setOnFinished(evento -> raiz.getChildren().remove(contenedorMensaje));
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

    // Casilla del tablero
    private static class Casilla extends StackPane {
        public Casilla(boolean light) {
            Rectangle border = new Rectangle(DIMENSION_CASILLA, DIMENSION_CASILLA);
            border.setFill(light ? Color.BEIGE : Color.BROWN);
            getChildren().add(border);
        }
    }

    // Pieza de ajedrez
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

                if (newX >= 0 && newX < ANCHO && newY >= 0 && newY < ALTO) {
                    ultimaPosicionDestino = new PosicionGrafica(newX,newY);
                    recolocar(ultimaPosicionOrigen);
                    //moverPieza(ultimaPosicionOrigen,ultimaPosicionDestino);
                } else {
                    recolocar(ultimaPosicionOrigen);
                }

            });
        }
        private void recolocar(PosicionGrafica posicion){
            int[] coordenadas = posicion.getCoordenadasPixeles();
            relocate(coordenadas[0],coordenadas[1]);
        }
    }
}