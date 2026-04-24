package com.ssafy.service;

import com.ssafy.dto.Member;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MemberServiceImpl implements MemberService{
	//싱글톤
	private static final MemberServiceImpl instance = new MemberServiceImpl();

	private MemberServiceImpl() {};
	
	public static MemberServiceImpl getInstance() {
		return instance;
	}
	
	private final Map<String, Member> store = new ConcurrentHashMap<>();
	
	//등록
	@Override
	public void registerMember(Member member) throws Exception {
		//아이디 비어있거나 null이면 오류 메세지 IllegalArgumentException;
		if(member == null || member.getId() == null || member.getId().isBlank()) {
			throw new IllegalArgumentException("회원 정보가 올바르지 않습니다");
		}
		if(store.containsKey(member.getId())) {
			throw new Exception("이미 사용 중인 아이디입니다."+member.getId());			
		}
		store.put(member.getId(), member);
	}

	//조회
	@Override
	public Member getMember(String id) throws Exception {
		if(id == null || id.isBlank()) {
			throw new IllegalArgumentException("아이디를 입력해주세요");
		}
		return store.get(id);
	}

	//수정
	@Override
	public void updateMember(Member member) throws Exception {
		if(member == null || member.getId() == null) {
			throw new IllegalArgumentException("수정할 회원 정보가 올바르지 않습니다");
		}
		//아이디 변경 불가, 나머지는 변경 가능
		Member existing = store.get(member.getId());
		if(existing == null) {
			throw new Exception("존재하지 않는 회원입니다: "+member.getId());
		}
		//null이 아닌 필드 갱신
		if(member.getPassword() != null && !member.getPassword().isBlank()) {
			existing.setPassword(member.getPassword());
		}
		if(member.getName() != null && !member.getName().isBlank()) {
			existing.setName(member.getName());
		}
		if(member.getAddress() != null) {
			existing.setAddress(member.getAddress());
		}
		if(member.getPhone() != null && !member.getPhone().isBlank()) {
			existing.setPhone(member.getPhone());
		}
		
		
	}

	//삭제
	@Override
	public void deleteMember(String id) throws Exception {
		// TODO Auto-generated method stub
		if(id == null || id.isBlank()) {
			throw new IllegalArgumentException("아이디를 입력해 주세요");
		}
		if(!store.containsKey(id)) {
			throw new Exception("존재하지 않는 아이디입니다");
		}
		store.remove(id);
	}

	//비밀번호 찾기
	@Override
	public String findPassword(String id, String phone) throws Exception {
		// TODO Auto-generated method stub
		if(id == null || phone == null) {
			throw new IllegalArgumentException("아이디와 전화번호를 입력해주세요");
		}
		Member member = store.get(id);
		if(member != null && phone.equals(member.getPhone())) {
			return member.getPassword();
		}
		
		return null;

	}

	//로그인
	@Override
	public Member login(String id, String password) throws Exception {
		if(id == null || password == null) {
			throw new IllegalArgumentException("아이디와 비밀번호를 입력해주세요");
		}
		Member member = store.get(id);
		if(member != null && member.getPassword().equals(password)) {
			return member;
		}
		return null;
	}

}
