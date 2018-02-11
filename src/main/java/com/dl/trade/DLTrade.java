package com.dl.trade;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DLTrade extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(DLTrade.class);

    private ChartUI chartUI;

    private TradeHistory tradeHistory;

    private StatService statService;

    @Override
    public void init() {
        tradeHistory = new TradeHistory();
        chartUI = new ChartUI();

        ExchangeService ExchangeService = new ExchangeService();
        ExchangeService.setTradeListener(trade -> {
            tradeHistory.add(trade);
        });
        ExchangeService.trade();

        statService = new StatService(tradeHistory);
        statService.setStatListener((bidCoung, askCount) -> {
            chartUI.addTradeToChart(bidCoung, askCount);
        });
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");

        Scene scene = new Scene(chartUI.createChart(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
