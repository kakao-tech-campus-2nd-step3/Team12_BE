package katecam.luvicookie.ditto.domain.notice.application;

import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    public void create(NoticeCreateRequest noticeCreateRequest, Member member) {
        Notice notice = noticeCreateRequest.toEntity();
        LocalDateTime now = LocalDateTime.now();
        notice.setCreatedAt(now);
        notice.setMemberId(member.getId());
        noticeRepository.save(notice);
    }

    public NoticeResponse getNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        Member member = memberRepository.findById(notice.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        NoticeResponse noticeResponse = NoticeResponse.from(notice);
        noticeResponse.setNickName(member.getNickname());
        return noticeResponse;
    }


    public NoticeListResponse getNotices(Pageable pageable) {
        Page<NoticeResponse> noticeResponses = noticeRepository.findAll(pageable)
                .map(notice -> {
                    NoticeResponse response = NoticeResponse.from(notice);
                    Member member = memberRepository.findById(notice.getMemberId())
                            .orElseThrow();
                    response.setNickName(member.getNickname()); // 원하는 닉네임을 설정
                    return response;
                });
        return NoticeListResponse.from(noticeResponses);
    }

    @Transactional
    public NoticeResponse updateNotice(Integer noticeId, NoticeUpdateRequest noticeUpdateRequest) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        notice.updateNotice(noticeUpdateRequest);
        Member member = memberRepository.findById(notice.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        NoticeResponse noticeResponse = NoticeResponse.from(notice);
        noticeResponse.setNickName(member.getNickname());
        return noticeResponse;
    }

    public void deleteNotice(Integer noticeId) {
        noticeRepository.findById(noticeId).orElseThrow(() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND));
        noticeRepository.deleteById(noticeId);
    }
}
