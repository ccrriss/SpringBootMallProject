package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.request.GetProductListReq;
import com.chrisjia.mall.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping("/{id}")
    public ApiRestResponse getProductDetail(@PathVariable("id") Integer id) throws ExceptionClass {
        Product resultProduct = productService.getProductDetail(id);
        return ApiRestResponse.success(resultProduct);
    }

    @GetMapping
    public ApiRestResponse getProductList(GetProductListReq getProductListReq){
        PageInfo pageInfo = productService.getProductList(getProductListReq);
        return ApiRestResponse.success(pageInfo);
    }
}
