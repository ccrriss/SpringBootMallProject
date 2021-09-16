package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.model.request.AddCategoryReq;
import com.chrisjia.mall.model.request.UpdateCategoryReq;
import com.chrisjia.mall.service.CategoryService;
import com.chrisjia.mall.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    @PostMapping("/login")
    public ApiRestResponse adminLogin(String username, String password,HttpSession session) throws ExceptionClass {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return new ApiRestResponse(ExceptionEnum.USER_REGISTER_EXCEPTION.getCode(),
                    ExceptionEnum.USER_REGISTER_EXCEPTION.getMsg());
        }
        User user = userService.postUserLogin(username, password);
        if (userService.checkAdminPermission(user)) {
            user.setPassword(null);
            session.setAttribute(Constant.USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.fail(ExceptionEnum.USER_LOGIN_NO_ADMIN_PERMISSION);
        }
    }

    @PostMapping("/category")
    public ApiRestResponse adminAddCategory(@Valid AddCategoryReq addCategoryReq, HttpSession session) throws ExceptionClass {
        categoryService.postAddCategory(addCategoryReq);
        return ApiRestResponse.success();
    }

    @PutMapping("/category")
    public ApiRestResponse adminUpdateCategory(@Valid UpdateCategoryReq updateCategoryReq, HttpSession session) throws ExceptionClass {
        categoryService.updateCategory(updateCategoryReq);
        return ApiRestResponse.success();
    }

    @DeleteMapping("/category")
    public ApiRestResponse adminDeleteCategory(@RequestParam int id) throws ExceptionClass {
        categoryService.deleteCategory(id);
        return ApiRestResponse.success();
    }

    @GetMapping(value = "/category", produces = "application/json;charset=UTF-8")
    public ApiRestResponse adminGetCategoryList(@RequestParam int page, @RequestParam int limit){
        PageInfo pageInfo = categoryService.getAdminCategoryList(page,limit);
        return ApiRestResponse.success(pageInfo);
    }


    @PostMapping("/upload/image")
    public ApiRestResponse adminUploadImage(HttpServletRequest request, @RequestParam MultipartFile file) throws MalformedURLException {
        String filename = file.getOriginalFilename();
        if(StringUtils.isEmpty(filename)) {
            return ApiRestResponse.fail(ExceptionEnum.FILE_NAME_EMPTY_EXCEPTION);
        }
        String suffix = filename.substring(filename.lastIndexOf("."));
        // generating image UUID
        UUID uuid = UUID.randomUUID();
        String resultFilename = uuid.toString() + suffix;

        // generating file

        /*ServletContext context = request.getServletContext();
        String uploadPath = context.getResource("/").getPath() + "upload/";
        System.out.println(uploadPath);*/

        String uploadPath = Constant.FILE_UPLOAD_DIR;
        System.out.println(uploadPath);
        File fileDir = new File(uploadPath);
        File resultFile = new File(uploadPath + resultFilename);
        if(!fileDir.exists()) {
            if(!fileDir.mkdir()){
                return ApiRestResponse.fail(ExceptionEnum.MKDIR_FAIL_EXCEPTION);
            }
        }
        try {
            file.transferTo(resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // same as toString, convert StringBuffer to String
        String fullFilePath = request.getRequestURL() + "";

        try {
            return ApiRestResponse.success(getHost(new URI(request.getRequestURL().toString())) +
                    "/upload/" + resultFilename);
        } catch (URISyntaxException e) {
            return ApiRestResponse.fail(ExceptionEnum.FILE_UPLOAD_FAIL_EXCEPTION);
        }
    }

    private URI getHost(URI uri){
        URI hostURI = null;
        try {
            hostURI = new URI(
                   uri.getScheme(),uri.getUserInfo(),uri.getHost(), uri.getPort(),null, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return hostURI;
    }
}
