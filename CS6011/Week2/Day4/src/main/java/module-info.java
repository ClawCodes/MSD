module com.example.day4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.day4 to javafx.fxml;
    exports com.example.day4;
}