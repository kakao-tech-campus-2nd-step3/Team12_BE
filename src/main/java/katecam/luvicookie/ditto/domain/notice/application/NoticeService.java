package katecam.luvicookie.ditto.domain.notice.application;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.dao.NoticeRepository;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeListResponse;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeResponse;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeUpdateRequest;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final StudyRepository studyRepository;

    public void create(NoticeCreateRequest noticeCreateRequest, Integer studyId, Member member) {
        Notice notice = noticeCreateRequest.toEntity();
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        notice.setNoticeInfo(study, member);
        noticeRepository.save(notice);
    }

    public NoticeResponse getNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        return NoticeResponse.from(notice);
    }


    public NoticeListResponse getNotices(Pageable pageable, Integer studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        Page<NoticeResponse> noticeResponses = noticeRepository.findAllByStudy(pageable, study)
                .map(NoticeResponse::from);
        return NoticeListResponse.from(noticeResponses);
    }

    @Transactional
    public NoticeResponse updateNotice(Integer noticeId, NoticeUpdateRequest noticeUpdateRequest, Member member) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        isWriterMatches(notice, member);
        if(notice.getTitle()!=null)
            notice.updateNoticeTitle(noticeUpdateRequest.title());
        if(notice.getContent()!=null)
            notice.updateNoticeContent(noticeUpdateRequest.content());
        return NoticeResponse.from(notice);
    }

    public void deleteNotice(Integer noticeId, Member member) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        isWriterMatches(notice, member);
        noticeRepository.deleteById(noticeId);
    }

    public void isWriterMatches(Notice notice, Member member){
        if(notice.getMember() != member){
             throw new GlobalException((ErrorCode.WRITER_NOT_MATCH));
        }
    }

}
