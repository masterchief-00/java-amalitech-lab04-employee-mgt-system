module com.kwizera.javaamalitechlabemployeemgtsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.kwizera.javaamalitechlabemployeemgtsystem to javafx.fxml;
    exports com.kwizera.javaamalitechlabemployeemgtsystem;
}