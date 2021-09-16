package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.model.vo.CategoryVO;
import com.chrisjia.mall.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping
    public ApiRestResponse getCategoryList(){
        List<CategoryVO> categoryVOList = categoryService.getCategoryList(0);
        return ApiRestResponse.success(categoryVOList);
    }
}
