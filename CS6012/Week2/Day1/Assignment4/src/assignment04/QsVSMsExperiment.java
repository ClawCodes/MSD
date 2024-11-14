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
import java.util.Comparator;

public class QsVSMsExperiment extends TimerTemplate {
    ArrayList<Integer> listUndertest = new ArrayList<>();
    String algoUnderTest = null;
    String caseUnderTest = null;

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public QsVSMsExperiment(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        listUndertest.clear();
        if (caseUnderTest.equals("BestCase")) {
            listUndertest = SortUtil.generateBestCase(n);
        } else if (caseUnderTest.equals("WorstCase")) {
            listUndertest = SortUtil.generateWorstCase(n);
        } else if (caseUnderTest.equals("AverageCase")) {
            listUndertest = SortUtil.generateAverageCase(n);
        } else {
            throw new IllegalArgumentException("Unknown case " + caseUnderTest);
        }
    }

    @Override
    protected void timingIteration(int n) {
        ArrayList<Integer> newList = new ArrayList<>();
        for (Integer i : listUndertest) {
            newList.add(i);
        }
        if (algoUnderTest.equals("QuickSort")) {
            SortUtil.quicksort(newList, Comparator.naturalOrder());
        } else if (algoUnderTest.equals("MergeSort")) {
            SortUtil.msDriver(newList, Comparator.naturalOrder());
        }
    }

    @Override
    protected void compensationIteration(int n) {
        ArrayList<Integer> newList = new ArrayList<>();
        for (Integer i : listUndertest) {
            newList.add(i);
        }
        if (algoUnderTest.equals("QuickSort")) {
        } else if (algoUnderTest.equals("MergeSort")) {
        }
    }

    protected static class ExperimentResult{
        public TimerTemplate.Result[] results;
        public String algo;

        ExperimentResult(TimerTemplate.Result[] results, String algo) {
            this.results = results;
            this.algo = algo;
        }
    }

    public static void plotResults(ArrayList<QsVSMsExperiment.ExperimentResult> results, String type) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (QsVSMsExperiment.ExperimentResult r : results) {
            XYSeries series = new XYSeries(r.algo);
            for (TimerTemplate.Result result : r.results) {
                series.add(result.n(), result.avgNanoSecs());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "QuickSort vs. MergeSort " + type, "Time: nanoseconds", "# of Elements", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

        try {
            OutputStream out = new FileOutputStream("QsVSMs_" + type + ".png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static void runExperiment(int[] problemSizes, int timesToLoop, String caseMethod, int threshold, String pivotMethod) {
        ArrayList<QsVSMsExperiment.ExperimentResult> caseResults = new ArrayList<>();
        QsVSMsExperiment timer = new QsVSMsExperiment(problemSizes, timesToLoop);
        timer.caseUnderTest = caseMethod;

        // QS
        timer.algoUnderTest = "QuickSort";
        SortUtil.setPivotMethod(pivotMethod);
        TimerTemplate.Result[] results = timer.run();
        caseResults.add(new QsVSMsExperiment.ExperimentResult(results, "QuickSort"));
        // MS
        timer.algoUnderTest = "MergeSort";
        SortUtil.setMsThreshold(threshold);
        results = timer.run();
        caseResults.add(new QsVSMsExperiment.ExperimentResult(results, "MergeSort"));

        plotResults(caseResults, caseMethod);
    }

    public static void main(String[] args) {
        int numProblemSizes = 10;
        int[] problemSizes = new int[numProblemSizes];

        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 5);
        }

        // Best MS threshold
        int threshold = 11;
        // Best QS pivot selector
        String pivotMethod = "randomPivot";
        int timesToLoop = 100;

        // BEST CASE TEST
//        runExperiment(problemSizes, timesToLoop, "BestCase", threshold, pivotMethod);
        runExperiment(problemSizes, timesToLoop, "AverageCase", threshold, pivotMethod);
//        runExperiment(problemSizes, timesToLoop, "WorstCase", threshold, pivotMethod);
    }
}
