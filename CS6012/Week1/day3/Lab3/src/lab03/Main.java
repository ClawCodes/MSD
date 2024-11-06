package lab03;

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

public class Main {
    static TimerTemplate.Result[] results_ = null;

    public static void plotResults(){
        XYSeries series = new XYSeries("Results");
        for (TimerTemplate.Result result : results_) {
            series.add(result.n(), result.avgNanoSecs());
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Time of ArrayList.clear() to complete",
                "Time (nano seconds)",
                "Input size (number of elements in array)",
                dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);

        try {
            OutputStream out = new FileOutputStream("plot.png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }

    public static void main(String[] args) {
        int numProblemSizes = 15;
        int[] problemSizes = new int[numProblemSizes];

        for (int i = 0; i < problemSizes.length; i++) {
            problemSizes[i] = (int) Math.pow(2, i + 10);
        }

        Timer timer = new Timer(problemSizes, 100);
        results_ = timer.run();
        plotResults();
    }
}
