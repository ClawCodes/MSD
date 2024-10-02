// Scratch for in class examples

import java.util.ArrayList;

interface Instrument {
    void play();
    void setVolume(boolean loud);
}

class Trumpet implements Instrument{
    boolean isLoud = false;

    @Override
    public void play() {
        System.out.println("toot toot");
    }

    @Override
    public void setVolume(boolean loud) {
        isLoud = loud;
    }
}

class Drums implements Instrument{
    @Override
    public void play() {
        System.out.println("rat-a-tat-tat");
    }

    @Override
    public void setVolume(boolean loud) {
        return;
    }
}

class ElectricPiano implements Instrument{
    boolean isLoud = false;
    boolean isOn = false;

    @Override
    public void play() {
        if (isOn) {
            if (isLoud) {
                System.out.println("A-B-C-B-A");
            } else {
                System.out.println("a-b-c-b-a");
            }
        } else {
            System.out.println("...");
        }
    }

    @Override
    public void setVolume(boolean loud) {
        isLoud = loud;
    }

    public void turnOn(boolean on) {
        isOn = on;
    }
}

class Band{
    private ArrayList<Instrument> instruments = new ArrayList<>();
    private String name;

    Band(String name){
        this.name = name;
    }

    void addInstrument(Instrument instrument){
        instruments.add(instrument);
    }

    void play(){
        for(Instrument instrument : instruments){
            if (instrument instanceof ElectricPiano){
                ((ElectricPiano) instrument).turnOn(true);
            }
            instrument.setVolume(true);
            instrument.play();
        }
    }
}

class ConcertHall{
    Band currentAct;

    public void bookAct(Band band){
        currentAct = band;
    }

    public void play(){
        currentAct.play();
    }
}

public class Main {
    public static void main(String[] args) {
        Trumpet trumpet = new Trumpet();
        Drums drums = new Drums();
        ElectricPiano piano = new ElectricPiano();

        Band band = new Band("My Garage Band");
        band.addInstrument(trumpet);
        band.addInstrument(drums);
        band.addInstrument(piano);

        ConcertHall concertHall = new ConcertHall();
        concertHall.bookAct(band);
        concertHall.play();
    }
}