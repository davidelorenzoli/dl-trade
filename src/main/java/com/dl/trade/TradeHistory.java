package com.dl.trade;

import org.knowm.xchange.dto.marketdata.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradeHistory {
    private Object SYNC = new Object();
    private List<Trade> trades = new ArrayList<>();

    public void add(Trade trade) {
        synchronized (SYNC) {
            trades.add(trade);
        }
    }

    public List<Trade> getAll(boolean clear) {
        List<Trade> tradesCopy = new ArrayList<>();

        synchronized (SYNC) {
            tradesCopy.addAll(trades);

            if (clear) {
                trades.clear();
            }
        }

        return tradesCopy;
    }
}
