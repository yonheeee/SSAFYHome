package com.ssafy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 컨트롤러 공통 유틸리티 인터페이스
 *
 * JSON 응답 전송, 페이지 리다이렉트 등
 * 공통 로직을 default 메서드로 제공합니다.
 */
public interface ControllerHelper {

	// ─── 응답 전송 ────────────────────────────────────────────────────────────

	/**
	 * Content-Type: application/json; charset=UTF-8 으로 문자열 응답 전송
	 */
	default void sendJson(HttpServletResponse response, String json) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(json);
			out.flush();
		}
	}

	/**
	 * 간단한 성공/실패 JSON 응답 생성
	 *
	 * @param success true → {"result":"success"}, false → {"result":"fail","message":"..."}
	 */
	default String buildResult(boolean success, String message) {
		if (success) {
			return "{\"result\":\"success\"}";
		}
		String safeMsg = (message == null ? "오류가 발생했습니다." : message)
				.replace("\"", "\\\"");
		return "{\"result\":\"fail\",\"message\":\"" + safeMsg + "\"}";
	}

	/**
	 * 클라이언트를 지정 경로로 리다이렉트
	 */
	default void redirect(HttpServletResponse response, String path) throws IOException {
		response.sendRedirect(path);
	}

	// ─── 파라미터 헬퍼 ────────────────────────────────────────────────────────

	/**
	 * request 파라미터를 읽어 trim 처리 후 반환 (없으면 빈 문자열)
	 */
	default String param(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		return (value == null) ? "" : value.trim();
	}
}