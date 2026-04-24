package com.ssafy.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * 모든 HTTP 요청의 처리 시간·정보를 로깅하는 필터
 *
 * 적용 범위: /*
 * 출력 예) [2026-04-24 12:00:00] GET /member?action=info | user=hong | 23ms
 */
@WebFilter("/*")
public class LoggingFilter implements Filter {

	private static final Logger log = Logger.getLogger(LoggingFilter.class.getName());
	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		long start = System.currentTimeMillis();

		try {
			chain.doFilter(req, res);
		} finally {
			long elapsed = System.currentTimeMillis() - start;

			String method  = request.getMethod();
			String uri     = request.getRequestURI();
			String query   = request.getQueryString();
			String fullUri = query != null ? uri + "?" + query : uri;

			// 로그인 회원 아이디 (없으면 "anonymous")
			String userId = "anonymous";
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute("loginMember") != null) {
				Object member = session.getAttribute("loginMember");
				// 리플렉션 없이 toString 활용
				userId = extractId(member.toString());
			}

			String msg = String.format("[%s] %s %s | user=%s | %dms",
					LocalDateTime.now().format(FMT),
					method, fullUri, userId, elapsed);

			log.info(msg);
		}
	}

	/** Member.toString() 에서 id= 이후 값 추출 */
	private String extractId(String memberStr) {
		int idx = memberStr.indexOf("id=");
		if (idx == -1) return "unknown";
		int end = memberStr.indexOf(",", idx);
		return end == -1
				? memberStr.substring(idx + 3)
				: memberStr.substring(idx + 3, end);
	}
}