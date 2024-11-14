package assignment04;

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
import java.util.Comparator;
import java.util.Random;

public class QuicksortExperiment extends TimerTemplate {
    ArrayList<Integer> listUndertest = new ArrayList<>();

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public QuicksortExperiment(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        listUndertest.clear();
        Random random = new Random(100);
        for (int i = 0; i < n; i++) {
            listUndertest.add(random.nextInt());
        }
    }

    @Override
    protected void timingIteration(int n) {
        ArrayList<Integer> newList = new ArrayList<>();
        for (Integer i : listUndertest) {
            newList.add(i);
        }
        SortUtil.quicksort(newList, Comparator.naturalOrder());
    }

    @Override
    protected void compensationIteration(int n) {
        ArrayList<Integer> newList = new ArrayList<>();
        for (Integer i : listUndertest) {
            newList.add(i);
        }
    }

    protected static class ExperimentResult{
        public Result[] results;
        public String method;

        ExperimentResult(Result[] results, String method) {
            this.results = results;
            this.method = method;
        }
    }

    public static void plotResults(ArrayList<ExperimentResult> results) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (ExperimentResult r : results) {
            XYSeries series = new XYSeries("Pivot method: " + r.method);
            for (TimerTemplate.Result result : r.results) {
                series.add(result.n(), result.avgNanoSecs());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "QuickSort Runtime with different pivot methods", "Time: nanoseconds", "# of Elements", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

        try {
            OutputStream out = new FileOutputStream("quicksortPivot.png");
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

        ArrayList<ExperimentResult> results_ = new ArrayList<>();
        // random, 3median, middle
        for (String pivotMethod : Arrays.asList("randomPivot", "threeMedianPivot", "middlePivot")) {
            QuicksortExperiment timer = new QuicksortExperiment(problemSizes, 10);
            SortUtil.setPivotMethod(pivotMethod);
            Result[] results = timer.run();
            results_.add(new ExperimentResult(results, pivotMethod));
        }

        plotResults(results_);

    }
}
