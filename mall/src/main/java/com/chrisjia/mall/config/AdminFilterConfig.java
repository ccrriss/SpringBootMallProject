package com.chrisjia.mall.config;

import com.chrisjia.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminFilterConfig {

    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }

    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(adminFilter());
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
//        filterRegistrationBean.addUrlPatterns("/admin/product");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        filterRegistrationBean.addUrlPatterns("/admin/upload/image");
        filterRegistrationBean.setName("adminFilterConf");
        return filterRegistrationBean;
    }
}
