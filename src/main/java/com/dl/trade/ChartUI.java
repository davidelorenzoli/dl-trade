package com.dl.trade;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChartUI {
    private static final Logger LOG = LoggerFactory.getLogger(ChartUI.class);

    // sellers
    private XYChart.Series askSeries;
    // buyers
    private XYChart.Series bidSeries;

    private AtomicInteger counter = new AtomicInteger(0);

    public Parent createChart() {
        askSeries = new XYChart.Series();
        askSeries.setName("Ask");

        bidSeries = new XYChart.Series();
        bidSeries.setName("Bid");

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Minutes");

        //creating the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Oder Book");
        lineChart.getData().add(askSeries);
        lineChart.getData().add(bidSeries);

        setLineColor(askSeries, Color.RED);
        setLineColor(bidSeries, Color.GREEN);

        return lineChart;
    }

    public void addTradeToChart(long bidCount, long askCount) {
            Platform.runLater(() -> {
            bidSeries.getData().add(new XYChart.Data(counter.get(), bidCount));
            askSeries.getData().add(new XYChart.Data(counter.get(), askCount));

            counter.incrementAndGet();

            if (bidSeries.getData().size() > 20) {
                LOG.info("Removing");
                bidSeries.getData().remove(0);
            }
            if (askSeries.getData().size() > 20) {
                LOG.info("Removing");
                askSeries.getData().remove(0);
            }
        });
    }

    private void setLineColor(XYChart.Series series, Color color) {
        Node line = series.getNode().lookup(".chart-series-line");

        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    }
}
