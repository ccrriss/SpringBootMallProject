package com.chrisjia.mall.service.impl;

import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.filter.UserFilter;
import com.chrisjia.mall.model.dao.CartMapper;
import com.chrisjia.mall.model.dao.OrderItemMapper;
import com.chrisjia.mall.model.dao.OrderMapper;
import com.chrisjia.mall.model.dao.ProductMapper;
import com.chrisjia.mall.model.pojo.Order;
import com.chrisjia.mall.model.pojo.OrderItem;
import com.chrisjia.mall.model.pojo.Product;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.model.request.AddOrderReq;
import com.chrisjia.mall.model.vo.CartVO;
import com.chrisjia.mall.model.vo.OrderItemVO;
import com.chrisjia.mall.model.vo.OrderVO;
import com.chrisjia.mall.service.CartService;
import com.chrisjia.mall.service.OrderService;
import com.chrisjia.mall.service.UserService;
import com.chrisjia.mall.util.OrderNumberUtils;
import com.chrisjia.mall.util.QrCodeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("orderService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {
    @Resource
    private CartService cartService;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private UserService userService;

    @Value("${file.upload.ip}")
    private String ip;

    @Override
    public OrderVO getOrderDetail(String orderNo) throws ExceptionClass {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null) {
            throw new ExceptionClass(ExceptionEnum.ORDER_NOT_EXIST_EXCEPTION);
        }
        User currentUser = UserFilter.currentUser;
        Integer userId = currentUser.getId();
        if(!order.getUserId().equals(userId)) {
            throw new ExceptionClass(ExceptionEnum.ORDER_USER_ID_NOT_MATCH_EXCEPTION);
        }
        // set status and orderItemVOList in orderVO
        OrderVO orderVO = convertOrderToOrderVO(order);
        // finished
        return orderVO;
    }


    private OrderVO convertOrderToOrderVO(Order order) throws ExceptionClass {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        Constant.OrderStatus orderStatus = Constant.OrderStatus.getStatusFromCode(order.getOrderStatus());
        String status = orderStatus.getStatus();
        orderVO.setStatus(status);

        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemListByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem :
                orderItemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    @Override
    public PageInfo getOrderList(Integer page, Integer limit) throws ExceptionClass {
        User currentUser = UserFilter.currentUser;
        Integer userId = currentUser.getId();
        List<Order> orderList = orderMapper.selectOrderListByUserId(userId);
        if(CollectionUtils.isEmpty(orderList)) {
            throw new ExceptionClass(ExceptionEnum.ORDER_LIST_NOT_EXIST_EXCEPTION);
        }
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order :
                orderList) {
            OrderVO orderVO = convertOrderToOrderVO(order);
            orderVOList.add(orderVO);
        }
        PageHelper.startPage(page, limit);
        return new PageInfo<>(orderVOList);
    }

    @Override
    public PageInfo adminGetOrderList(Integer page, Integer limit) throws ExceptionClass {
        PageHelper.startPage(page, limit);
        List<Order> orderList = orderMapper.selectOrderList();
        if(CollectionUtils.isEmpty(orderList)) {
            throw new ExceptionClass(ExceptionEnum.ORDER_LIST_NOT_EXIST_EXCEPTION);
        }
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order :
                orderList) {
            OrderVO orderVO = convertOrderToOrderVO(order);
            orderVOList.add(orderVO);
        }
        return new PageInfo(orderVOList);
    }
    @Override
    public void deleteOrder(String orderNo) throws ExceptionClass {
        User currentUser = UserFilter.currentUser;
        Integer userId = currentUser.getId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null) {
            throw new ExceptionClass(ExceptionEnum.ORDER_NOT_EXIST_EXCEPTION);
        }
        if(!order.getUserId().equals(userId)) {
            throw new ExceptionClass(ExceptionEnum.ORDER_USER_ID_NOT_MATCH_EXCEPTION);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatus.UN_PAID.getStatusCode())) {
            order.setEndTime(new Date());
            order.setOrderStatus(Constant.OrderStatus.CANCELLED.getStatusCode());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new ExceptionClass(ExceptionEnum.ORDER_STATUS_CANNOT_BE_CANCELLED_EXCEPTION);
        }
    }

    @Override
    public String getQrCode(String orderNo) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        int port = request.getLocalPort();

        String payUrl = "http://" + ip + ":" + port + "/order/pay?orderNo=" + orderNo;
        try {
            QrCodeUtils.generateQrCode(payUrl, 300, 300, Constant.FILE_UPLOAD_DIR + orderNo + ".png");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        String qrCodeAddress = "http://" + ip + ":" + port + "/upload/" + orderNo + ".png";
        return qrCodeAddress;
    }

    @Override
    public String addOrder(AddOrderReq addOrderReq) throws ExceptionClass {
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.getCartList(userId);
        List<CartVO> validCartVOList = new ArrayList<>();
        for (CartVO cartVO :
                cartVOList) {
            if (cartVO.getSelected().equals(Constant.CartSelectedStatus.SELECTED)) {
                validCartVOList.add(cartVO);
            }
        }
        if(CollectionUtils.isEmpty(validCartVOList)) {
            throw new ExceptionClass(ExceptionEnum.CART_SELECTED_STATUS_WRONG_EXCEPTION);
        }
        // check if product in cart is valid
        validateProductForCartList(cartVOList);
        // convert cartVOList to OrderItemList
        List<OrderItem> orderItemList = convertCartVOListToOrderItemList(validCartVOList);
        // reduce the stock of each product in the list
        reduceStockForProducts(orderItemList);
        // remove selected items in the cart since their price has been calculated in order
        for (CartVO cartVO :
                validCartVOList) {
            cartVOList.remove(cartVO);
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
        // generate order
        Order order = new Order();
        String orderNo = OrderNumberUtils.generateOrderNum(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(calculateTotalPrice(orderItemList));
        order.setReceiverName(addOrderReq.getReceiverName());
        order.setReceiverAddress(addOrderReq.getReceiverAddress());
        order.setReceiverMobile(addOrderReq.getReceiverMobile());
        order.setOrderStatus(Constant.OrderStatus.UN_PAID.getStatusCode());
        order.setPostage(0);
        order.setPaymentType(2);
        orderMapper.insertSelective(order);
        // save orderItem
        for (OrderItem orderItem :
                orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        return orderNo;
    }
    private void validateProductForCartList(List<CartVO> cartVOList) throws ExceptionClass {
        for (CartVO cartVO :
                cartVOList) {
            Integer productId = cartVO.getProductId();
            Product resultProduct = productMapper.selectByPrimaryKey(productId);
            if(resultProduct == null) {
                throw new ExceptionClass(ExceptionEnum.PRODUCT_NOT_EXIST_EXCEPTION);
            }
            if(resultProduct.getStatus().equals(Constant.ProductSellStatus.NOT_SALE)) {
                throw new ExceptionClass(ExceptionEnum.PRODUCT_NOT_SELL_EXCEPTION);
            }
            Integer count = cartVO.getQuantity();
            if(count > resultProduct.getStock()) {
                throw new ExceptionClass(ExceptionEnum.PRODUCT_STOCK_INSUFFICIENT_EXCEPTION);
            }
            if(count < 0) {
                throw new ExceptionClass(ExceptionEnum.CART_NEGATIVE_COUNT_EXCEPTION);
            }
        }
    }
    private List<OrderItem> convertCartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();

        for (CartVO cartVO :
                cartVOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItem.setCreateTime(cartVO.getCreateTime());
            orderItem.setUpdateTime(cartVO.getUpdateTime());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }
    private void reduceStockForProducts(List<OrderItem> orderItemList) throws ExceptionClass {
        for (OrderItem orderItem :
                orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            Integer count = orderItem.getQuantity();
            if(count > product.getStock()) {
                throw new ExceptionClass(ExceptionEnum.PRODUCT_STOCK_INSUFFICIENT_EXCEPTION);
            }
            product.setStock(product.getStock() - count);
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    private Integer calculateTotalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (OrderItem orderItem :
                orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    @Override
    public void pay(String orderNo) throws ExceptionClass {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null) {
            throw new ExceptionClass(ExceptionEnum.ORDER_NOT_EXIST_EXCEPTION);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatus.UN_PAID.getStatusCode())) {
            order.setOrderStatus(Constant.OrderStatus.PAID.getStatusCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new ExceptionClass(ExceptionEnum.ORDER_STATUS_WRONG_EXCEPTION);
        }
    }

    @Override
    public void adminPostShipping(String orderNo) throws ExceptionClass {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null) {
            throw new ExceptionClass(ExceptionEnum.ORDER_NOT_EXIST_EXCEPTION);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatus.PAID.getStatusCode())) {
            order.setOrderStatus(Constant.OrderStatus.TRANSIT.getStatusCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new ExceptionClass(ExceptionEnum.ORDER_STATUS_WRONG_EXCEPTION);
        }
    }

    @Override
    public void postDelivered(String orderNo) throws ExceptionClass {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null) {
            throw new ExceptionClass(ExceptionEnum.ORDER_NOT_EXIST_EXCEPTION);
        }
        if(!order.getOrderStatus().equals(Constant.OrderStatus.TRANSIT.getStatusCode())) {
            throw new ExceptionClass(ExceptionEnum.ORDER_STATUS_WRONG_EXCEPTION);
        }
        Integer userId = order.getUserId();
        if(!userService.checkAdminPermission(UserFilter.currentUser) && !userId.equals(UserFilter.currentUser.getId())) {
            throw new ExceptionClass(ExceptionEnum.ORDER_USER_ID_NOT_MATCH_EXCEPTION);
        } else {
            order.setOrderStatus(Constant.OrderStatus.DELIVERED.getStatusCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
    }
}
