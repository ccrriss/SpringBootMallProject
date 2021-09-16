package com.chrisjia.mall.service;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.request.AddCategoryReq;
import com.chrisjia.mall.model.request.UpdateCategoryReq;
import com.chrisjia.mall.model.vo.CategoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {
    public void postAddCategory(AddCategoryReq addCategoryReq) throws ExceptionClass;

    public void updateCategory(UpdateCategoryReq updateCategoryReq) throws ExceptionClass;

    public void deleteCategory(Integer categoryId) throws ExceptionClass;

    public PageInfo getAdminCategoryList(int page, int limit);

    public List<CategoryVO> getCategoryList(Integer parentId);
}
