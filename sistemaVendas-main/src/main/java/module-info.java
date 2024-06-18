module javafxmvc.sistemavendas {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires jasperreports;
    requires jasperreports.fonts;

    opens javafxmvc.sistemavendas to javafx.fxml;
    opens javafxmvc.sistemavendas.controller to javafx.fxml;
    opens javafxmvc.sistemavendas.model.domain;

    exports javafxmvc.sistemavendas;
}
