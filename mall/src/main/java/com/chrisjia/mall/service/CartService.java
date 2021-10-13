package com.chrisjia.mall.service;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.vo.CartVO;

import java.util.List;

public interface CartService {
    public List<CartVO> addCart(Integer userId, Integer productId, Integer count) throws ExceptionClass;

    public List<CartVO> updateCart(Integer userId, Integer productId, Integer count) throws ExceptionClass;

    public List<CartVO> getCartList(Integer userId) throws ExceptionClass;

    public List<CartVO> deleteCart(Integer userId, Integer productId) throws ExceptionClass;

    public List<CartVO> updateSelectedStatus(Integer userId, Integer productId, Integer selected) throws ExceptionClass;

    public List<CartVO> updateSelectedStatusAll(Integer userId, Integer selected) throws ExceptionClass;
}
