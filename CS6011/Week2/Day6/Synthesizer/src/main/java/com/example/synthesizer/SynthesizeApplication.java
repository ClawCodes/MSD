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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SynthesizeApplication extends Application {

    // TODO: Update to play the speaker widget
    private void play() {
        Mixer mixer = new Mixer();
        for (AudioComponentWidget component : allWidgets_){
            if (!component.getAudioComponent().isPlayable()){
                continue;
            }
            mixer.connectInput(component.getAudioComponent());
        }
        if (activeClip_ != null) {
            activeClip_.stopClip();
        }

        if (!mixer.isPlayable()){
            System.out.println("WARNING: No on screen elements are currently playable.");
        } else {
            activeClip_ = mixer.getClip();
            activeClip_.playClip();
        }
    }

    private void stopClip(){
        activeClip_.stopClip();
        activeClip_ = null;
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
            System.out.println("Could not find requested wiget: " + name);
        }

        allWidgets_.add(widget);
        mainCanvas_.getChildren().add(widget);
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
        sineWaveBtn.setOnAction(event -> {createComponent(SineWave.class.getSimpleName());});

        // VolumeAdjuster
        Button volAdjusterBtn = new Button(VolumeAdjuster.class.getSimpleName());
        rightPane.getChildren().add(volAdjusterBtn);
        volAdjusterBtn.setOnAction(event -> {createComponent(VolumeAdjuster.class.getSimpleName());});

        // LinearRamp
        Button LinearRampBtn = new Button(LinearRamp.class.getSimpleName());
        rightPane.getChildren().add(LinearRampBtn);
        LinearRampBtn.setOnAction(event -> {createComponent(LinearRamp.class.getSimpleName());});

        // Mixer
        Button MixerBtn = new Button(Mixer.class.getSimpleName());
        rightPane.getChildren().add(MixerBtn);
        MixerBtn.setOnAction(event -> {createComponent(Mixer.class.getSimpleName());});
        
        // VFSineWave 
        Button VFSineWaveBtn = new Button(VFSineWave.class.getSimpleName());
        rightPane.getChildren().add(VFSineWaveBtn);
        VFSineWaveBtn.setOnAction(event -> {createComponent(VFSineWave.class.getSimpleName());});

        // Center
        mainCanvas_ = new AnchorPane();
        mainCanvas_.setStyle("-fx-background-color: black;");

        Speaker speakerWidget = new Speaker(mainCanvas_);
        mainCanvas_.getChildren().add(speakerWidget);

        // Bottom
        HBox bottomPane = new HBox();
        bottomPane.setAlignment(Pos.CENTER);

        Button playBtn = new Button("Play");
        playBtn.setOnAction(event -> {play();});
        bottomPane.getChildren().add(playBtn);

        Button stopBtn = new Button("Stop");
        stopBtn.setOnAction(event -> {stopClip();});
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
        stage.setTitle("Rad Synth");
        stage.show();
    }

    public static void removeWidget(AudioComponentWidget widget){
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
}