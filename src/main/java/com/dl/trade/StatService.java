package com.dl.trade;

import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static org.knowm.xchange.dto.Order.OrderType.ASK;
import static org.knowm.xchange.dto.Order.OrderType.BID;

public class StatService {
    private static final Logger LOG = LoggerFactory.getLogger(StatService.class);

    private StatListener statListener;

    public StatService(TradeHistory tradeHistory) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<Trade> trades = tradeHistory.getAll(true);

                LOG.info("Counting trades: " + trades);

                long bidCount = trades.stream()
                        .filter(trade -> trade.getType() == BID)
                        .count();

                long askCount = trades.stream()
                        .filter(trade -> trade.getType() == ASK)
                        .count();

                statListener.onBidAskCount(bidCount, askCount);
            }
        }, Calendar.getInstance().getTime(), TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS));
    }

    public void setStatListener(StatListener statListener) {
        this.statListener = statListener;
    }

    public interface StatListener {
        public void onBidAskCount(long bidCount, long askCount);
    }
}
