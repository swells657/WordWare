module com.example.fbla {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires org.apache.commons.lang3;

    opens com.example.fbla to javafx.fxml;
    exports com.example.fbla;
}