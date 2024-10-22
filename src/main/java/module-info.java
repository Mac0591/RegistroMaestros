module com.registro.registromaestros {
    requires javafx.controls;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jfr;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.registro.registromaestros to javafx.fxml;
    exports com.registro.registromaestros;
    exports com.registro.registromaestros.excel;
    opens com.registro.registromaestros.excel to javafx.fxml;
}