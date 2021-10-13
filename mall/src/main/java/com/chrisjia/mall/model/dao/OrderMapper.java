package com.chrisjia.mall.model.dao;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    public Order selectByOrderNo(String orderNo);

    public List<Order> selectOrderListByUserId(Integer userId);

    public List<Order> selectOrderList();
}