import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RainData atlData = new RainData("data/Atlanta.txt");
        atlData.writeAverages();
        RainData maconData = new RainData("data/Macon.txt");
        maconData.writeAverages();
    }
}