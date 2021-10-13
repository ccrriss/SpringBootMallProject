package com.chrisjia.mall.controller;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.model.request.AddOrderReq;
import com.chrisjia.mall.model.vo.OrderVO;
import com.chrisjia.mall.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping
    public ApiRestResponse addOrder(AddOrderReq addOrderReq) throws ExceptionClass {
        String OrderNo = orderService.addOrder(addOrderReq);
        return ApiRestResponse.success(OrderNo);
    }

    @GetMapping("/{orderNo}")
    public ApiRestResponse getOrderDetail(@PathVariable("orderNo") String orderNo) throws ExceptionClass {
        OrderVO orderVO = orderService.getOrderDetail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping
    public ApiRestResponse getOrderList(@RequestParam Integer page, @RequestParam Integer limit) throws ExceptionClass {
        PageInfo pageInfo = orderService.getOrderList(page, limit);
        return ApiRestResponse.success(pageInfo);
    }

    @DeleteMapping
    public ApiRestResponse deleteOrder(@RequestParam String orderNo) throws ExceptionClass {
        orderService.deleteOrder(orderNo);
        return ApiRestResponse.success();
    }

    @GetMapping("/qrcode")
    public ApiRestResponse getQrCode(@RequestParam String orderNo) {
        String qrCodeAddress = orderService.getQrCode(orderNo);
        return ApiRestResponse.success(qrCodeAddress);
    }

    @PostMapping("/pay")
    public ApiRestResponse pay(@RequestParam String orderNo) throws ExceptionClass {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/delivered")
    public ApiRestResponse postDelivered(@RequestParam String orderNo) throws ExceptionClass {
        orderService.postDelivered(orderNo);
        return ApiRestResponse.success();
    }
}
