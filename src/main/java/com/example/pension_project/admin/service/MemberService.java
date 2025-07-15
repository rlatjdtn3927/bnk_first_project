package com.example.pension_project.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pension_project.admin.dto.MemberDto;
import com.example.pension_project.jpa.entity.admin.Member;
import com.example.pension_project.jpa.repository.admin.MemberRepository;

@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;
	private Member convert(MemberDto dto) {
	    Member member = new Member();
	    member.setUserid(dto.getUserid());
	    member.setUsername(dto.getUsername());
	    member.setPassword(dto.getPassword());
	    member.setName(dto.getName());
	    member.setEmail(dto.getEmail());
	    member.setPhone(dto.getPhone());
	    member.setRoll(dto.getRoll());
	    return member;
	}
	private MemberDto convertToDto(Member member) {
	    MemberDto dto = new MemberDto();
	    dto.setUserid(member.getUserid());
	    dto.setUsername(member.getUsername());
	    dto.setPassword(member.getPassword());
	    dto.setName(member.getName());
	    dto.setEmail(member.getEmail());
	    dto.setPhone(member.getPhone());
	    dto.setRoll(member.getRoll());
	    return dto;
	}
	
	public boolean logincheck(MemberDto memberDto) {
		Member member = new Member();
		member = memberRepository.findByUsername(memberDto.getUsername());
		System.out.println("(MemberService)세션에 저장될 사용자 정보"+member);
		if(member != null && memberDto.getPassword().equals(member.getPassword())) {
			return true;
		}
		return false;
	}
	
	public MemberDto getUserInfoByUsername(String username) {
		System.out.println("(MemberService)요청받은 사용자 이름: "+ username);
		return convertToDto(memberRepository.findByUsername(username));
	}
}
