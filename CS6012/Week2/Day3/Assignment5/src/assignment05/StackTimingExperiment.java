package assignment05;

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

public class StackTimingExperiment extends TimerTemplate {
    ArrayStack<Integer> arrayStack = new ArrayStack<>();
    LinkedListStack<Integer> linkedStack = new LinkedListStack<>();
    public static String methodUndertest = "";
    public static String classUndertest = "";

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
        arrayStack.clear();
        linkedStack.clear();
        for (int i = 0; i < n; i++) {
            arrayStack.push(i);
            linkedStack.push(i);
        }
    }

    @Override
    protected void timingIteration(int n) {
        if (classUndertest.equals("ArrayStack")) {
            if (methodUndertest.equals("pop")) {
                arrayStack.pop();
            } else if (methodUndertest.equals("push")) {
                arrayStack.push(n);
            } else {
                arrayStack.peek();
            }
        } else {
            if (methodUndertest.equals("pop")) {
                linkedStack.pop();
            } else if (methodUndertest.equals("push")) {
                linkedStack.push(n);
            } else {
                linkedStack.peek();
            }
        }
    }

    @Override
    protected void compensationIteration(int n) {
        if (classUndertest.equals("ArrayStack")) {
            if (methodUndertest.equals("pop")) {
            } else if (methodUndertest.equals("push")) {
            } else {
            }
        } else {
            if (methodUndertest.equals("pop")) {
            } else if (methodUndertest.equals("push")) {
            } else {
            }
        }    }

    protected static class ExperimentResult {
        public Result[] results;
        public String stackType;

        ExperimentResult(Result[] results, String stackType) {
            this.results = results;
            this.stackType = stackType;
        }
    }

    public static void plotResults(ArrayList<ExperimentResult> results) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (ExperimentResult r : results) {
            XYSeries series = new XYSeries(r.stackType);
            for (Result result : r.results) {
                series.add(result.avgNanoSecs(), result.n());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                "ArrayStack vs. LinkedListStack " + methodUndertest + "() runtime growth", "Time: nanoseconds", "# of Elements", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

        try {
            OutputStream out = new FileOutputStream(methodUndertest + ".png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static void runTest(int[] problemSizes, String method) {
        ArrayList<ExperimentResult> results_ = new ArrayList<>();
        for (String classToTest : Arrays.asList("ArrayStack", "LinkedListStack")) {
            StackTimingExperiment.classUndertest = classToTest;
            StackTimingExperiment.methodUndertest = method;
            StackTimingExperiment timer = new StackTimingExperiment(problemSizes, 100);
            Result[] res = timer.run();
            results_.add(new ExperimentResult(res, classToTest));
        }
        plotResults(results_);
    }


    public static int[] generateProblemSizes() {
        int numProblemSizes = 10;
        int[] problemSizes = new int[numProblemSizes];

        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 10);
        }
        return problemSizes;
    }

    public static void main(String[] args) {
        var problemSizes = generateProblemSizes();
        runTest(problemSizes, "pop");
        runTest(problemSizes, "push");
        runTest(problemSizes, "peek");
    }
}
