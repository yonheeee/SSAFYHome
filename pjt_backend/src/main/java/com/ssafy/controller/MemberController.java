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
 * 회원 CRUD 컨트롤러
 *
 * URL 패턴: /member
 *
 * ┌──────────┬──────────┬──────────────────────────────────────┐
 * │ Method   │ action   │ 기능                                  │
 * ├──────────┼──────────┼──────────────────────────────────────┤
 * │ POST     │ register │ 회원 등록 (회원가입)                   │
 * │ GET      │ info     │ 로그인 회원 정보 조회                  │
 * │ POST     │ update   │ 회원 정보 수정                        │
 * │ POST     │ delete   │ 회원 탈퇴                             │
 * └──────────┴──────────┴──────────────────────────────────────┘
 */
@WebServlet("/member")
public class MemberController extends HttpServlet implements ControllerHelper {
	private static final long serialVersionUID = 1L;

	private final MemberService memberService = MemberServiceImpl.getInstance();

	// ─── GET ─────────────────────────────────────────────────────────────────

	/**
	 * 회원 정보 조회
	 * action=info → 현재 세션 회원 정보를 JSON 으로 반환
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = param(request, "action");

		if ("info".equals(action)) {
			getMemberInfo(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원하지 않는 요청입니다.");
		}
	}

	// ─── POST ────────────────────────────────────────────────────────────────

	/**
	 * action 파라미터로 register / update / delete 분기
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String action = param(request, "action");

		switch (action) {
			case "register" -> registerMember(request, response);
			case "update"   -> updateMember(request, response);
			case "delete"   -> deleteMember(request, response);
			default         -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원하지 않는 요청입니다.");
		}
	}

	// ─── 회원 등록 ────────────────────────────────────────────────────────────

	/**
	 * 회원가입 처리
	 * 필수: id, password, name, phone
	 * 선택: address
	 */
	private void registerMember(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String id       = param(request, "id");
		String password = param(request, "password");
		String name     = param(request, "name");
		String address  = param(request, "address");
		String phone    = param(request, "phone");

		// 필수 값 검증
		if (id.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
			sendJson(response, buildResult(false, "필수 항목을 모두 입력해 주세요."));
			return;
		}

		try {
			Member member = new Member(id, password, name, address, phone);
			memberService.registerMember(member);
			sendJson(response, buildResult(true, null));
		} catch (Exception e) {
			sendJson(response, buildResult(false, e.getMessage()));
		}
	}

	// ─── 회원 조회 ────────────────────────────────────────────────────────────

	/**
	 * 세션에서 로그인 회원 아이디를 읽어 정보를 JSON 으로 반환
	 */
	private void getMemberInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginMember") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			sendJson(response, buildResult(false, "로그인이 필요합니다."));
			return;
		}

		Member loginMember = (Member) session.getAttribute("loginMember");

		try {
			Member member = memberService.getMember(loginMember.getId());
			if (member == null) {
				sendJson(response, buildResult(false, "회원 정보를 찾을 수 없습니다."));
				return;
			}
			// 간단 JSON 직렬화 (Jackson 없이)
			String json = toJson(member);
			sendJson(response, json);
		} catch (Exception e) {
			sendJson(response, buildResult(false, e.getMessage()));
		}
	}

	// ─── 회원 수정 ────────────────────────────────────────────────────────────

	/**
	 * 회원 정보 수정
	 * 세션 회원의 password / name / address / phone 수정
	 */
	private void updateMember(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginMember") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			sendJson(response, buildResult(false, "로그인이 필요합니다."));
			return;
		}

		Member loginMember = (Member) session.getAttribute("loginMember");

		String password = param(request, "password");
		String name     = param(request, "name");
		String address  = param(request, "address");
		String phone    = param(request, "phone");

		if (password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
			sendJson(response, buildResult(false, "필수 항목을 모두 입력해 주세요."));
			return;
		}

		try {
			Member updated = new Member(loginMember.getId(), password, name, address, phone);
			memberService.updateMember(updated);

			// 세션의 이름도 최신화
			loginMember.setName(name);

			sendJson(response, buildResult(true, null));
		} catch (Exception e) {
			sendJson(response, buildResult(false, e.getMessage()));
		}
	}

	// ─── 회원 탈퇴 ────────────────────────────────────────────────────────────

	/**
	 * 회원 탈퇴 후 세션 무효화
	 */
	private void deleteMember(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginMember") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			sendJson(response, buildResult(false, "로그인이 필요합니다."));
			return;
		}

		Member loginMember = (Member) session.getAttribute("loginMember");

		try {
			memberService.deleteMember(loginMember.getId());
			session.invalidate();     // 탈퇴 후 세션 파기
			sendJson(response, buildResult(true, null));
		} catch (Exception e) {
			sendJson(response, buildResult(false, e.getMessage()));
		}
	}

	// ─── 내부 유틸 ───────────────────────────────────────────────────────────

	/** Member 객체를 JSON 문자열로 변환 (비밀번호 제외) */
	private String toJson(Member m) {
		return String.format(
			"{\"id\":\"%s\",\"password\":\"%s\",\"name\":\"%s\",\"address\":\"%s\",\"phone\":\"%s\"}",
			escape(m.getId()),
			escape(m.getPassword()),
			escape(m.getName()),
			escape(m.getAddress() == null ? "" : m.getAddress()),
			escape(m.getPhone())
		);
	}

	private String escape(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}