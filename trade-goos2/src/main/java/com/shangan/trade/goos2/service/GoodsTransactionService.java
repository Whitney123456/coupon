package com.shangan.trade.goos2.service;

public interface GoodsTransactionService {
    boolean tryGoodsStock();

    boolean cancleGoodsStock();

    boolean commitGoodsStock();
}
