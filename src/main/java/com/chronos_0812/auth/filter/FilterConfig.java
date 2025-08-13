package com.chronos_0812.auth.filter;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration 을 활용해 커스텀 필터 등록
 * - order: 번호가 낮을수록 먼저 실행
 * - urlPatterns: 기본은 /* 로 전체 API
 */

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthFilter()); // Filter 등록
        filterRegistrationBean.addUrlPatterns("/*"); // 전체 URL에 Filter 적용
//        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
