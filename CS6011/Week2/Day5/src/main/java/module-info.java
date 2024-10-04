module com.example.day5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.day5 to javafx.fxml;
    exports com.example.day5;
}