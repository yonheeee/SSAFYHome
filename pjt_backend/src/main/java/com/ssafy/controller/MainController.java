package com.ssafy.controller;

import com.ssafy.dto.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * 메인 페이지 컨트롤러
 *
 * URL 패턴: /main
 *
 * ┌──────────┬────────────────────────────────────────────────┐
 * │ Method   │ 기능                                            │
 * ├──────────┼────────────────────────────────────────────────┤
 * │ GET      │ 메인 페이지(index.jsp)로 포워드                  │
 * │          │ 세션에 로그인 정보가 있으면 loginMember 전달      │
 * └──────────┴────────────────────────────────────────────────┘
 */
@WebServlet("/main")
public class MainController extends HttpServlet implements ControllerHelper {
	private static final long serialVersionUID = 1L;

	// ─── GET ─────────────────────────────────────────────────────────────────

	/**
	 * 메인 페이지 요청 처리
	 *
	 * 1. 세션에서 loginMember 확인
	 * 2. 로그인 상태이면 isLoggedIn=true, userName 을 request attribute 로 전달
	 * 3. index.jsp 로 포워드
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	// ─── POST ────────────────────────────────────────────────────────────────

	/**
	 * POST 요청은 GET 으로 위임
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}
	
	/**
     * 모든 요청을 한곳에서 처리 (중복 제거)
     */
    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. 공통 로직: 세션 정보 확인 (for html)
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginMember") != null) {
            Member loginMember = (Member) session.getAttribute("loginMember");
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("userName", loginMember.getName());
        } else {
            request.setAttribute("isLoggedIn", false);
        }

        // 2. 분기 처리: action 파라미터에 따른 이동 (for jsp)
        String action = getActionParameter(request, response);

        switch (action) {
            case "index" -> forward(request, response, "/index.jsp");
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}