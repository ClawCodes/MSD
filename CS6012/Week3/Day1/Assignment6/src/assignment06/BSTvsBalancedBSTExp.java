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

public class BSTvsBalancedBSTExp extends TimerTemplate {
    ArrayList<Integer> unorderedSet = new ArrayList<>();
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    TreeSet<Integer> treeSet = new TreeSet<>();
    String treeType;
    String runType;

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public BSTvsBalancedBSTExp(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        unorderedSet.clear();
        bst.clear();
        treeSet.clear();
        for (int i = 0; i < n; i++) {
            unorderedSet.add(n);
        }
        bst.addAll(unorderedSet);
        treeSet.addAll(unorderedSet);
    }

    @Override
    protected void timingIteration(int n) {
        if (runType.equals("contains")){
            if (treeType.equals("bst")){
                bst.contains(n);
            } else{
                treeSet.contains(n);
            }
        } else {
            if (treeType.equals("bst")){
                BinarySearchTree<Integer> bst = new BinarySearchTree<>();
                bst.addAll(unorderedSet);
            } else{
                TreeSet<Integer> treeSet = new TreeSet<>();
                treeSet.addAll(unorderedSet);
            }
        }
    }

    @Override
    protected void compensationIteration(int n) {
        if (runType.equals("contains")){
            if (treeType.equals("bst")){
            } else{
            }
        } else {
            if (treeType.equals("bst")){
                BinarySearchTree<Integer> bst = new BinarySearchTree<>();
            } else{
                TreeSet<Integer> treeSet = new TreeSet<>();
            }
        }
    }

    protected static class ExperimentResult{
        public Result[] results;
        public String description;

        ExperimentResult(Result[] results, String description) {
            this.results = results;
            this.description = description;
        }
    }

    public static void plotResults(ArrayList<ExperimentResult> results, String title, String fileName) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (ExperimentResult r : results) {
            XYSeries series = new XYSeries(r.description);
            for (TimerTemplate.Result result : r.results) {
                series.add(result.avgNanoSecs(), result.n());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                title, "Time: nanoseconds", "# of Elements", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();

        try {
            OutputStream out = new FileOutputStream(fileName + ".png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static Result[] run(int[] problemSizes, int iterNum, String treeType, String runType){
        BSTvsBalancedBSTExp timer = new BSTvsBalancedBSTExp(problemSizes, iterNum);
        timer.treeType = treeType;
        timer.runType = runType;
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
                BSTvsBalancedBSTExp.run(problemSizes, iterCount, "bst", "contains"), "BinarySearchTree"
        ));
        results_.add(new ExperimentResult(
                BSTvsBalancedBSTExp.run(problemSizes, iterCount, "TreeSet", "contains"), "TreeSet"
        ));

        plotResults(results_, "Runtime of contains() for Java TreeSet and BinarySearchTree", "BSTvsTreeSetContains");

//        results_.add(new ExperimentResult(
//                BSTvsBalancedBSTExp.run(problemSizes, iterCount, "bst", "addAll"), "BinarySearchTree"
//        ));
//        results_.add(new ExperimentResult(
//                BSTvsBalancedBSTExp.run(problemSizes, iterCount, "TreeSet", "addAll"), "TreeSet"
//        ));
//
//        plotResults(results_, "Runtime of addAll() for Java TreeSet and BinarySearchTree", "BSTvsTreeSetAddAll");
    }
}

