package com.dl.trade;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.gdax.GDAXStreamingExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Exchange {
    private static final Logger LOG = LoggerFactory.getLogger(Exchange.class);

    private TradeListener tradeListener;

    public void trade() {
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
                    LOG.info("Incoming trade: {}", trade);
                    tradeListener.onTrade(trade);
                }, throwable -> {
                    LOG.error("Error in subscribing trades.", throwable);
                });
    }

    public void addTradeListener(TradeListener tradeListener) {
        this.tradeListener = tradeListener;
    }

    public interface TradeListener {
        public void onTrade(Trade trade);
    }
}
