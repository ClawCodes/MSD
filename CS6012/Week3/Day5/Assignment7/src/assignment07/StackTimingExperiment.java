package assignment07;

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
import java.util.Arrays;
import java.util.Collections;


public class StackTimingExperiment extends TimerTemplate {

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public StackTimingExperiment(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
    }

    @Override
    protected void timingIteration(int n) {
    }

    @Override
    protected void compensationIteration(int n) {
    }

    protected static class ExperimentResult {
        public Result[] results;
        public String classType;

        ExperimentResult(Result[] results, String classType) {
            this.results = results;
            this.classType = classType;
        }

    }

    public static int[] generateProblemSizes() {
        int numProblemSizes = 10;
        int[] problemSizes = new int[numProblemSizes];

        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 10);
        }
        return problemSizes;
    }

    public static void plotResults(ArrayList<ExperimentResult> results, String title, String fileName) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (ExperimentResult r : results) {
            XYSeries series = new XYSeries(r.classType);
            for (Result result : r.results) {
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
            OutputStream out = new FileOutputStream(fileName + ".png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static void runTest(int[] problemSizes, boolean testAdd, boolean useOrder, String title, String fileName) {
        ArrayList<ExperimentResult> results_ = new ArrayList<>();
        for (String classToTest : Arrays.asList("ArrayQueue", "TreeSetQueue")) {
            StackTimingExperiment timer = new StackTimingExperiment(problemSizes, 100);
            Result[] res = timer.run();
            results_.add(new ExperimentResult(res, classToTest));
        }
        plotResults(results_, title, fileName);
    }


    public static void main(String[] args) {
        var problemSizes = generateProblemSizes();

//        runTest(problemSizes, true, true, "Runtime time of tree construction from ordered set", "TreeConstructionOrderedSet");
//        runTest(problemSizes, true, false, "Runtime time of tree construction from permuted set", "TreeConstructionPermutedSet");
//        runTest(problemSizes, false, true, "Runtime time of emptying tree with iterative removeMin()", "EmptyingTree");
    }
}
