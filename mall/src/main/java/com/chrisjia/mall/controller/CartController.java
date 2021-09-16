package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @PostMapping
    public ApiRestResponse addCart(@RequestParam Integer productId, @RequestParam Integer count){
        return null;
    }
}
