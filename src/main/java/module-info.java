module app.souvik.captureeasy {
    requires javafx.controls;
    requires javafx.fxml;

    opens app.souvik.captureeasy to javafx.fxml;
    exports app.souvik.captureeasy;
}