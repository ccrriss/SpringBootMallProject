package com.chrisjia.mall.service.impl;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.dao.UserMapper;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.service.UserService;
import com.chrisjia.mall.util.Md5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

@Service("UserService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void postUserRegister(String username, String password) throws ExceptionClass {
        User user = userMapper.selectByUsername(username);
        if(user != null) {
            throw new ExceptionClass(ExceptionEnum.USER_HAS_EXIST_EXCEPTION.getCode(),
                    ExceptionEnum.USER_HAS_EXIST_EXCEPTION.getMsg());
        }
        user = new User();
        user.setUsername(username);
        try {
            user.setPassword(Md5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int result = userMapper.insertSelective(user);
        if(result != 1){
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION.getCode(),
                    ExceptionEnum.INTERNAL_EXCEPTION.getMsg());
        }
    }

    @Override
    public User postUserLogin(String username, String password) throws ExceptionClass {
        String md5Password = null;
        try {
            md5Password = Md5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = userMapper.selectByUsernameAndPassword(username, md5Password);
        if(user == null) {
            throw new ExceptionClass(ExceptionEnum.USER_LOGIN_EXCEPTION);
        }
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void postUpdateSignature(User user) throws ExceptionClass {
        int result = userMapper.updateByPrimaryKeySelective(user);
        if(result > 1){
            throw new ExceptionClass(ExceptionEnum.INTERNAL_EXCEPTION);
        }
    }

    @Override
    public boolean checkAdminPermission(User user){
        return user.getRole().equals(2);
    }
}
