package com.dl.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Chart extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(Chart.class);

    // sellers
    private XYChart.Series askSeries;
    // buyers
    private XYChart.Series bidSeries;

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void init() throws Exception {
        Exchange Exchange = new Exchange();
        Exchange.addTradeListener(trade -> addTradeToChart(trade));
        Exchange.trade();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");

        Scene scene = new Scene(createChart(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    private Parent createChart() {
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

    private void addTradeToChart(Trade trade) {
        Platform.runLater(() -> {
            int second = Calendar.getInstance().get(Calendar.SECOND);
            XYChart.Data tradePrice = new XYChart.Data(counter.incrementAndGet(), new Random().nextInt(10));

//            if (trade.getType() == Order.OrderType.ASK) {
//                askSeries.getData().add(tradePrice);
//            }
//            if (trade.getType() == Order.OrderType.BID) {
//                bidSeries.getData().add(tradePrice);
//            }

            bidSeries.getData().add(tradePrice);

            if (bidSeries.getData().size() > 20) {
                LOG.info("Removing");
                bidSeries.getData().remove(0);
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
