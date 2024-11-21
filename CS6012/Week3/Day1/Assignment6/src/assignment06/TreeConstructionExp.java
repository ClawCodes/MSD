package assignment06;

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
import java.util.*;

public class TreeConstructionExp extends TimerTemplate {
    ArrayList<Integer> orderedSet = new ArrayList<>();
    ArrayList<Integer> unorderedSet = new ArrayList<>();
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    boolean useOrder = true;
    Random rand = new Random(new Date().getTime());
    int intToFind;
    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public TreeConstructionExp(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        orderedSet.clear();
        for (int i = 0; i < n; i++) {
            orderedSet.add(n);
        }
        if (useOrder) {
            bst.addAll(orderedSet);
        } else {
            unorderedSet = orderedSet;
            for (int i = 0; i < 10; i++) {
                Collections.shuffle(unorderedSet);
            }
            bst.addAll(unorderedSet);
        }

        intToFind = rand.nextInt(n);
    }

    @Override
    protected void timingIteration(int n) {
        intToFind = rand.nextInt(n);
        bst.contains(intToFind);
    }

    @Override
    protected void compensationIteration(int n) {
        intToFind = rand.nextInt(n);
    }

    protected static class ExperimentResult{
        public Result[] results;
        public String order;

        ExperimentResult(Result[] results, String order) {
            this.results = results;
            this.order = order;
        }
    }

    public static void plotResults(ArrayList<ExperimentResult> results) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (ExperimentResult r : results) {
            XYSeries series = new XYSeries(r.order);
            for (TimerTemplate.Result result : r.results) {
                series.add(result.avgNanoSecs(), result.n());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                "BinarySearchTree.contains() runtime: ordered vs. random order node additions", "Time: nanoseconds", "# of Elements", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();

        try {
            OutputStream out = new FileOutputStream("Contains.png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static Result[] run(int[] problemSizes, int iterNum, boolean useOrder){
        TreeConstructionExp timer = new TreeConstructionExp(problemSizes, iterNum);
        timer.useOrder = useOrder;
        return timer.run();
    }

    public static void main(String[] args) {
        int numProblemSizes = 10;
        int[] problemSizes = new int[numProblemSizes];

        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 10);
        }

        int iterCount = 100;

        ArrayList<ExperimentResult> results_ = new ArrayList<>();

        results_.add(new ExperimentResult(
                TreeConstructionExp.run(problemSizes, iterCount, true), "Ordered"
        ));
        results_.add(new ExperimentResult(
                TreeConstructionExp.run(problemSizes, iterCount, false), "Unordered"
        ));

        plotResults(results_);
    }
}

