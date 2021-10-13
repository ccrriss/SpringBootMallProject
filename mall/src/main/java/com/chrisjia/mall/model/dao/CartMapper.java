package com.chrisjia.mall.model.dao;

import com.chrisjia.mall.model.pojo.Cart;
import com.chrisjia.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<CartVO> selectCartListByUserId(Integer userId);

    public void updateSelectedStatus(@Param("userId") Integer userId, @Param("productId") Integer productId,
                                     @Param("selected") Integer selected);
}