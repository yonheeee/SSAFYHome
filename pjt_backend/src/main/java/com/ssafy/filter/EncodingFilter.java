package com.ssafy.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

/**
 * 모든 요청/응답에 UTF-8 인코딩을 강제 적용하는 필터
 *
 * 적용 범위: /*  (전체 요청)
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

	private String encoding = "UTF-8";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// web.xml 에서 <init-param> 으로 인코딩 재정의 가능
		String enc = filterConfig.getInitParameter("encoding");
		if (enc != null && !enc.isBlank()) {
			encoding = enc;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 이미 인코딩이 설정된 경우 중복 설정 방지
		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding(encoding);
		}
		response.setCharacterEncoding(encoding);

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// 정리할 자원 없음
	}
}