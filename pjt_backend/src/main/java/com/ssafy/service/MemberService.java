package com.ssafy.service;

import com.ssafy.dto.Member;

public interface MemberService {
	// 회원 등록(회원 가입)
	void registerMember(Member member) throws Exception;

	// 아이디 회원 하나 조회
	Member getMember(String id) throws Exception;

	// 회원 정보 수정(비밀번호, 이름, 주소, 전화번호)
	void updateMember(Member member) throws Exception;

	// 회원 탈퇴(id)
	void deleteMember(String id) throws Exception;

	// 아이디 찾기 - 이름과 전화번호 일치하는 아이디 반환
	String findId(String name, String phone) throws Exception;

	// 비밀번호 찾기 - 아이디와 전화번호 일치하는 비밀번호 반환
	String findPassword(String id, String phone) throws Exception;

	// 로그인
	Member login(String id, String password) throws Exception;
}