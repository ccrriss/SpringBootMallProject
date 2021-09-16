package com.chrisjia.mall.service.impl;

import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.dao.CartMapper;
import com.chrisjia.mall.model.dao.ProductMapper;
import com.chrisjia.mall.model.pojo.Cart;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.vo.CartVO;
import com.chrisjia.mall.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("CartService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CartServiceImpl implements CartService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CartMapper cartMapper;

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
            cart.getQuantity() + count;
        }
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
}
