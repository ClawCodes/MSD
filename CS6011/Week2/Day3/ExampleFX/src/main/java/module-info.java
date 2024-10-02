module com.example.examplefx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.examplefx to javafx.fxml;
    exports com.example.examplefx;
}