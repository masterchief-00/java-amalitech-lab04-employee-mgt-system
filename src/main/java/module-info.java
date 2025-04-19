module com.kwizera.javaamalitechlabemployeemgtsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.kwizera.javaamalitechlabemployeemgtsystem to javafx.fxml;
    opens com.kwizera.javaamalitechlabemployeemgtsystem.controllers to javafx.fxml;
    opens com.kwizera.javaamalitechlabemployeemgtsystem.models to javafx.base;

    exports com.kwizera.javaamalitechlabemployeemgtsystem;
}