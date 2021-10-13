package com.chrisjia.mall.exception;

public enum ExceptionEnum {
    USER_LOGIN_EXCEPTION("40001", "User does not exist or password is wrong"),
    VERIFICATION_EXCEPTION("40002", "verification code wrong"),
    USER_REGISTER_EXCEPTION("40003", "User name or password cannot be empty"),
    USER_PASSWORD_LENGTH_EXCEPTION("40004", "Password's length is not legal"),
    USER_HAS_EXIST_EXCEPTION("40005", "Username has existed"),
    USER_HAS_NOT_LOGIN_EXCEPTION("40010", "User has not login yet"),
    USER_LOGIN_NO_ADMIN_PERMISSION("40011", "User has no admin permission"),

    CATEGORY_FIELD_EXCEPTION("41001", "Category field is missing"),
    CATEGORY_HAS_EXIST_EXCEPTION("41002", "Category has existed"),
    CATEGORY_ARGUMENT_NOT_VALID_EXCEPTION("41003", "Category argument is not valid"),
    CATEGORY_NOT_MATCH_EXCEPTION("41004", "Category field is not matching"),
    CATEGORY_NOT_EXIST_EXCEPTION("41005", "Category does not exist"),

    PRODUCT_HAS_EXIST_EXCEPTION("42002", "Product has existed"),
    PRODUCT_NOT_EXIST_EXCEPTION("42005", "Product does not exist"),
    PRODUCT_NOT_SELL_EXCEPTION("42010", "Product is not to sell"),
    PRODUCT_STOCK_INSUFFICIENT_EXCEPTION("42011", "Product stock is insufficient"),

    CART_NOT_EXIST_EXCEPTION("43005", "Cart does not exist"),
    CART_NEGATIVE_COUNT_EXCEPTION("43008", "Cart count is under 0"),
    CART_LIST_NOT_EXIST_EXCEPTION("43006", "Cart list do not exist"),
    CART_SELECTED_STATUS_WRONG_EXCEPTION("43003", "Cart selected field is not matching"),

    ORDER_STATUS_NOT_EXIST_EXCEPTION("44002", "Order status does not exist"),
    ORDER_STATUS_WRONG_EXCEPTION("44003", "Order status is not the expected status"),
    ORDER_LIST_NOT_EXIST_EXCEPTION("44006", "Order list do not exist"),
    ORDER_NOT_EXIST_EXCEPTION("44005", "Order does not exist"),
    ORDER_USER_ID_NOT_MATCH_EXCEPTION("44010", "Order's userId does not match"),
    ORDER_STATUS_CANNOT_BE_CANCELLED_EXCEPTION("44011", "Order's current status cannot be cancelled"),

    FILE_NAME_EMPTY_EXCEPTION("45001", "Filename is empty"),
    MKDIR_FAIL_EXCEPTION("45002", "mkdir failed"),
    FILE_UPLOAD_FAIL_EXCEPTION("45003", "File upload failed"),


    INTERNAL_EXCEPTION("50000", "Server internal wrong");

    private String code;
    private String msg;

    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{\n"
                + "    \"code\": " + code + ",\n"
                + "    \"msg\": \"" + msg + "\",\n"
                + "    \"data\": null\n"
                + "}";
    }
}
