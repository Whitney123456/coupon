package com.shangan.trade.goos2.service.impl;

import com.shangan.trade.goos2.service.GoodsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsTransactionServiceImpl implements GoodsTransactionService {
    @Override
    public boolean tryGoodsStock() {
        log.info("tryGoodsStock......");
        return true;
    }

    @Override
    public boolean cancleGoodsStock() {
        log.info("cancleGoodsStock......");
        return true;
    }

    @Override
    public boolean commitGoodsStock() {
        log.info("commitGoodsStock......");
        return true;
    }
}
