package com.chrisjia.mall.service;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.request.AddOrderReq;
import com.chrisjia.mall.model.vo.OrderVO;
import com.github.pagehelper.PageInfo;

public interface OrderService {
    public String addOrder(AddOrderReq addOrderReq) throws ExceptionClass;

    public OrderVO getOrderDetail(String orderNo) throws ExceptionClass;

    public PageInfo getOrderList(Integer page, Integer limit) throws ExceptionClass;

    public void deleteOrder(String orderNo) throws ExceptionClass;

    public String getQrCode(String orderNo);

    public PageInfo adminGetOrderList(Integer page, Integer limit) throws ExceptionClass;

    public void pay(String orderNo) throws ExceptionClass;

    public void adminPostShipping(String orderNo) throws ExceptionClass;

    public void postDelivered(String orderNo) throws ExceptionClass;
}
