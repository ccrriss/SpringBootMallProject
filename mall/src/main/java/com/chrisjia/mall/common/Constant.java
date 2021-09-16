package com.chrisjia.mall.common;

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
}
