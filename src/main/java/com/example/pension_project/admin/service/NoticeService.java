package com.example.pension_project.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.pension_project.admin.dto.NoticeDto;
import com.example.pension_project.admin.dto.PendingDto;
import com.example.pension_project.jpa.entity.admin.Notice;
import com.example.pension_project.jpa.entity.admin.Pending;
import com.example.pension_project.jpa.repository.admin.NoticeRepository;
import com.example.pension_project.jpa.repository.admin.PendingRepository;

@Service
public class NoticeService {
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private PendingRepository pendingRepository;

	// DTO → Entity (Pending)
	public Pending convertToEntity(PendingDto dto) {
		Pending entity = new Pending();
		entity.setP_id(dto.getP_id());
		entity.setB_id(dto.getB_id());
		entity.setB_title(dto.getB_title());
		entity.setB_content(dto.getB_content());
		entity.setB_created_at(dto.getB_created_at());
		entity.setStatus(dto.getStatus());
		entity.setAdmin_comment(dto.getAdmin_comment());
		entity.setRejected_comment(dto.getRejected_comment());
		return entity;
	}

	// Entity → DTO (Pending)
	public PendingDto convertToDto(Pending entity) {
		PendingDto dto = new PendingDto();
		dto.setP_id(entity.getP_id());
		dto.setB_id(entity.getB_id());
		dto.setB_title(entity.getB_title());
		dto.setB_content(entity.getB_content());
		dto.setB_created_at(entity.getB_created_at());
		dto.setStatus(entity.getStatus());
		dto.setAdmin_comment(entity.getAdmin_comment());
		dto.setRejected_comment(entity.getRejected_comment());
		return dto;
	}
	
	// DTO → Entity (Notice)
	public Notice convertToNoticeEntity(NoticeDto dto) {
		Notice entity = new Notice();
		entity.setB_id(dto.getB_id());
		entity.setB_title(dto.getB_title());
		entity.setB_content(dto.getB_content());
		entity.setB_view(dto.getB_view());
		return entity;
	}

	// Entity → DTO (Notice)
	public NoticeDto convertToNoticeDto(Notice entity) {
		NoticeDto dto = new NoticeDto();
		dto.setB_id(entity.getB_id());
		dto.setB_title(entity.getB_title());
		dto.setB_content(entity.getB_content());
		dto.setB_view(entity.getB_view());
		return dto;
	}
	
	//----------------------------------------------------------------------------
	//								유틸리티 영역									//
	//----------------------------------------------------------------------------
	
	// 결재 대기 테이블에 적재 (신규 등록)
	public void registrationRequest(PendingDto pendingDto) {
		pendingRepository.save(convertToEntity(pendingDto));
	}
	
	// 결재 상태 업데이트 (기존 데이터 수정)
	public void updatePendingStatus(PendingDto pendingDto) {
	    Optional<Pending> existingPending = pendingRepository.findById(pendingDto.getP_id());
	    if (existingPending.isPresent()) {
	        Pending pending = existingPending.get();
	        pending.setStatus(pendingDto.getStatus());
	        pending.setRejected_comment(pendingDto.getRejected_comment());
	        pendingRepository.save(pending);
	        System.out.println("결재 상태 업데이트 완료: ID=" + pendingDto.getP_id() + ", 상태=" + pendingDto.getStatus());
	    } else {
	        throw new RuntimeException("해당 결재 문서를 찾을 수 없습니다.");
	    }
	}
	
	// 공지사항 테이블에 적재 (신규 등록 또는 수정)
	public void registNotice(NoticeDto noticeDto) {
		System.out.println("전송된 데이터: " + noticeDto);
		
		Notice notice = convertToNoticeEntity(noticeDto);
		
		// b_id가 있으면 UPDATE, 없으면 INSERT
		if (noticeDto.getB_id() != null) {
			// 수정의 경우: 기존 데이터 조회 후 업데이트
			Optional<Notice> existingNotice = noticeRepository.findById(noticeDto.getB_id());
			if (existingNotice.isPresent()) {
				Notice existing = existingNotice.get();
				existing.setB_title(noticeDto.getB_title());
				existing.setB_content(noticeDto.getB_content());
				// 조회수는 유지 (필요시 noticeDto.getB_view()로 변경)
				if (noticeDto.getB_view() != null) {
					existing.setB_view(noticeDto.getB_view());
				}
				noticeRepository.save(existing);
				System.out.println("공지사항 수정 완료: " + noticeDto.getB_title());
			} else {
				// b_id가 있지만 해당 레코드가 없는 경우 새로 생성
				notice.setB_id(noticeDto.getB_id());
				noticeRepository.save(notice);
				System.out.println("공지사항 신규 등록 완료: " + noticeDto.getB_title());
			}
		} else {
			// 신규 등록의 경우
			noticeRepository.save(notice);
			System.out.println("공지사항 신규 등록 완료: " + noticeDto.getB_title());
		}
	}
	
	// 공지사항 수정 결재 요청
	public void correctionNotice(PendingDto pendingDto) {
		System.out.println("(NoticeService)수정하기 위해 넘어온 데이터: " + pendingDto);
		
		System.out.println("Pending에 넘어가는 데이터(correctionNotice): " + pendingDto);
		pendingRepository.save(convertToEntity(pendingDto));
	}
	
	// 공지사항 전체 조회
	public List<NoticeDto> getNotionList() {
		List<Notice> list = noticeRepository.findAll();
		List<NoticeDto> dtoList = new ArrayList<>();
		for (Notice n : list) {
			NoticeDto dto = new NoticeDto();
			dto.setB_id(n.getB_id());
			dto.setB_title(n.getB_title());
			dto.setB_content(n.getB_content());
			dto.setB_view(n.getB_view());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	// 결재 대기 목록 전체 조회
	public List<PendingDto> getPendingList() {
		List<Pending> list = pendingRepository.findAll();
		List<PendingDto> dtoList = new ArrayList<>();
		for (Pending p : list) {
			PendingDto dto = new PendingDto();
			dto.setP_id(p.getP_id());
			dto.setB_id(p.getB_id());
			dto.setB_title(p.getB_title());
			dto.setB_content(p.getB_content());
			dto.setB_created_at(p.getB_created_at());
			dto.setStatus(p.getStatus());
			dto.setAdmin_comment(p.getAdmin_comment());
			dto.setRejected_comment(p.getRejected_comment());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	// pending 개별 조회
	public Optional<PendingDto> getpendinListByPid(Integer p_id) {
		return pendingRepository.findById(p_id).map(this::convertToDto);
	}
	
	// notice 개별 조회
	public Optional<NoticeDto> getNoticeByBid(Integer b_id) {
		return noticeRepository.findById(b_id).map(this::convertToNoticeDto);
	}
	
	// 수정 서비스 구현 (미완성)
	public void correction() {
		// 작성되어 넘어온 데이터(b_id, field, value) 테스트 해봐야함
	}
}