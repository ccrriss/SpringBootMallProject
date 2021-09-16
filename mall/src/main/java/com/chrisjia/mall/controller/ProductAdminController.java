package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.request.AddProductReq;
import com.chrisjia.mall.model.request.UpdateProductReq;
import com.chrisjia.mall.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/admin/product")
@RestController
public class ProductAdminController {
    @Resource
    private ProductService productService;

    // product
    @PostMapping
    public ApiRestResponse adminAddProduct(@Valid AddProductReq addProductReq) throws ExceptionClass {
        productService.postAddProduct(addProductReq);
        return ApiRestResponse.success();
    }

    @PutMapping
    public ApiRestResponse adminUpdateProduct(@Valid UpdateProductReq updateProductReq) throws ExceptionClass {
        productService.updateProduct(updateProductReq);
        return ApiRestResponse.success();
    }

    @PutMapping("/batchUpdateSellStatus")
    public ApiRestResponse adminBatchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus){
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @DeleteMapping
    public ApiRestResponse adminDeleteProduct(@RequestParam int id) throws ExceptionClass {
        productService.deleteProduct(id);
        return ApiRestResponse.success();
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public ApiRestResponse adminGetCategoryList(@RequestParam int page, @RequestParam int limit) {
        PageInfo<Product> pageInfo = productService.adminGetCategoryList(page, limit);
        return ApiRestResponse.success(pageInfo);
    }
}
