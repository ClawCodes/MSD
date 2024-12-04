package assignment09;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Main {

    protected static class ExperimentResult {
        public TimerTemplate.Result[] results;
        public String label;

        ExperimentResult(TimerTemplate.Result[] results, String label) {
            this.results = results;
            this.label = label;
        }
    }

    public static void plotResults(ArrayList<ExperimentResult> results, String title, String outFile) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (ExperimentResult r : results) {
            XYSeries series = new XYSeries(r.label);
            for (TimerTemplate.Result result : r.results) {
                series.add(result.n(), result.avgNanoSecs());
            }
            dataset.addSeries(series);
        }


        JFreeChart chart = ChartFactory.createScatterPlot(
                title, "# of Elements", "Time: nanoseconds", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(false);

        try {
            OutputStream out = new FileOutputStream(outFile + ".png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static void main(String[] args) {
        int numProblemSizes = 10;
        int[] problemSizes = new int[numProblemSizes];
        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 5);
        }

        ArrayList<ExperimentResult> constructionResults = new ArrayList<>();
        // Bulk BSP construction
        BulkConstructionTimer bcT = new BulkConstructionTimer(problemSizes, 100);
        TimerTemplate.Result[] bulkResults = bcT.run();
        constructionResults.add(new ExperimentResult(bulkResults, "Bulk Construction"));
         // Empty BSP construction
        EmptyConstructionTimer ecT = new EmptyConstructionTimer(problemSizes, 100);
        TimerTemplate.Result[] emptyResults = ecT.run();
        constructionResults.add(new ExperimentResult(emptyResults, "Iterative insert"));

        plotResults(constructionResults, "Runtime for tree construction: Bulk constructor vs. iterative insert calls", "treeConstruction");

        ArrayList<ExperimentResult> collisionResults = new ArrayList<>();
        CollisionTimer ecC = new CollisionTimer(problemSizes, 100);
        CollisionTimer.bspCollision = true;
        TimerTemplate.Result[] bspCollisionResults = ecC.run();
        collisionResults.add(new ExperimentResult(bspCollisionResults, "BSPTree collision"));

        CollisionTimer.bspCollision = false;
        TimerTemplate.Result[] nonBspCollisionResults = ecC.run();
        collisionResults.add(new ExperimentResult(nonBspCollisionResults, "FarToNear traversal"));

        plotResults(collisionResults, "Runtime for collision methods: BSPTree.collision() vs. Far To Near traversal", "collisions");
    }
}
