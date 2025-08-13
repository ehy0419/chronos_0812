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

///  Filter 등록

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        /// Filter 등록
        filterRegistrationBean.setFilter(new AuthFilter());
        //- `setFilter()`
        //    - 등록할 필터를 파라미터로 전달하면 된다.

        /// Filter 순서 설정
        filterRegistrationBean.setOrder(1);
        //- `setOrder()`
        //    - Filter는 Chain 형태로 동작한다.
        //    - 즉, 실행될 Filter들의 순서가 필요하다.
        //    - 파라미터로 전달될 숫자에 따라 우선순위가 정해진다.
        //    - 숫자가 낮을수록 우선순위가 높다.

        /// 전체 URL에 Filter 적용
        filterRegistrationBean.addUrlPatterns("/*");
        //- `addUrlPatterns()`
        //    - 필터를 적용할 URL 패턴을 지정한다.
        //    - 여러개 URL 패턴을 한번에 지정할 수 있다.
        //    - 규칙은 Servlet URL Pattern과 같다.

        //- `filterRegistrationBean.addUrlPatterns("/*")`
        //    - 모든 Request는 Custom Filter를 항상 지나간다.

        return filterRegistrationBean;
    }
}
