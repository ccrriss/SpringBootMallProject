package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.filter.UserFilter;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.model.vo.CartVO;
import com.chrisjia.mall.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @PostMapping
    public ApiRestResponse addCart(@RequestParam Integer productId, @RequestParam Integer count) throws ExceptionClass {
        List<CartVO> cartVOList = cartService.addCart(getCurrentUser().getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @GetMapping
    public ApiRestResponse getCartList() throws ExceptionClass {
        List<CartVO> cartVOList = cartService.getCartList(getCurrentUser().getId());
        return ApiRestResponse.success(cartVOList);
    }

    @PutMapping
    public ApiRestResponse updateCart(@RequestParam Integer productId, @RequestParam Integer count) throws ExceptionClass {
        List<CartVO> cartVOList = cartService.updateCart(getCurrentUser().getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @DeleteMapping
    public ApiRestResponse deleteCart(@RequestParam Integer productId) throws ExceptionClass {
        List<CartVO> cartVOList = cartService.deleteCart(getCurrentUser().getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    private User getCurrentUser(){
        return UserFilter.currentUser;
    }

    @PutMapping("/select/{productId}")
    public ApiRestResponse updateSelected(@PathVariable("productId") Integer productId, @RequestParam Integer selected) throws ExceptionClass {
        List<CartVO> cartVOList = cartService.updateSelectedStatus(getCurrentUser().getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    @PutMapping("/select")
    public ApiRestResponse updateSelectedAll(@RequestParam Integer selected) throws ExceptionClass {
        List<CartVO> cartVOList = cartService.updateSelectedStatusAll(getCurrentUser().getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}
