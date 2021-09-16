package com.chrisjia.mall.model.dao;

import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    public Product selectByName(String name);

    public int batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("sellStatus") Integer sellStatus);

    public List<Product> selectProductList();

    public List<Product> selectProductListByQuery(@Param("query") ProductListQuery query);
}