package assignment07;

import com.github.javafaker.Faker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.Set;

public class CollisionInspector {
    static Set<String> names = new HashSet<>();

    public static void createNames(int n){
        Faker faker = new Faker();
        names.clear();
        while (names.size() < n) {
            String first = faker.name().firstName();
            String last = faker.name().lastName();
            names.add(first + " " + last);
        }
    }

    public static class Result {
        int capacity;
        int collisions;

        Result(int capacity, int collisions) {
            this.capacity = capacity;
            this.collisions = collisions;
        }

        int getCollisions() {
            return collisions;
        }
        int getCapacity() {
            return capacity;
        }
    }

    public static void plotResults(Map<String, Result[]> results, String title, String fileName) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Map.Entry<String, Result[]> r : results.entrySet()) {
            XYSeries series = new XYSeries(r.getKey());
            for (Result res: r.getValue()) {
                series.add(res.getCapacity(), res.getCollisions());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                title, "Capacity", "Collision count", dataset
        );

        XYPlot plot = chart.getXYPlot();
        plot.setForegroundAlpha(0.6f); // Set global alpha (value between 0.0f and 1.0f)

        JFrame frame = new JFrame("Plot");
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(false);

        try {
            OutputStream out = new FileOutputStream(fileName + ".png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static int countCollisions(int capacity, HashFunctor functor) {
        ChainingHashTable table = new ChainingHashTable(capacity, functor);
        table.addAll(names);
        LinkedList<String>[] storage = table.getStorage();
        int collisions = 0;
        for (LinkedList<String> list : storage) {
            if (list != null && list.size() > 1){
                collisions += list.size() - 1;
            }
        }
        return collisions;
    }

    public static Result[] getCollisionSeries(ArrayList<Integer> capacities, HashFunctor functor) {
        Result[] results = new Result[capacities.size()];

        for (int i = 0; i < capacities.size(); i++) {
            int capacity = capacities.get(i);
            int collisions = countCollisions(capacity, functor);
            results[i] = new Result(capacity, collisions);
        }

        return results;
    }

    public static void main(String[] args) {
        createNames(1000);
        ArrayList<Integer> capacities = new ArrayList<>(Arrays.asList(
                7, 17, 29, 41, 53, 67, 79, 97, 107, 127, 139, 157, 173
        ));

        Map<String, Result[]> results = new HashMap<>();

        BadHashFunctor badFunctor = new BadHashFunctor();
        Result[] badCollisions = getCollisionSeries(capacities, badFunctor);
        results.put("BadHashFunctor", badCollisions);

        MediocreHashFunctor mediocreFunctor = new MediocreHashFunctor();
        Result[] mediocreCollisions = getCollisionSeries(capacities, mediocreFunctor);
        results.put("MediocreHashFunctor", mediocreCollisions);
        GoodHashFunctor goodFunctor = new GoodHashFunctor();
        Result[] goodCollisions = getCollisionSeries(capacities, goodFunctor);
        results.put("GoodHashFunctor", goodCollisions);

        plotResults(results, "Collision count by capacity and hash functor", "Collisions");
    }
}
