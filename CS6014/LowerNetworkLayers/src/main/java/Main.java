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

    public static void main(String[] args) throws InterruptedException {
//        int[] netWorkSizes = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        int[] netWorkSizes = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
        int iterCount = 10;
        int[] messageCounts = new int[netWorkSizes.length];
        for (int i = 0; i < netWorkSizes.length; i++) {
            int runningCount = 0;
            for (int j = 0; j < iterCount; j++) {
                runningCount += runTrial(netWorkSizes[i]);
            }
            messageCounts[i] = runningCount / iterCount;
        }
        plotTrails(netWorkSizes, messageCounts);
    }

    public static int runTrial(int netWorkSize) throws InterruptedException {
        Network.makeProbablisticNetwork(netWorkSize);
        Network.dump();

        Network.startup();
        Network.runBellmanFord();

        return Network.getMessageCount();
    }

    public static void plotTrails(int[] x, int[] y){
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Exp");
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Average message count per network size", "Network size", "Avg. number of messages", dataset
        );

        JFrame frame = new JFrame("Plot");
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(false);

        try {
            OutputStream out = new FileOutputStream( "networkTrials.png");
            ChartUtils.writeChartAsPNG(out, chart, frame.getWidth(), frame.getHeight());
        } catch (IOException e) {
            System.out.println("Error when saving chart");
        }
    }
}
