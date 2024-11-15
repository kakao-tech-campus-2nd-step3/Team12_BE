package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyUpdateRequest;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
import katecam.luvicookie.ditto.domain.studymember.dao.StudyMemberRepository;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyLeaderResponse;
import katecam.luvicookie.ditto.fixture.MemberFixture;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("스터디 서비스 테스트")
class StudyServiceTest {

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private AwsFileService awsFileService;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @Mock
    private StudyMemberService studyMemberService;

    @InjectMocks
    private StudyService studyService;

    @Nested
    @DisplayName("스터디 조회")
    class getStudy {
        @Test
        @DisplayName("스터디 목록 페이지 조회 성공")
        void getStudyListSuccess() {
            Study study1 = Study.builder()
                    .name("테스트 스터디")
                    .topic("테스트 주제")
                    .isOpen(true)
                    .build();
            study1.initCreatedAt();

            Study study2 = Study.builder()
                    .name("CS 스터디")
                    .topic("CS 주제")
                    .isOpen(true)
                    .build();
            study2.initCreatedAt();

            List<Study> studyList = Arrays.asList(study1, study2);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Study> studies = new PageImpl<>(studyList, pageable, 0);
            StudyCriteria criteria = StudyCriteria.builder()
                    .name("스터디")
                    .topic("주제")
                    .isOpen("true")
                    .build();
            given(studyRepository.findAllByTopicAndNameAndIsOpen(criteria.topic(), criteria.name(), Boolean.getBoolean(criteria.isOpen()), pageable))
                    .willReturn(studies);

            Integer numberOfMember = 2;
            StudyLeaderResponse studyLeaderResponse = new StudyLeaderResponse(new MemberFixture(1), numberOfMember);
            given(studyMemberService.getStudyLeader(any()))
                    .willReturn(studyLeaderResponse);

            StudyListResponse studyListResponse = studyService.getStudyList(pageable, criteria);

            assertThat(studyListResponse).isNotNull();
            assertThat(studyListResponse.studies().size())
                    .isEqualTo(studyList.size());
        }

        @Test
        @DisplayName("스터디 조회 성공")
        void getStudyByIdSuccess() {
            Study study = Study.builder()
                    .name("테스트 스터디")
                    .topic("테스트 주제")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            Integer studyId = 1;

            given(studyRepository.findById(studyId))
                    .willReturn(Optional.of(study));

            Integer numberOfMember = 2;
            StudyLeaderResponse studyLeaderResponse = new StudyLeaderResponse(new MemberFixture(1), numberOfMember);
            given(studyMemberService.getStudyLeader(any()))
                    .willReturn(studyLeaderResponse);

            StudyResponse studyResponse = studyService.getStudy(studyId);

            assertThat(studyResponse).isNotNull();
            assertThat(studyResponse.name()).isEqualTo(study.getName());
        }

        @Test
        @DisplayName("사용자 스터디 조회 성공")
        void getStudyListOfMemberFail() {
            Study study1 = Study.builder()
                    .name("테스트 스터디")
                    .topic("테스트 주제")
                    .isOpen(true)
                    .build();
            study1.initCreatedAt();

            Study study2 = Study.builder()
                    .name("CS 스터디")
                    .topic("CS 주제")
                    .isOpen(true)
                    .build();
            study2.initCreatedAt();

            List<Study> studyList = Arrays.asList(study1, study2);

            StudyMember studyMember1 = StudyMember.builder()
                    .studyId(1)
                    .build();

            StudyMember studyMember2 = StudyMember.builder()
                    .studyId(2)
                    .build();

            List<StudyMember> studyMemberList = Arrays.asList(studyMember1, studyMember2);

            Integer memberId = 1;

            given(studyMemberRepository.findAllByMember_Id(memberId))
                    .willReturn(studyMemberList);
            given(studyRepository.findById(studyMember1.getStudyId()))
                    .willReturn(Optional.of(study1));
            given(studyRepository.findById(studyMember2.getStudyId()))
                    .willReturn(Optional.of(study2));

            Integer numberOfMember = 2;
            StudyLeaderResponse studyLeaderResponse = new StudyLeaderResponse(new MemberFixture(1), numberOfMember);
            given(studyMemberService.getStudyLeader(any()))
                    .willReturn(studyLeaderResponse);

            List<StudyResponse> studyResponseList = studyService.getStudyListByMemberId(memberId);

            assertThat(studyResponseList).isNotNull();
            assertThat(studyResponseList.size()).isEqualTo(studyList.size());
            verify(studyRepository).findById(studyMember1.getStudyId());
            verify(studyRepository).findById(studyMember2.getStudyId());
        }
    }

