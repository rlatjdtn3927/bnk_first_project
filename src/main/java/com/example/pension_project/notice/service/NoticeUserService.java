package com.example.pension_project.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.pension_project.jpa.entity.admin.Notice;
import com.example.pension_project.jpa.repository.admin.NoticeRepository;
import com.example.pension_project.notice.dto.NoticeDto;
import com.example.pension_project.notice.dto.NoticeSummaryDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeUserService {

    private final NoticeRepository noticeRepository;

    // 공지 목록 (페이징 + 정렬)
    public Page<NoticeSummaryDto> getNotices(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAllOrderByCreatedAt(pageable);
        return notices.map(notice -> new NoticeSummaryDto(
                notice.getB_id(),
                notice.getB_title(),
                notice.getB_created_at(),
                notice.getB_view()
        ));
    }

    // 공지 상세
    public NoticeDto getNoticeDetail(Integer id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 공지사항이 없습니다."));
        notice.setB_view(notice.getB_view() + 1);
        noticeRepository.save(notice);

        return new NoticeDto(
                notice.getB_id(),
                notice.getB_title(),
                notice.getB_content(),
                notice.getB_created_at(),
                notice.getB_view()
        );
    }
}
