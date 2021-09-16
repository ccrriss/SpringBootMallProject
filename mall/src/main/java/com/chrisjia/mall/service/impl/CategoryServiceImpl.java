package com.chrisjia.mall.service.impl;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.dao.CategoryMapper;
import com.chrisjia.mall.model.pojo.Category;
import com.chrisjia.mall.model.request.AddCategoryReq;
import com.chrisjia.mall.model.request.UpdateCategoryReq;
import com.chrisjia.mall.model.vo.CategoryVO;
import com.chrisjia.mall.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void postAddCategory(AddCategoryReq addCategoryReq) throws ExceptionClass {
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category);
        String categoryName = addCategoryReq.getName();
        Category resultCategory = categoryMapper.selectByName(categoryName);
        if(resultCategory != null){
            throw new ExceptionClass(ExceptionEnum.CATEGORY_FIELD_EXCEPTION);
        }
        int result = categoryMapper.insertSelective(category);
        if(result != 1){
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCategory(UpdateCategoryReq updateCategoryReq) throws ExceptionClass {
        // if all properties are empty except id, it's worthless and will throw an exception
        if(StringUtils.isEmpty(updateCategoryReq.getName()) && StringUtils.isEmpty(updateCategoryReq.getOrderNum()) &&
            StringUtils.isEmpty(updateCategoryReq.getParentId()) && StringUtils.isEmpty(updateCategoryReq.getType())
        ) {
            throw new ExceptionClass(ExceptionEnum.CATEGORY_FIELD_EXCEPTION);
        }

        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        Category resultCategory = null;
        // whether updateCategoryReq has name or not
        if(!StringUtils.isEmpty(updateCategoryReq.getName())) {
            resultCategory = categoryMapper.selectByName(category.getName());
        } else {
            resultCategory = categoryMapper.selectByPrimaryKey(category.getId());
        }
        // resultCategory is empty or id does not match
        if(resultCategory == null) {
            throw new ExceptionClass(ExceptionEnum.CATEGORY_NOT_EXIST_EXCEPTION);
        }
        if(!resultCategory.getId().equals(category.getId())){
            throw new ExceptionClass(ExceptionEnum.CATEGORY_NOT_MATCH_EXCEPTION);
        }
        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if(result != 1) {
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    public void deleteCategory(Integer categoryId) throws ExceptionClass {
        Category resultCategory = categoryMapper.selectByPrimaryKey(categoryId);
        // cannot find the category
        if(resultCategory == null) {
            throw new ExceptionClass(ExceptionEnum.CATEGORY_NOT_EXIST_EXCEPTION);
        }
        int result = categoryMapper.deleteByPrimaryKey(categoryId);
        if(result == 0) {
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    public PageInfo getAdminCategoryList(int page, int limit) {
        PageHelper.startPage(page, limit, "type, order_num");
        List<Category> categoryList = categoryMapper.selectCategoryList();
        return new PageInfo(categoryList);
    }

    @Override
    @Cacheable(value = "getCategoryList")
    public List<CategoryVO> getCategoryList(Integer parentId) {
        List<CategoryVO> categoryVOList = new ArrayList<>();
        getCategoryRecursive(categoryVOList, parentId);
        return categoryVOList;
    }

    private void getCategoryRecursive(List<CategoryVO> categoryVOList, int parentId) {
        // recursively getting all sub categories
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if(!CollectionUtils.isEmpty(categoryList)){
            for (Category category :
                    categoryList) {
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                getCategoryRecursive(categoryVO.getCategoryVOList(), categoryVO.getId());
            }
        }
    }
}
