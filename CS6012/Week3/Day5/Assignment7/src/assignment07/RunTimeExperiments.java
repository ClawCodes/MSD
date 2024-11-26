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


public class RunTimeExperiments extends TimerTemplate {
    static Set<String> names_ = new HashSet<>();
    static ChainingHashTable table_;
    static String methodUnderTest; // add, contains, remove
    static Faker faker_ = new Faker();

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public RunTimeExperiments(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        table_.clear();
        names_.clear();
        while (names_.size() < n) {
            String first = faker_.name().firstName();
            String last = faker_.name().lastName();
            names_.add(first + " " + last);
        }
    }

    @Override
    protected void timingIteration(int n) {
        String name = faker_.name().firstName() + " " + faker_.name().lastName();
        if (methodUnderTest.equals("add")) {
            table_.add(name);
            table_.remove(name);
        } else if (methodUnderTest.equals("remove")) {
            table_.add(name);
            table_.remove(name);
        } else if (methodUnderTest.equals("contains")) {
            table_.contains(name);
        } else {
            throw new RuntimeException("Unknown method: " + methodUnderTest);
        }
    }

    @Override
    protected void compensationIteration(int n) {
        String name = faker_.name().firstName() + " " + faker_.name().lastName();
        if (methodUnderTest.equals("add")) {
            table_.remove(name);
        } else if (methodUnderTest.equals("remove")) {
            table_.add(name);
        } else if (methodUnderTest.equals("contains")) {
        } else {
            throw new RuntimeException("Unknown method: " + methodUnderTest);
        }
    }

    protected static class ExperimentResult {
        public Result[] results;
        public String functor;

        ExperimentResult(Result[] results, String functor) {
            this.results = results;
            this.functor = functor;
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
            XYSeries series = new XYSeries(r.functor);
            for (Result result : r.results) {
                series.add(result.n(), result.avgNanoSecs());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                title, "# of Elements", "Time: nanoseconds", dataset
        );

        XYPlot plot = chart.getXYPlot();
        plot.setForegroundAlpha(0.75f); // Set global alpha (value between 0.0f and 1.0f)


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

    public static void runTest(int[] problemSizes, String method, String title, String fileName) {
        ArrayList<ExperimentResult> results_ = new ArrayList<>();
        for (HashFunctor func : Arrays.asList(new BadHashFunctor(), new MediocreHashFunctor(), new GoodHashFunctor())) {
            RunTimeExperiments timer = new RunTimeExperiments(problemSizes, 100);
            table_ = new ChainingHashTable(31, func);
            methodUnderTest = method;
            Result[] res = timer.run();
            results_.add(new ExperimentResult(res, func.getClass().getSimpleName()));
        }
        plotResults(results_, title, fileName);
    }


    public static void main(String[] args) {
        var problemSizes = generateProblemSizes();

        runTest(problemSizes, "add", "Runtime time of hash table add() with different hash functors", "addRuntime");
        runTest(problemSizes, "remove", "Runtime time of hash table remove() with different hash functors", "removeRuntime");
        runTest(problemSizes, "contains", "Runtime time of hash table contains() with different hash functors", "containsRuntime");
    }
}
