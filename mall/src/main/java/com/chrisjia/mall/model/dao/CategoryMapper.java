package com.chrisjia.mall.model.dao;

import com.chrisjia.mall.model.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    public Category selectByName(String name);

    public List<Category> selectCategoryList();

    public List<Category> selectCategoriesByParentId(int parentId);
}