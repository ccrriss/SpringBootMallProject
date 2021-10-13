package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/order")
public class OrderAdminController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public ApiRestResponse adminGetOrderList(@RequestParam Integer page, @RequestParam Integer limit) throws ExceptionClass {
        PageInfo pageInfo = orderService.adminGetOrderList(page, limit);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping
    public ApiRestResponse adminPostShipping(@RequestParam String orderNo) throws ExceptionClass {
        orderService.adminPostShipping(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/delivered")
    public ApiRestResponse adminPostDelivered(@RequestParam String orderNo) throws ExceptionClass {
        orderService.postDelivered(orderNo);
        return ApiRestResponse.success();
    }
}
