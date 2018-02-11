package com.dl.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DLTradeTest extends Application {
    @Test
    public void test() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");

        Scene scene = new Scene(createChart(), 800, 600);
        stage.setScene(scene);

        stage.show();
    }

    private Parent createChart() {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");

        //creating the chart
        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Stock Monitoring, 2010");
        lineChart.getData().add(series);

        new Thread(() -> {
            List<XYChart.Data> dataList = new ArrayList<>();

            //populating the series with data
            dataList.add(new XYChart.Data(1, 23));
            dataList.add(new XYChart.Data(2, 14));
            dataList.add(new XYChart.Data(3, 15));
            dataList.add(new XYChart.Data(4, 24));
            dataList.add(new XYChart.Data(5, 34));
            dataList.add(new XYChart.Data(6, 36));
            dataList.add(new XYChart.Data(7, 22));
            dataList.add(new XYChart.Data(8, 45));
            dataList.add(new XYChart.Data(9, 43));
            dataList.add(new XYChart.Data(10, 17));
            dataList.add(new XYChart.Data(11, 29));
            dataList.add(new XYChart.Data(12, 25));


            for (XYChart.Data data : dataList) {
                Platform.runLater(() -> {
                    series.getData().add(data);
                });
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return lineChart;

    }
}
