package com.example.synthesizer;

import com.example.synthesizer.widgets.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class SynthesizeApplication extends Application {
    private void play() {
        if (activeClip_ != null) {
            activeClip_.stopClip();
        }

        if (speaker_.getAudioComponent().isPlayable()) {
            activeClip_ = speaker_.getAudioComponent().getClip();
            activeClip_.playClip();
        }
    }

    private void stopClip() {
        if (activeClip_ != null) {
            activeClip_.stopClip();
            activeClip_ = null;
        }
    }

    private void createComponent(String name) {
        AudioComponentWidget widget = null;

        if (name.equals("SineWave"))
            widget = new SineWaveWidget(mainCanvas_, name);
        else if (name.equals("VolumeAdjuster")) {
            widget = new VolumeAdjusterWidget(mainCanvas_, name);
        } else if (name.equals("LinearRamp")) {
            widget = new LinearRampWidget(mainCanvas_, name);
        } else if (name.equals("Mixer")) {
            widget = new MixerWidget(mainCanvas_, name);
        } else if (name.equals("VFSineWave")) {
            widget = new VFSineWaveWidget(mainCanvas_, name);
        }

        if (widget == null) {
            System.out.println("Could not find requested widget: " + name);
        }

        allWidgets_.addFirst(widget); // Build the widget array in reverse, so the elements on top can be found first
        mainCanvas_.getChildren().add(widget);
    }

    private void resizeLine(MouseEvent mouseEvent) {
        if (currentLine_ != null) {
            currentLine_.setEndX(mouseEvent.getSceneX());
            currentLine_.setEndY(mouseEvent.getSceneY());
        }
    }

    private void updateCurrentLine(double x, double y) {
        LineWidget line = new LineWidget(x, y, x, y, mainCanvas_);
        line.setStroke(Color.BLUEVIOLET);
        line.setStrokeWidth(5);
        currentLine_ = line;
    }

    private void createLine(MouseEvent mouseEvent) {
        var x = mouseEvent.getSceneX();
        var y = mouseEvent.getSceneY();

        for (AudioComponentWidget widget : allWidgets_) {
            if (widget.IOContains(x, y)) {
                updateCurrentLine(x, y);
                return;
            }
        }
    }

    private void handleLine(MouseEvent mouseEvent) {
        if (currentLine_ == null) {
            return;
        }

        AudioComponentWidget input = null;
        AudioComponentWidget output = null;

        double startX = currentLine_.getStartX();
        double startY = currentLine_.getStartY();
        double endX = currentLine_.getEndX();
        double endY = currentLine_.getEndY();

//        for (var widget : allWidgets_) {
//            if ((input == null) && (widget.inputContains(startX, startY) | widget.inputContains(endX, endY))) {
//                input = widget;
//            }
//            if ((output == null) && (widget.outputContains(endX, endY) | widget.outputContains(startX, startY))) {
//                output = widget;
//            }
//        }

        for (var widget : allWidgets_) {
            if (input == null) {
                if (widget.inputContains(startX, startY)) {
                    input = widget;
                    currentLine_.setStartsFromOutput(false);
                } else if (widget.inputContains(endX, endY)) {
                    input = widget;
                }
            }
            if (output == null) {
                if (widget.outputContains(startX, startY)) {
                    output = widget;
                } else if (widget.outputContains(endX, endY)) {
                    output = widget;
                    currentLine_.setStartsFromOutput(false);
                }
            }
        }

        if (input == null | output == null) {
            mainCanvas_.getChildren().remove(currentLine_);
            return;
        }

        // TODO: Check if the found components already have inputs and outputs and adjust accordingly
        // Also check if attempting to connect to self
        // Add line to both components
        // Add both components to line widget

        // Persist line and update widget connections
        currentLine_.setInputWidget(input);
        currentLine_.setOutputWidget(output);
        currentLine_.connectComponents();

        System.out.println(speaker_.getAudioComponent().hasInput());
        lines_.add(currentLine_);
        currentLine_ = null;
    }

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // Right
        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(4));
        rightPane.setStyle("-fx-background-color: black;");

        // TODO: Create function
        // Create and add components to right pane
        // SineWave
        Button sineWaveBtn = new Button(SineWave.class.getSimpleName());
        rightPane.getChildren().add(sineWaveBtn);
        sineWaveBtn.setOnAction(event -> {
            createComponent(SineWave.class.getSimpleName());
        });

        // VolumeAdjuster
        Button volAdjusterBtn = new Button(VolumeAdjuster.class.getSimpleName());
        rightPane.getChildren().add(volAdjusterBtn);
        volAdjusterBtn.setOnAction(event -> {
            createComponent(VolumeAdjuster.class.getSimpleName());
        });

        // LinearRamp
        Button LinearRampBtn = new Button(LinearRamp.class.getSimpleName());
        rightPane.getChildren().add(LinearRampBtn);
        LinearRampBtn.setOnAction(event -> {
            createComponent(LinearRamp.class.getSimpleName());
        });

        // Mixer
        Button MixerBtn = new Button(Mixer.class.getSimpleName());
        rightPane.getChildren().add(MixerBtn);
        MixerBtn.setOnAction(event -> {
            createComponent(Mixer.class.getSimpleName());
        });

        // VFSineWave 
        Button VFSineWaveBtn = new Button(VFSineWave.class.getSimpleName());
        rightPane.getChildren().add(VFSineWaveBtn);
        VFSineWaveBtn.setOnAction(event -> {
            createComponent(VFSineWave.class.getSimpleName());
        });

        // Center
        mainCanvas_ = new AnchorPane();
        mainCanvas_.setStyle("-fx-background-color: black;");
        mainCanvas_.setOnMousePressed(this::createLine);
        mainCanvas_.setOnMouseDragged(this::resizeLine);
        mainCanvas_.setOnMouseReleased(mouseEvent -> {
            this.handleLine(mouseEvent);
        });

        speaker_ = new Speaker(mainCanvas_);
        allWidgets_.add(speaker_);
        mainCanvas_.getChildren().add(speaker_);

        // Bottom
        HBox bottomPane = new HBox();
        bottomPane.setAlignment(Pos.CENTER);

        Button playBtn = new Button("Play");
        playBtn.setOnAction(event -> {
            play();
        });
        bottomPane.getChildren().add(playBtn);

        Button stopBtn = new Button("Stop");
        stopBtn.setOnAction(event -> {
            stopClip();
        });
        bottomPane.getChildren().add(stopBtn);

        root.setRight(rightPane);
        root.setCenter(mainCanvas_);
        root.setBottom(bottomPane);

        return root;
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent(), 800, 800);
        stage.setScene(scene);
        stage.setTitle("Synth Lord");
        stage.show();
    }

    public static void removeWidget(AudioComponentWidget widget) {
        allWidgets_.remove(widget);
        mainCanvas_.getChildren().remove(widget);
    }

    public static void main(String[] args) {
        launch();
    }

    private static ArrayList<AudioComponentWidget> allWidgets_ = new ArrayList<>();
    private static AnchorPane mainCanvas_;
    private static Speaker speaker_;
    private AudioClip activeClip_;
    private static ArrayList<LineWidget> lines_ = new ArrayList<>();
    private static LineWidget currentLine_;
}