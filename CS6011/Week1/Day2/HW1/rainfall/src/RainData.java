import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.text.DecimalFormat;

class RainLog {
    public String month;
    public Integer year;
    public Double amount;

    public RainLog(String month, Integer year, Double amount) {
        this.month = month;
        this.year = year;
        this.amount = amount;
    }
};

public class RainData {
    private final ArrayList<RainLog> rainData = new ArrayList<>();
    private String cityName;
    private static final DecimalFormat decFormat = new DecimalFormat("0.00");

    public RainData(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner reader = new Scanner(file);

        this.cityName = reader.nextLine();

        while (reader.hasNextLine()) {
            rainData.add(new RainLog(reader.next(), reader.nextInt(), reader.nextDouble()));
        }
        reader.close();
    }

    private Map<String, Double> calculateAverages() {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Integer> monthCounts = new HashMap<>();
        for (RainLog log : rainData) {
            if (averages.containsKey(log.month)) {
                averages.put(log.month, averages.get(log.month) + log.amount);
            } else {
                averages.put(log.month, log.amount);
            }
            if (monthCounts.containsKey(log.month)) {
                monthCounts.put(log.month, monthCounts.get(log.month) + 1);
            } else {
                monthCounts.put(log.month, 1);
            }
        }
        for (String month : averages.keySet()) {
            averages.put(month, averages.get(month) / monthCounts.get(month));
        }
        return averages;
    }

    public void writeAverages() throws IOException {
        File file = new File("src/" + this.cityName + "_rainfall_results.txt");
        FileWriter writer = new FileWriter(file);

        Map<String, Double> averages = calculateAverages();
        double overall = 0;
        for (Map.Entry<String, Double> entry : averages.entrySet()) {
            overall += entry.getValue();
            writer.write(
                    "The average rainfall amount for " + entry.getKey() + " is " + decFormat.format(entry.getValue()) + " inches.\n"
            );
        }
        writer.write("The overall average for " + this.cityName + " is " + decFormat.format(overall) + " inches.\n");
        writer.close();
    }
}
