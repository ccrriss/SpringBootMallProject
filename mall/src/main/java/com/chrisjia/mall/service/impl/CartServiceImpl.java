package com.chrisjia.mall.service.impl;

import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.filter.AdminFilter;
import com.chrisjia.mall.filter.UserFilter;
import com.chrisjia.mall.model.dao.CartMapper;
import com.chrisjia.mall.model.dao.ProductMapper;
import com.chrisjia.mall.model.pojo.Cart;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.model.vo.CartVO;
import com.chrisjia.mall.service.CartService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.security.auth.login.Configuration;
import java.util.List;

@Service("CartService")
@Transactional(propagation = Propagation.REQUIRED)
public class CartServiceImpl implements CartService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CartMapper cartMapper;

    @Override
    public List<CartVO> addCart(Integer userId, Integer productId, Integer count) throws ExceptionClass {
        validateProduct(productId, count);

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if(cart == null) {
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.ProductSellStatus.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            // all keeps same but add the count
            Cart resultCart = new Cart();
            BeanUtils.copyProperties(cart, resultCart);
            resultCart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(resultCart);
        }
        return getCartList(userId);
    }

    @Override
    public List<CartVO> updateCart(Integer userId, Integer productId, Integer count) throws ExceptionClass {
        validateProduct(productId, count);

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if(cart == null) {
            throw new ExceptionClass(ExceptionEnum.CART_NOT_EXIST_EXCEPTION);
        } else {
            Cart resultCart = new Cart();
            BeanUtils.copyProperties(cart, resultCart);
            resultCart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(resultCart);
        }
        return getCartList(userId);
    }

    private void validateProduct(Integer productId, Integer count) throws ExceptionClass {
        Product resultProduct = productMapper.selectByPrimaryKey(productId);
        if(resultProduct == null) {
            throw new ExceptionClass(ExceptionEnum.PRODUCT_NOT_EXIST_EXCEPTION);
        }
        if(resultProduct.getStatus().equals(Constant.ProductSellStatus.NOT_SALE)) {
            throw new ExceptionClass(ExceptionEnum.PRODUCT_NOT_SELL_EXCEPTION);
        }
        if(count > resultProduct.getStock()) {
            throw new ExceptionClass(ExceptionEnum.PRODUCT_STOCK_INSUFFICIENT_EXCEPTION);
        }
        if(count < 0) {
            throw new ExceptionClass(ExceptionEnum.CART_NEGATIVE_COUNT_EXCEPTION);
        }
    }

    @Override
    public List<CartVO> getCartList(Integer userId) throws ExceptionClass {
        List<CartVO> cartVOList = cartMapper.selectCartListByUserId(userId);
        if(CollectionUtils.isEmpty(cartVOList)) {
            throw new ExceptionClass(ExceptionEnum.CART_LIST_NOT_EXIST_EXCEPTION);
        }
        for (CartVO cartVO:
             cartVOList) {
            int price = cartVO.getQuantity() * cartVO.getPrice();
            cartVO.setTotalPrice(price);
        }
        return cartVOList;
    }

    @Override
    public List<CartVO> deleteCart(Integer userId, Integer productId) throws ExceptionClass {
        Cart resultCart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if(resultCart == null) {
            throw new ExceptionClass(ExceptionEnum.CART_NOT_EXIST_EXCEPTION);
        }
        cartMapper.deleteByPrimaryKey(resultCart.getId());
        return getCartList(userId);
    }

    @Override
    public List<CartVO> updateSelectedStatus(Integer userId, Integer productId, Integer selected) throws ExceptionClass {
        Cart resultCart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if(resultCart == null) {
            throw new ExceptionClass(ExceptionEnum.CART_NOT_EXIST_EXCEPTION);
        }
        // true if has been selected, false if hasn't been selected
        if(checkSelectedStatus(selected)) {
            cartMapper.updateSelectedStatus(userId, productId, selected);
        }
        return getCartList(userId);
    }

    @Override
    public List<CartVO> updateSelectedStatusAll(Integer userId, Integer selected) throws ExceptionClass {
        List<CartVO> cartVOList = cartMapper.selectCartListByUserId(userId);
        if(CollectionUtils.isEmpty(cartVOList)) {
            throw new ExceptionClass(ExceptionEnum.CART_LIST_NOT_EXIST_EXCEPTION);
        }
        if(checkSelectedStatus(selected)) {
            cartMapper.updateSelectedStatus(userId, null, selected);
        }
        return getCartList(userId);
    }

    private boolean checkSelectedStatus(Integer selectedStatus) throws ExceptionClass {
        if(Constant.CartSelectedStatus.SELECTED_STATUS.contains(selectedStatus)) {
            return true;
        }else {
            throw new ExceptionClass(ExceptionEnum.CART_SELECTED_STATUS_WRONG_EXCEPTION);
        }
    }
}
