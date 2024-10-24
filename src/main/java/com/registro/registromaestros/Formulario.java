package com.registro.registromaestros;

import com.registro.registromaestros.excel.Exportar;
import com.registro.registromaestros.excel.Lector;
import com.registro.registromaestros.model.Profesor;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Formulario extends Application {

    private ImageView imageView;
    private ImageView logoView;
    private TextField nombreText;
    private TextField eventoText;
    private TextField cspText;
    private TextField regionText;
    private TextField delegacionText;
    private TextField carteraText;
    private TextField folioText;
    private Button exportarBtn;
    private int contAsistentes;
    private String fileName;
    private Label asistenciasLabel;
    List<String> maestros;
    ArrayList<Profesor> asistencias;
    private StringBuilder qrDataBuilder = new StringBuilder(); // Para almacenar los datos leídos

    @Override
    public void start(Stage stage) {
        maestros = new Lector().readExcelFromResources();
        asistencias = new ArrayList<>();

        // Crear etiquetas
        Label nombreLabel = new Label("Nombre del Profesor:");
        Label cspLabel = new Label("C.S.P.:");
        Label regionLabel = new Label("Región:");
        Label delegacionLabel = new Label("Delegacion Sindical:");
        Label carteraLabel = new Label("Cartera:");
        Label folioLabel = new Label("Folio:");
        Label eventoLabel = new Label("Evento:");
        Label asistenciasTextoLabel = new Label("Asistentes:");
        // Crear un Label para mostrar el número
        asistenciasLabel = new Label(); // Reemplaza "42" por el número que desees
        asistenciasLabel.setStyle("-fx-font-size: 100px; -fx-text-fill: black;"); // Tamaño de fuente y color

        // Crear campos de texto no editables
        nombreText = new TextField();
        nombreText.setEditable(false);
        nombreText.setAlignment(Pos.CENTER);

        cspText = new TextField();
        cspText.setEditable(false);
        cspText.setAlignment(Pos.CENTER);

        regionText = new TextField();
        regionText.setEditable(false);
        regionText.setAlignment(Pos.CENTER);

        eventoText = new TextField();
        eventoText.setEditable(false);
        eventoText.setAlignment(Pos.CENTER);

        delegacionText = new TextField();
        delegacionText.setEditable(false);
        delegacionText.setAlignment(Pos.CENTER);

        carteraText = new TextField();
        carteraText.setEditable(false);
        carteraText.setAlignment(Pos.CENTER);

        folioText = new TextField();
        folioText.setEditable(false);
        folioText.setAlignment(Pos.CENTER);

        // Crear un ImageView para mostrar una imagen
        imageView = new ImageView();

        // Ajustar tamaño de la imagen
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        //Agregar Logo
        logoView = new ImageView();

        // Ajustar tamaño de la imagen
        logoView.setFitHeight(100);
        logoView.setFitWidth(100);
        logoView.setImage(new Image(getClass().getResourceAsStream("/img/Logo.png")));

        //Hacer boton
        exportarBtn = new Button("Exportar Excel");
        exportarBtn.setOnMouseClicked(event -> {
            if (mostrarAlerta()) {
                // Llamar al método que crea el archivo Excel
                Exportar export = new Exportar();
                export.writeDataToExcel(asistencias, fileName);
                asistencias.clear();
                asistenciasLabel.setText("0");
                cargarTexto(new Profesor());
                contAsistentes = 0;
                imageView.setImage(null);
                alertaExcelCreado();

            }
            cspText.requestFocus();
        });
        exportarBtn.getStyleClass().add("btn-success");

        alertaTitulo();

        VBox vbox = new VBox(10); // 10 es el espaciado entre nodos
        vbox.setAlignment(Pos.CENTER); // Centra el contenido en VBox
        vbox.getChildren().addAll(logoView, eventoLabel, eventoText, cspLabel, cspText, nombreLabel,
                nombreText, regionLabel, regionText, delegacionLabel, delegacionText, carteraLabel,
                carteraText, folioLabel, folioText, imageView, exportarBtn);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        // Crear espaciadores (puedes usar Region o VBox vacíos para los lados)
        Region leftSpacer = new Region();

        // Crear vbox a la derecha
        VBox rigthVbox = new VBox(10); // 10 es el espaciado entre nodos
        rigthVbox.setAlignment(Pos.CENTER); // Centra el contenido en VBox
        rigthVbox.getChildren().addAll(asistenciasTextoLabel, asistenciasLabel);

        // Asignar tamaño preferido a los espaciadores (ancho)
        leftSpacer.setPrefWidth(250);  // Espacio a la izquierda
        rigthVbox.setPrefWidth(250); // Espacio a la derecha

        // Agregar espaciadores a los lados del BorderPane
        borderPane.setLeft(leftSpacer);
        borderPane.setRight(rigthVbox);
        // Crear la escena y agregar el GridPane
        Scene scene = new Scene(borderPane, 800, 800);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.setOnKeyReleased(this::handleQRCodeInput);

        contAsistentes = 0;

        stage.setTitle("Registro de Asistencia de Maestros");
        stage.setScene(scene);
        stage.show();
    }

    public void dataExists(Profesor profesor) {
        //Corroborar que no es un duplicado
        if (asistencias.contains(profesor)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setHeaderText("Datos duplicados");
            alert.setContentText("Este QR ya se ha registrado.");
            alert.show();
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> alert.close()); // Cerrar la alerta al finalizar la pausa
            pause.play();
        } else
            //Corroborar si existe en el excel
            if (maestros.contains(profesor.getCsp())) {
                contAsistentes++;
                asistenciasLabel.setText(String.valueOf(contAsistentes));
                imageView.setImage(new Image(getClass().getResourceAsStream("/img/Correct.png")));
                asistencias.add(profesor);
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/img/Wrong.png")));
            }
    }

    private void handleQRCodeInput(KeyEvent event) {
        // Si el lector QR envía el carácter "Enter" al final del escaneo, procesar el código
        if (event.getCode().toString().equals("ENTER")) {
            processQRCode();
            qrDataBuilder.setLength(0); // Limpiar después de procesar
        } else {
            // Capturar cada carácter leído del QR y agregarlo al StringBuilder
            qrDataBuilder.append(event.getText());
        }
    }

    private void processQRCode() {
        String qrData = qrDataBuilder.toString();

        // Dividir la cadena usando el delimitador "||"
        String[] datos = qrData.split("\\|\\|");

        // Asegurarse de que la cantidad de datos es correcta
        if (datos.length == 7) {
            // Crear un objeto Empleado usando los datos leídos
            Profesor profesor = new Profesor(
                    datos[0],               // CSP
                    datos[1].toUpperCase(), // Nombre
                    datos[2],               // Region
                    datos[3].toUpperCase(), // Delegacion
                    datos[4].toUpperCase(), // Cartera
                    datos[5]                // Folio
            );

            profesor.setCartera(controlarAcentos(profesor.getCartera()));
            cargarTexto(profesor);
            dataExists(profesor);

        } else {
            // Mostrar un mensaje de error si los datos no son válidos
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Datos incorrectos");
            alert.setContentText("Favor de intentar leer de nuevo el código QR.");
            alert.show();
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> alert.close()); // Cerrar la alerta al finalizar la pausa
            pause.play();
        }
    }

    public void cargarTexto(Profesor profesor) {
        nombreText.setText(profesor.getNombreProfesor());
        cspText.setText(profesor.getCsp());
        regionText.setText(profesor.getRegion());
        delegacionText.setText(profesor.getDelegacionSindical());
        carteraText.setText(profesor.getCartera());
        folioText.setText(profesor.getFolio());
    }

    private boolean mostrarAlerta() {
        // Crear una alerta de tipo confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Al crear el archivo excel con la lista de asistentes se borraran los registros actuales" +
                "\n¿Desea terminar la lista?");
        alert.setContentText("Presiona Sí para continuar o No para cancelar.");

        // Personalizar los botones de la alerta
        ButtonType buttonYes = new ButtonType("Sí");
        ButtonType buttonNo = new ButtonType("No");

        // Establecer los botones en la alerta
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        // Mostrar la alerta y esperar por la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();

        // Verificar cuál botón fue presionado
        return result.isPresent() && result.get() == buttonYes;
    }

    private void alertaTitulo() {
        // Crear un ComboBox con algunas opciones
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll( "ASUNTOS ECONÓMICOS",
                "ASUNTOS PROFESIONALES Y DE CULTURA GENERAL", "ASUNTOS MÉDICO ASISTENCIALES",
                "ASUNTOS POLÍTICO SINDICALES", "ASUNTOS GENERALES");

        // Seleccionar el primer valor por defecto
        comboBox.setValue(comboBox.getItems().get(0));

        // Crear una alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Selecciona una opción");
        alert.setHeaderText("Por favor, selecciona una opción de la lista:");

        // Añadir el ComboBox al contenido del DialogPane
        DialogPane dialogPane = alert.getDialogPane();
        VBox content = new VBox(10, comboBox); // VBox con un espacio de 10 entre los elementos
        dialogPane.setContent(content);

        // Mostrar la alerta y esperar la respuesta del usuario
        alert.showAndWait().ifPresentOrElse(response -> {
            fileName = comboBox.getValue();
            eventoText.setText(fileName);
        }, () ->{
            Platform.exit();
        });

    }

    private void alertaTitulo(int val) {
        // Crear un ComboBox con algunas opciones
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll( "ASUNTOS ECONÓMICOS",
                "ASUNTOS PROFESIONALES Y DE CULTURA GENERAL", "ASUNTOS MÉDICO ASISTENCIALES",
                "ASUNTOS POLÍTICO SINDICALES", "ASUNTOS GENERALES");

        // Seleccionar el primer valor por defecto
        comboBox.setValue(comboBox.getItems().get(val));

        // Crear una alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Selecciona una opción");
        alert.setHeaderText("Por favor, selecciona una opción de la lista:");

        // Añadir el ComboBox al contenido del DialogPane
        DialogPane dialogPane = alert.getDialogPane();
        VBox content = new VBox(10, comboBox); // VBox con un espacio de 10 entre los elementos
        dialogPane.setContent(content);

        // Mostrar la alerta y esperar la respuesta del usuario
        alert.showAndWait().ifPresent(response -> {
            fileName = comboBox.getValue();
            eventoText.setText(fileName);
        });

    }

    private void alertaExcelCreado(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("El archivo se ha creado Exitosamente");
        alert.setContentText("Puede encontrar el archivo en su escritorio.");
        alert.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            alert.close();
            // Llamar a la segunda alerta al cerrar la primera
            Platform.runLater(this::alertaTitulo);
        });
        pause.play();
    }

    public String controlarAcentos(String acentuada) {
        //0211 = ó
        //0205 = í
        if (acentuada.equals("SRIO. DE PRENSA Y ACCI0211N POL0205TICA")) {
            return "SRIO. DE PRENSA Y ACCIÓN POLÍTICA";
        }
        return acentuada;
    }

    public static void main(String[] args) {
        launch();
    }
}