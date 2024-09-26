import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

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
    private String cityName = "NO CITY";
    private static final DecimalFormat decFormat = new DecimalFormat("0.00");

    public void setCityName(String name) {
        Pattern linePattern = Pattern.compile("^\\w+\\s+\\d{4}\\s+\\d\\.\\d+");
        boolean match = linePattern.matcher(name).find();
        if (match) {
            throw new IllegalArgumentException("The provided city name " + name + " is not valid.");
        } else {
            this.cityName = name;
        }
    }

    public RainData(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner reader = new Scanner(file);

        setCityName(reader.nextLine());

        while (reader.hasNext()) {
            rainData.add(new RainLog(reader.next(), reader.nextInt(), reader.nextDouble()));
        }
        reader.close();
    }

    public ArrayList<RainLog> getRainData() {
        return rainData;
    }

    public String getCityName() {
        return cityName;
    }

    public Map<String, Double> calculateAverages() {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Integer> monthCounts = new HashMap<>();
        for (RainLog log : rainData) {
            if (averages.containsKey(log.month)) {
                averages.put(log.month, averages.get(log.month) + log.amount);
                monthCounts.put(log.month, monthCounts.get(log.month) + 1);
            } else {
                averages.put(log.month, log.amount);
                monthCounts.put(log.month, 1);
            }
        }
        averages.replaceAll((m, v) -> v / monthCounts.get(m));
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
