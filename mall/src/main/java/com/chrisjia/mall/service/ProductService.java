package com.chrisjia.mall.service;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.request.AddProductReq;
import com.chrisjia.mall.model.request.GetProductListReq;
import com.chrisjia.mall.model.request.UpdateProductReq;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {
    public void postAddProduct(AddProductReq addProductReq) throws ExceptionClass;

    public void updateProduct(UpdateProductReq updateProductReq) throws ExceptionClass;

    public void deleteProduct(Integer productId) throws ExceptionClass;

    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    public PageInfo<Product> adminGetCategoryList(Integer page, Integer limit);

    public Product getProductDetail(Integer id) throws ExceptionClass;

    public PageInfo<Product> getProductList(GetProductListReq getProductListReq);
}
