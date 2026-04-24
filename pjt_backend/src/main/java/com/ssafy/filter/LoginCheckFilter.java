package com.ssafy.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 로그인 여부를 검사하는 인증 필터
 *
 * 적용 범위: /member, /auth (action=logout 제외)
 * - 세션에 loginMember 가 없으면 로그인 페이지로 리다이렉트
 * - AJAX 요청이면 401 JSON 응답
 *
 * 로그인 없이 접근 가능한 URI 는 EXCLUDE_URIS 에 등록합니다.
 */
@WebFilter({"/member", "/auth"})
public class LoginCheckFilter implements Filter {

	/** 로그인 없이 통과시킬 (URI, action) 조합 */
	private static final Set<String> PUBLIC_ACTIONS = new HashSet<>(Arrays.asList(
			"login",         // POST /auth?action=login
			"check",         // GET /auth?action=check
			"logout",        // GET|POST /auth?action=logout
			"findPassword",  // POST /auth?action=findPassword
			"register"       // POST /member?action=register
	));

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest  request  = (HttpServletRequest)  req;
		HttpServletResponse response = (HttpServletResponse) res;

		String action = nullToEmpty(request.getParameter("action"));

		// 공개 액션은 인증 검사 생략
		if (PUBLIC_ACTIONS.contains(action)) {
			chain.doFilter(request, response);
			return;
		}

		// 세션 확인
		HttpSession session = request.getSession(false);
		boolean loggedIn = (session != null && session.getAttribute("loginMember") != null);

		if (loggedIn) {
			chain.doFilter(request, response);
			return;
		}

		// 미인증 처리
		String xhr = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(xhr)) {
			// AJAX 요청 → 401 JSON
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print("{\"result\":\"fail\",\"message\":\"로그인이 필요합니다.\"}");
		} else {
			// 일반 요청 → 로그인 페이지로 리다이렉트 (원래 URL 저장)
			String redirectUrl = request.getRequestURI();
			String query = request.getQueryString();
			if (query != null) redirectUrl += "?" + query;

			request.getSession(true).setAttribute("redirectAfterLogin", redirectUrl);
			response.sendRedirect(request.getContextPath() + "/pages/login.html");
		}
	}

	private String nullToEmpty(String s) {
		return s == null ? "" : s.trim();
	}
}