    @Nested
    @DisplayName("스터디 생성")
    class createStudy {
        @Test
        @DisplayName("스터디 생성 성공")
        void createStudySuccess() throws IOException {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            StudyCreateRequest request = new StudyCreateRequest("스터디", "스터디 설명", true, "스터디 주제");

            String imageUrl = "https://test_domain.com/image.jpg";

            given(awsFileService.saveStudyProfileImage(any()))
                    .willReturn(imageUrl);

            assertDoesNotThrow(() -> studyService.create(member, request, null));
            verify(awsFileService).saveStudyProfileImage(null);
            verify(studyRepository).save(any());
            verify(studyMemberService).createStudyMember(any(), eq(member), eq(StudyMemberRole.LEADER), eq(""));
        }

        @Test
        @DisplayName("스터디 생성 실패")
        void createStudyFail() throws IOException {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            StudyCreateRequest request = new StudyCreateRequest("스터디", "스터디 설명", true, "스터디 주제");

            given(awsFileService.saveStudyProfileImage(any()))
                    .willThrow(IOException.class);

            assertThatThrownBy(() -> studyService.create(member, request, null))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.FILE_UPLOAD_FAILED.getMessage());
            verify(awsFileService).saveStudyProfileImage(null);
        }
    }

    @Nested
    @DisplayName("스터디 삭제")
    class deleteStudy {
        @Test
        @DisplayName("스터디 삭제 성공")
        void deleteStudySuccess() {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            Integer studyId = 1;

            assertDoesNotThrow(() -> studyService.delete(member, studyId));
            verify(studyMemberService).validateStudyLeader(studyId, member);
            verify(studyMemberService).deleteAllStudyMember(studyId);
            verify(studyRepository).deleteById(studyId);
        }

        @Test
        @DisplayName("스터디 삭제 실패")
        void deleteStudyFail() {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            Integer studyId = 1;

            willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                    .given(studyMemberService).validateStudyLeader(studyId, member);

            assertThatThrownBy(() -> studyService.delete(member, studyId))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
            verify(studyMemberService).validateStudyLeader(studyId, member);
        }
    }

    @Nested
    @DisplayName("스터디 수정")
    class updateStudy {
        @Test
        @DisplayName("스터디 정보 수정 성공")
        void updateStudySuccess() {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            Integer studyId = 1;

            StudyUpdateRequest request = new StudyUpdateRequest("스터디", "스터디 설명", true, "스터디 주제");

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .topic("테스트 주제")
                    .isOpen(true)
                    .build();

            given(studyRepository.findById(studyId))
                    .willReturn(Optional.of(study));

            assertDoesNotThrow(() -> studyService.update(member, studyId, request));
            verify(studyMemberService).validateStudyLeader(studyId, member);
            verify(studyRepository).findById(studyId);
        }

        @Test
        @DisplayName("스터디 정보 수정 실패")
        void updateStudyFail() {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            Integer studyId = 1;

            StudyUpdateRequest request = new StudyUpdateRequest("스터디", "스터디 설명", true, "스터디 주제");

            willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                    .given(studyMemberService).validateStudyLeader(studyId, member);

            assertThatThrownBy(() -> studyService.update(member, studyId, request))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
            verify(studyMemberService).validateStudyLeader(studyId, member);
        }

        @Test
        @DisplayName("스터디 프로필 이미지 수정 성공")
        void updateProfileImageSuccess() throws IOException {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            Integer studyId = 1;

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .topic("테스트 주제")
                    .isOpen(true)
                    .build();

            given(studyRepository.findById(studyId))
                    .willReturn(Optional.of(study));

            String imageUrl = "https://test_domain.com/image.jpg";

            given(awsFileService.saveStudyProfileImage(any()))
                    .willReturn(imageUrl);

            assertDoesNotThrow(() -> studyService.updateProfileImage(member, studyId, null));
            verify(studyMemberService).validateStudyLeader(studyId, member);
            verify(studyRepository).findById(studyId);
            verify(awsFileService).saveStudyProfileImage(null);
        }

        @Test
        @DisplayName("스터디 프로필 이미지 수정 실패")
        void updateProfileImageFail() {
            Member member = Member.builder()
                    .name("스터디 회원")
                    .build();

            Integer studyId = 1;

            willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                    .given(studyMemberService).validateStudyLeader(studyId, member);

            assertThatThrownBy(() -> studyService.updateProfileImage(member, studyId, null))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
            verify(studyMemberService).validateStudyLeader(studyId, member);
        }
    }

}