package katecam.luvicookie.ditto.domain.notice.application;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.dao.NoticeRepository;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeListResponse;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeUpdateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    public void create(NoticeCreateRequest noticeCreateRequest, Member member) {
        Notice notice = noticeCreateRequest.toEntity();
        LocalDate now = LocalDate.now();
        notice.setCreatedAt(now);
        notice.setWriter_id(member.getId());
        noticeRepository.save(notice);
    }

    public NoticeResponse getNotice(Integer noticeId) {
        return noticeRepository.findById(noticeId)
                .map(NoticeResponse::from)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
    }


    public NoticeListResponse getNotices(Pageable pageable) {
        Page<NoticeResponse> noticeResponses = noticeRepository.findAll(pageable)
                .map(NoticeResponse::from);
        return NoticeListResponse.from(noticeResponses);
    }

    @Transactional
    public NoticeResponse updateNotice(Integer noticeId, NoticeUpdateRequest noticeUpdateRequest) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        notice.updateNotice(noticeUpdateRequest);
        return NoticeResponse.from(notice);
    }

    public void deleteNotice(Integer noticeId) {
        getNotice(noticeId);
        noticeRepository.deleteById(noticeId);
    }
}
