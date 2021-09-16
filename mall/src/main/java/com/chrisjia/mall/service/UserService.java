package com.chrisjia.mall.service;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.pojo.User;

public interface UserService {
    public User getUserById(Integer userId);

    public void postUserRegister(String username, String password) throws ExceptionClass;

    public User postUserLogin(String username, String password) throws ExceptionClass;

    public void postUpdateSignature(User user) throws ExceptionClass;

    boolean checkAdminPermission(User user);
}
