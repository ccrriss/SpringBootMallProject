package com.chrisjia.mall.service.impl;

import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.dao.ProductMapper;
import com.chrisjia.mall.model.pojo.Category;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.query.ProductListQuery;
import com.chrisjia.mall.model.request.AddProductReq;
import com.chrisjia.mall.model.request.GetProductListReq;
import com.chrisjia.mall.model.request.UpdateProductReq;
import com.chrisjia.mall.model.vo.CategoryVO;
import com.chrisjia.mall.service.CategoryService;
import com.chrisjia.mall.service.ProductService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("productService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryService categoryService;

    @Override
    public void postAddProduct(AddProductReq addProductReq) throws ExceptionClass {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        String productName = product.getName();
        Product resultProduct = productMapper.selectByName(productName);
        if(resultProduct != null) {
            throw new ExceptionClass(ExceptionEnum.PRODUCT_HAS_EXIST_EXCEPTION);
        }
        int result = productMapper.insertSelective(product);
        if(result != 1) {
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    public void updateProduct(UpdateProductReq updateProductReq) throws ExceptionClass {
        Product updateProduct = new Product();
        BeanUtils.copyProperties(updateProductReq, updateProduct);
        Product resultProduct = productMapper.selectByName(updateProduct.getName());
        if(resultProduct != null && !resultProduct.getId().equals(updateProduct.getId())){
            throw new ExceptionClass(ExceptionEnum.PRODUCT_HAS_EXIST_EXCEPTION);
        }
        int result = productMapper.updateByPrimaryKeySelective(updateProduct);
        if(result != 1) {
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    public void deleteProduct(Integer productId) throws ExceptionClass {
        Product resultProduct = productMapper.selectByPrimaryKey(productId);
        if(resultProduct == null) {
            throw new ExceptionClass(ExceptionEnum.PRODUCT_NOT_EXIST_EXCEPTION);
        }
        int result = productMapper.deleteByPrimaryKey(productId);
        if(result == 0) {
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public PageInfo<Product> adminGetCategoryList(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Product> productList = productMapper.selectProductList();
        return new PageInfo<>(productList);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public Product getProductDetail(Integer id) throws ExceptionClass {
        Product resultProduct = productMapper.selectByPrimaryKey(id);
        if (resultProduct == null) {
            throw new ExceptionClass(ExceptionEnum.PRODUCT_NOT_EXIST_EXCEPTION);
        }
        return resultProduct;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public PageInfo<Product> getProductList(GetProductListReq getProductListReq) {
        ProductListQuery productListQuery = new ProductListQuery();
        if(!StringUtils.isEmpty(getProductListReq.getKeyword())){
           String keyword = new StringBuilder().append("%").append(getProductListReq.getKeyword()).append("%").toString();
           productListQuery.setKeyword(keyword);
        }
        // search product and sub categories of this product
        if(getProductListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService.getCategoryList(getProductListReq.getCategoryId());
            List<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(getProductListReq.getCategoryId());
            getCategoryIdsRecursive(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        // order by
        String orderby = getProductListReq.getOrderby();
        if(Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderby)) {
            PageHelper.startPage(getProductListReq.getPage(), getProductListReq.getLimit(),
                    orderby);
        } else {
            PageHelper.startPage(getProductListReq.getPage(), getProductListReq.getLimit());
        }

        List<Product> productList = productMapper.selectProductListByQuery(productListQuery);
        return new PageInfo<>(productList);
    }

    private void getCategoryIdsRecursive(List<CategoryVO> categoryVOList, List<Integer> categoryIds){
        if(!CollectionUtils.isEmpty(categoryVOList)) {
            for (CategoryVO categoryVO :
                    categoryVOList) {
                categoryIds.add(categoryVO.getId());
                getCategoryIdsRecursive(categoryVO.getCategoryVOList(), categoryIds);
            }
        }
    }
}
