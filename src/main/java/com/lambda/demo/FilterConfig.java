package com.lambda.demo;

import com.lambda.demo.Service.FileService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AccessControlFilter> accessControlFilter(FileService fileService) {
        FilterRegistrationBean<AccessControlFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AccessControlFilter(fileService));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}