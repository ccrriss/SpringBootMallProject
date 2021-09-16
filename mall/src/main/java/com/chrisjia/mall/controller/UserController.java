package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping
    public User getUserById(@RequestParam("userId") Integer userId){
        return userService.getUserById(userId);
    }

    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("username") String username, @RequestParam("password") String password) throws ExceptionClass {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return new ApiRestResponse(ExceptionEnum.USER_REGISTER_EXCEPTION.getCode(),
                    ExceptionEnum.USER_REGISTER_EXCEPTION.getMsg());
        }
        if(password.length() < 6){
            return new ApiRestResponse(ExceptionEnum.USER_PASSWORD_LENGTH_EXCEPTION.getCode(),
                    ExceptionEnum.USER_PASSWORD_LENGTH_EXCEPTION.getMsg());
        }
        userService.postUserRegister(username, password);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    public ApiRestResponse login(String username, String password, HttpSession session) throws ExceptionClass {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return new ApiRestResponse(ExceptionEnum.USER_REGISTER_EXCEPTION.getCode(),
                    ExceptionEnum.USER_REGISTER_EXCEPTION.getMsg());
        }
        User user = userService.postUserLogin(username, password);
        user.setPassword(null);
        session.setAttribute(Constant.USER, user);
        return ApiRestResponse.success(user);
    }

    @PostMapping("/update")
    public ApiRestResponse updateSignature(String signature, HttpSession session) throws ExceptionClass {
        User user = (User)session.getAttribute(Constant.USER);
        if(user == null){
            return ApiRestResponse.fail(ExceptionEnum.USER_HAS_NOT_LOGIN_EXCEPTION);
        }
        User tempUser = new User();
        tempUser.setId(user.getId());
        tempUser.setPersonalizedSignature(signature);
        userService.postUpdateSignature(tempUser);
        return ApiRestResponse.success();
    }

    @PostMapping("/logout")
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.USER);
        return ApiRestResponse.success();
    }
}
