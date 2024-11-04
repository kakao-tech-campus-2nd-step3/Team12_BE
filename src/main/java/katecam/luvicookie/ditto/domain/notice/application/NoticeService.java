package katecam.luvicookie.ditto.domain.notice.application;

import katecam.luvicookie.ditto.domain.notice.dao.NoticeRepository;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeListResponse;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeResponse;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeUpdateRequest;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public void create(NoticeCreateRequest noticeCreateRequest, TeamMate teamMate) {
        Notice notice = noticeCreateRequest.toEntity();
        notice.setTeamMate(teamMate);
        noticeRepository.save(notice);
    }

    public NoticeResponse getNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        return NoticeResponse.from(notice);
    }


    public NoticeListResponse getNotices(Pageable pageable) {
        Page<NoticeResponse> noticeResponses = noticeRepository.findAll(pageable)
                .map(NoticeResponse::from);
        return NoticeListResponse.from(noticeResponses);
    }

    @Transactional
    public NoticeResponse updateNotice(Integer noticeId, NoticeUpdateRequest noticeUpdateRequest, TeamMate teamMate) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        isWriterMatches(notice, teamMate);
        notice.updateNotice(noticeUpdateRequest);
        return NoticeResponse.from(notice);
    }

    public void deleteNotice(Integer noticeId, TeamMate teamMate) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        isWriterMatches(notice, teamMate);
        noticeRepository.deleteById(noticeId);
    }

    public void isWriterMatches(Notice notice, TeamMate teamMate){
        if(notice.getTeamMate().getMember() != teamMate){
             throw new GlobalException((ErrorCode.WRITER_NOT_MATCH));
        }
    }

}
