import org.w3c.dom.Text;

import java.awt.*;

// Scratch file for in class examples
public class Main {
    public static void main(String[] args) {
        // Assuming this is in a JavaFX project under the HelloApplication main

        stage.setTitle("Hello!");

        BoarderPane bp = new BoarderPane();

        HBox hb = new HBox();
        bp.setCenter(hb);

        Button btn = new Button();
        btn.setText("Press Me");
        // To set the action on and event, you need to pass the method you want to call after the event
        // You need to provide the method in a lambda
        // e is the event that happened which is of type ActionEvent
        btn.setOnAction(e -> handleButtonPress(e));


        Slider slider = new Slider();
        slider.setMin(0.0);
        slider.setMax(1.0);

        hbox.getChildren().add(slider);

        Text text = new Text();
        text.setText("Hello");

        slider.setOnMouseDragged(e -> handleSlider(text));


        // You need to gather the children array to add components to it
        // HBox provided a horizontal plane to add components to
        hbox.getChildren().add(btn);
        hbox.getChildren().add(slider);
        hbox.getChildren().add(text);

        // OR
        hbox.getChildren().addAll(btn, slider, text);

        Scene scene = new Scene(bp, 400, 240);

        stage.setScene(scene);
        stage.show();

    }
}