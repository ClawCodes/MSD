module com.example.newsynth {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.newsynth to javafx.fxml;
    exports com.example.newsynth;
}