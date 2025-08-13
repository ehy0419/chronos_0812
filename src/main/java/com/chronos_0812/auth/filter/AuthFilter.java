package com.chronos_0812.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

/**
 * 로그인 필요한 요청만 허가?하는 필터
 */

/**
 [Servlet Filter 란?]
 - Servlet Filter는 보안, 로깅, 인코딩, 인증/인가 등 다양한 작업을 처리하기 위해 사용된다.

 [Servlet Filter 특징]
 1. 공통 관심사 로직 처리
    - 공통된 로직을 중앙 집중적으로 구현하여 재사용성이 높고 유지보수가 쉽다.
    - 모든 요청이 하나의 입구를 통해 처리되어 일관성을 유지한다.
 2. HTTP 요청 및 응답 필터링
 3. Filter Chain
    - 여러 개의 필터가 순차적으로 적용될 수 있다.
    - filterChain.doFilter(request, response); 다음 필터로 제어를 전달한다.
        - Filter는 Chain 형식으로 구성된다.
        - Filter는 개발자가 자유롭게 추가할 수 있다.
        - Filter는 순서를 지정하여 추가할 수 있다.
 4. doFilter()
    - 실제 필터링 작업을 수행하는 주요 메소드로 필터가 처리할 작업을 정의한다.
    - 다음 필터로 제어를 넘길지 여부를 결정한다.

[Servlet Filter 적용]
 - 요청 -> Filter -> Servlet -> Controller
 - Filter를 적용하면 Servlet이 호출되기 이전에 Filter를 항상 거치게된다.
 - 공통 관심사를 필터에만 적용하면 모든 요청 or 응답에 적용된다 .
 - Filter는 특정 URL Pattern에 적용할 수 있다.
 - Spring을 사용하는 경우 Servlet은 Dispatcher Servlet이다.

 [Filter Interface에 대해서 알아보기]
 - Java Servlet에서 HTTP 요청과 응답을 가로채고, 이를 기반으로 다양한 처리 작업을 수행하는 데 사용되는 Interface이다.
 * jakarta.servlet.Filter
 - Filter Interface를 Implements하여 구현하고 Bean으로 등록하여 사용한다.
    - Servlet Container가 Filter를 Singleton 객체로 생성 및 관리한다.

 [Filter의 메서드 기능]
 1. init()
 - Filter를 초기화하는 메서드이다.
 - Servlet Container가 생성될 때 호출된다.
 - default method이기 때문에 implements 후 구현하지 않아도 된다.

 2. doFilter()
 - Client에서 요청이 올 때 마다 doFilter() 메서드가 호출된다.
 - doFilter() 내부에 필터 로직(공통 관심사 로직)을 구현하면 된다.
 - WAS에서 doFilter() 를 호출해주고 하나의 필터의 doFilter()가 통과된다면
 - Filter  Chain에 따라서 순서대로 doFilter() 를 호출한다.
 - 더이상 doFilter() 를 호출할 Filter가 없으면 Servlet이 호출된다.

 3. destroy()
 - 필터를 종료하는 메서드이다.
 - Servlet Container가 종료될 때 호출된다.
 - default method이기 때문에 implements 후 구현하지 않아도 된다.

 */

///  Filter 구현체
// 요청 URL을 Log로 출력하는 Filter
@Slf4j                                              // 로그 작성 어노테이션
public class AuthFilter implements Filter {
    // 인증을 하지 않아도될 URL Path 배열
    private static final String[] WHITE_LIST = {"/", "/signup", "/login"};

    @Override
    public void doFilter(                           // 실제 필터링 작업을 수행하는 주요 메소드로 필터가 처리할 작업을 정의
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        // Filter에서 수행할 Logic
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ///  ServletRequest 는 기능이 별로 없어서 대부분 기능이 많은 HttpServletRequest 를 다운 캐스팅 하여 사용한다. ///
        String requestURI = httpServletRequest.getRequestURI();

        log.info("request URI={}", requestURI);
        // chain 이 없으면 Servlet을 바로 호출
        // == doFilter() 는 더 이상 호출할 Filter가 없다면 Servlet을 호출한다.
        filterChain.doFilter(servletRequest, servletResponse);

        if (!isWhiteList(requestURI)) {
            HttpSession session = httpServletRequest.getSession(false);

            // 로그인하지 않은 사용자인 경우
            if (session == null || session.getAttribute("LOGIN_DIRECTOR") == null) {
                throw new RuntimeException("로그인 해주세요.");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
