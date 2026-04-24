package com.ssafy.controller;

import com.ssafy.dto.Member;
import com.ssafy.service.MemberService;
import com.ssafy.service.MemberServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * 인증 컨트롤러  /auth
 *
 * GET  action=check        세션 로그인 여부 확인 (nav 렌더링용)
 * GET  action=logout       로그아웃
 * POST action=login        로그인
 * POST action=logout       로그아웃
 * POST action=findPassword 비밀번호 찾기
 */
@WebServlet("/auth")
public class AuthController extends HttpServlet implements ControllerHelper {
    private static final long serialVersionUID = 1L;

    private final MemberService memberService = MemberServiceImpl.getInstance();

    // ── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = param(request, "action");
        switch (action) {
            case "check"  -> checkSession(request, response);
            case "logout" -> logout(request, response);
            default       -> response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = param(request, "action");
        switch (action) {
            case "login"        -> login(request, response);
            case "logout"       -> logout(request, response);
            case "findPassword" -> findPassword(request, response);
            default             -> response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // ── 세션 확인 (nav 렌더링) ────────────────────────────────────────────────

    /**
     * GET /auth?action=check
     * 로그인 중: {"result":"success","name":"홍길동"}
     * 비로그인: {"result":"fail"}
     */
    private void checkSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginMember") != null) {
            Member m = (Member) session.getAttribute("loginMember");
            sendJson(response, "{\"result\":\"success\",\"name\":\"" + escape(m.getName()) + "\"}");
        } else {
            sendJson(response, "{\"result\":\"fail\"}");
        }
    }

    // ── 로그인 ────────────────────────────────────────────────────────────────

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id       = param(request, "id");
        String password = param(request, "password");

        if (id.isEmpty() || password.isEmpty()) {
            sendJson(response, buildResult(false, "아이디와 비밀번호를 입력해 주세요."));
            return;
        }

        try {
            Member member = memberService.login(id, password);
            if (member == null) {
                sendJson(response, buildResult(false, "아이디 또는 비밀번호가 일치하지 않습니다."));
                return;
            }

            // 세션 고정 공격 방지
            HttpSession old = request.getSession(false);
            if (old != null) old.invalidate();

            HttpSession session = request.getSession(true);
            session.setAttribute("loginMember", member);
            session.setMaxInactiveInterval(60 * 30);

            sendJson(response, "{\"result\":\"success\",\"name\":\"" + escape(member.getName()) + "\"}");

        } catch (Exception e) {
            sendJson(response, buildResult(false, e.getMessage()));
        }
    }

    // ── 로그아웃 ──────────────────────────────────────────────────────────────

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        String xhr = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xhr)) {
            sendJson(response, buildResult(true, null));
        } else {
            redirect(response, request.getContextPath() + "/main");
        }
    }

    // ── 비밀번호 찾기 ─────────────────────────────────────────────────────────

    private void findPassword(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id    = param(request, "id");
        String phone = param(request, "phone");

        if (id.isEmpty() || phone.isEmpty()) {
            sendJson(response, buildResult(false, "아이디와 전화번호를 입력해 주세요."));
            return;
        }

        try {
            String password = memberService.findPassword(id, phone);
            if (password == null) {
                sendJson(response, buildResult(false, "일치하는 회원 정보가 없습니다."));
                return;
            }
            sendJson(response, "{\"result\":\"success\",\"password\":\"" + escape(password) + "\"}");
        } catch (Exception e) {
            sendJson(response, buildResult(false, e.getMessage()));
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}