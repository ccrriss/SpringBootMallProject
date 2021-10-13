package com.chrisjia.mall.common;

import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.pojo.Order;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Constant {
    public static final String SALT = "chrisjia";
    public static final String USER = "CURRENT_USER";

    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price asc", "price desc");
    }

    public interface ProductSellStatus {
        static final int SALE = 1;
        int NOT_SALE = 0;
        int CHECKED = 1;
        int UNCHECKED = 0;
    }

    public interface CartSelectedStatus {
        Set<Integer> SELECTED_STATUS = Sets.newHashSet(1, 0);
        int SELECTED = 1;
        int UN_SELECTED = 0;
    }

    public enum OrderStatus {
        CANCELLED(0, "User has cancelled the order"),
        UN_PAID(10, "User has placed the order but hasn't paid"),
        PAID(11, "User has paid for the order but hasn't shipped yet"),
        TRANSIT(12, "Product is in transit"),
        DELIVERED(13, "Product has been delivered");

        private int statusCode;
        private String status;

        OrderStatus(int statusCode, String status) {
            this.statusCode = statusCode;
            this.status = status;
        }

        public static OrderStatus getStatusFromCode(int statusCode) throws ExceptionClass {
            for(OrderStatus orderStatus: OrderStatus.values()) {
                if(orderStatus.getStatusCode() == statusCode){
                    return orderStatus;
                }
            }
            throw new ExceptionClass(ExceptionEnum.ORDER_STATUS_NOT_EXIST_EXCEPTION);
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
