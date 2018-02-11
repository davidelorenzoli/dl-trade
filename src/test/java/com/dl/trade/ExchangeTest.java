package com.dl.trade;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.gdax.GDAXStreamingExchange;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExchangeTest {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeTest.class);

    @Test
    public void test() throws IOException {
        StreamingExchange exchange = StreamingExchangeFactory.INSTANCE.createExchange(GDAXStreamingExchange.class.getName());

        // Connect to the Exchange WebSocket API. Blocking wait for the connection.
        ProductSubscription productSubscription = ProductSubscription
                .create()
                .addAll(CurrencyPair.BTC_EUR)
                .build();

        exchange.connect(productSubscription).blockingAwait();

        // Subscribe to live trades update.
        exchange.getStreamingMarketDataService()
                .getTrades(CurrencyPair.BTC_EUR)
                .subscribe(trade -> {
                    //LOG.info("Incoming trade: {}", trade);
                }, throwable -> {
                    LOG.error("Error in subscribing trades.", throwable);
                });

        // Subscribe order book data with the reference to the subscription.
        Disposable subscription = exchange.getStreamingMarketDataService()
                .getOrderBook(CurrencyPair.BTC_EUR, 1)
                .subscribe(orderBook -> {
                    LOG.info("Order book: {}/{}", orderBook.getAsks(), orderBook.getBids());
                });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            LOG.error("", e);
        }

        // Unsubscribe from data order book.
        //subscription.dispose();

        // Disconnect from exchange (non-blocking)
        //exchange.disconnect().subscribe(() -> LOG.info("Disconnected from the Exchange"));
    }
}
