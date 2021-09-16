package com.chrisjia.mall.model.dao;

import com.chrisjia.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    public User selectByUsername(String username);

    public User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}