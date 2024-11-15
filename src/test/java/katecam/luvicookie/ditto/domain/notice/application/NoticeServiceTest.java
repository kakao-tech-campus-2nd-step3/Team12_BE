package katecam.luvicookie.ditto.domain.notice.application;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.dao.NoticeRepository;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeUpdateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeListResponse;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeResponse;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeService 테스트")
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private StudyRepository studyRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Nested
    @DisplayName("공지 생성")
    class CreateNotice {

        @Test
        @DisplayName("공지 생성 성공")
        void createNoticeSuccess() {
            NoticeCreateRequest request = new NoticeCreateRequest("공지 제목", "공지 내용");
            Integer studyId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            given(studyRepository.findById(studyId)).willReturn(Optional.of(study));

            assertDoesNotThrow(() -> noticeService.create(request, studyId, member));
            verify(noticeRepository).save(any(Notice.class));
        }

        @Test
        @DisplayName("공지 생성 실패 - 스터디 없음")
        void createNoticeFail_StudyNotFound() {
            NoticeCreateRequest request = new NoticeCreateRequest("공지 제목", "공지 내용");
            Integer studyId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            given(studyRepository.findById(studyId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> noticeService.create(request, studyId, member))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.STUDY_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("공지 조회")
    class GetNotice {

        @Test
        @DisplayName("공지 조회 성공")
        void getNoticeSuccess() {
            Integer noticeId = 1;

            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            Notice notice = Notice.builder()
                    .title("공지 제목")
                    .content("공지 내용")
                    .member(member)
                    .build();
            notice.initCreatedAt();

            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            NoticeResponse response = noticeService.getNotice(noticeId);

            assertThat(response).isNotNull();
            assertThat(response.title()).isEqualTo(notice.getTitle());
        }

        @Test
        @DisplayName("공지 조회 실패 - 공지 없음")
        void getNoticeFail_NoticeNotFound() {
            Integer noticeId = 1;

            given(noticeRepository.findById(noticeId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> noticeService.getNotice(noticeId))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("공지 목록 조회")
    class GetNotices {

        @Test
        @DisplayName("공지 목록 조회 성공")
        void getNoticesSuccess() {
            Integer studyId = 1;

            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            Notice notice = Notice.builder()
                    .title("공지 제목")
                    .content("공지 내용")
                    .member(member)
                    .build();
            notice.initCreatedAt();

            Pageable pageable = PageRequest.of(0, 10);

            Page<Notice> notices = new PageImpl<>(List.of(notice));

            given(studyRepository.findById(studyId)).willReturn(Optional.of(study));
            given(noticeRepository.findAllByStudy(pageable, study)).willReturn(notices);

            NoticeListResponse response = noticeService.getNotices(pageable, studyId);

            assertThat(response).isNotNull();
            assertThat(response.notices()).isNotEmpty();
        }

        @Test
        @DisplayName("공지 목록 조회 실패 - 스터디 없음")
        void getNoticesFail_StudyNotFound() {
            Integer studyId = 1;
            Pageable pageable = PageRequest.of(0, 10);

            given(studyRepository.findById(studyId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> noticeService.getNotices(pageable, studyId))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.STUDY_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("공지 수정")
    class UpdateNotice {

        @Test
        @DisplayName("공지 수정 성공")
        void updateNoticeSuccess() {
            Integer noticeId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            NoticeUpdateRequest request = new NoticeUpdateRequest("수정된 제목", "수정된 내용");
            Notice notice = Notice.builder()
                    .title("공지 제목")
                    .content("공지 내용")
                    .member(member)
                    .build();
            notice.initCreatedAt();

            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            NoticeResponse response = noticeService.updateNotice(noticeId, request, member);

            assertThat(response).isNotNull();
            assertThat(response.title()).isEqualTo(request.title());
            assertThat(response.content()).isEqualTo(request.content());
        }

        @Test
        @DisplayName("공지 수정 실패 - 공지 없음")
        void updateNoticeFail_NoticeNotFound() {
            Integer noticeId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            NoticeUpdateRequest request = new NoticeUpdateRequest("수정된 제목", "수정된 내용");

            given(noticeRepository.findById(noticeId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> noticeService.updateNotice(noticeId, request, member))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("공지 수정 실패 - 작성자 불일치")
        void updateNoticeFail_WriterNotMatch() {
            Integer noticeId = 1;
            Member member1 = Member.builder()
                    .name("테스트 회원1")
                    .nickname("닉네임1")
                    .build();

            Member member2 = Member.builder()
                    .name("테스트 회원2")
                    .nickname("닉네임2")
                    .build();

            NoticeUpdateRequest request = new NoticeUpdateRequest("수정된 제목", "수정된 내용");

            Notice notice = Notice.builder()
                    .title("공지 제목")
                    .content("공지 내용")
                    .member(member2)
                    .build();
            notice.initCreatedAt();

            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            assertThatThrownBy(() -> noticeService.updateNotice(noticeId, request, member1))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.WRITER_NOT_MATCH.getMessage());
        }
    }

    @Nested
    @DisplayName("공지 삭제")
    class DeleteNotice {

        @Test
        @DisplayName("공지 삭제 성공")
        void deleteNoticeSuccess() {
            Integer noticeId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .nickname("닉네임")
                    .build();

            Notice notice = Notice.builder()
                    .title("공지 제목")
                    .content("공지 내용")
                    .member(member)
                    .build();
            notice.initCreatedAt();

            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            assertDoesNotThrow(() -> noticeService.deleteNotice(noticeId, member));
            verify(noticeRepository).deleteById(noticeId);
        }

        @Test
        @DisplayName("공지 삭제 실패 - 공지 없음")
        void deleteNoticeFail_NoticeNotFound() {
            Integer noticeId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();

            given(noticeRepository.findById(noticeId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> noticeService.deleteNotice(noticeId, member))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.NOTICE_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("공지 삭제 실패 - 작성자 불일치")
        void deleteNoticeFail_WriterNotMatch() {
            Integer noticeId = 1;
            Member member1 = Member.builder()
                    .name("테스트 회원1")
                    .nickname("닉네임1")
                    .build();

            Member member2 = Member.builder()
                    .name("테스트 회원2")
                    .nickname("닉네임2")
                    .build();

            Notice notice = Notice.builder()
                    .title("공지 제목")
                    .content("공지 내용")
                    .member(member2)
                    .build();
            notice.initCreatedAt();

            given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

            assertThatThrownBy(() -> noticeService.deleteNotice(noticeId, member1))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.WRITER_NOT_MATCH.getMessage());
        }
    }
}
