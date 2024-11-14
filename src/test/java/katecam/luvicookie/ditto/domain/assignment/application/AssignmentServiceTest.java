package katecam.luvicookie.ditto.domain.assignment.application;

import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentFileRepository;
import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentRepository;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.assignment.dto.request.AssignmentRequest;
import katecam.luvicookie.ditto.domain.assignment.dto.response.*;
import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssignmentService 테스트")
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private AssignmentFileRepository assignmentFileRepository;

    @Mock
    private AwsFileService awsFileService;

    @Mock
    private StudyMemberService studyMemberService;

    @InjectMocks
    private AssignmentService assignmentService;

    @Nested
    @DisplayName("과제 생성")
    class CreateAssignment {

        @Test
        @DisplayName("과제 생성 성공")
        void createAssignmentSuccess() {
            AssignmentRequest request = new AssignmentRequest("테스트 과제", "테스트 설명", "2024-12-31 12:00");
            Integer studyId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            given(studyRepository.findById(studyId)).willReturn(Optional.of(study));
            given(assignmentRepository.save(any())).willReturn(Assignment.builder().build());

            AssignmentCreateResponse response = assignmentService.create(request, studyId, member);

            assertThat(response).isNotNull();
            verify(studyMemberService).validateStudyLeader(studyId, member);
            verify(assignmentRepository).save(any(Assignment.class));
        }
    }

    @Nested
    @DisplayName("과제 수정")
    class UpdateAssignment {

        @Test
        @DisplayName("과제 수정 성공")
        void updateAssignmentSuccess() {
            Integer assignmentId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            AssignmentRequest request = new AssignmentRequest("수정된 과제", "수정된 설명", "2024-12-31 12:00");

            Assignment assignment = Assignment.builder()
                    .title("테스트 과제")
                    .content("테스트 설명")
                    .study(study)
                    .deadline(LocalDateTime.of(2024,12,12,12,0))
                    .build();
            assignment.initCreatedAt();

            given(assignmentRepository.findById(assignmentId)).willReturn(Optional.of(assignment));

            AssignmentResponse response = assignmentService.update(assignmentId, request, member);

            assertThat(response).isNotNull();
            verify(studyMemberService).validateStudyLeader(assignment.getStudy().getId(), member);
        }
    }

    @Nested
    @DisplayName("과제 삭제")
    class DeleteAssignment {

        @Test
        @DisplayName("과제 삭제 성공")
        void deleteAssignmentSuccess() {
            Integer assignmentId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();

            Study study = Study.builder()
                    .name("테스트 스터디")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            Assignment assignment = Assignment.builder()
                    .title("테스트 과제")
                    .content("테스트 설명")
                    .study(study)
                    .deadline(LocalDateTime.of(2024,12,12,12,0))
                    .build();
            assignment.initCreatedAt();

            given(assignmentRepository.findById(assignmentId)).willReturn(Optional.of(assignment));
            given(assignmentRepository.existsById(assignmentId)).willReturn(true); // existsById가 true를 반환하도록 모킹

            assertDoesNotThrow(() -> assignmentService.delete(assignmentId, member));

            verify(studyMemberService).validateStudyLeader(assignment.getStudy().getId(), member);
            verify(assignmentRepository).deleteById(assignmentId); // deleteById가 호출되었는지 확인

        }
    }

    @Nested
    @DisplayName("과제 목록 조회")
    class GetAssignments {

        @Test
        @DisplayName("과제 목록 조회 성공")
        void getAssignmentsSuccess() {
            Integer studyId = 1;
            Study study = Study.builder()
                    .name("테스트 스터디")
                    .isOpen(true)
                    .build();
            study.initCreatedAt();

            Assignment assignment = Assignment.builder()
                    .title("테스트 과제")
                    .content("테스트 설명")
                    .study(study)
                    .deadline(LocalDateTime.of(2024,12,12,12,0))
                    .build();
            assignment.initCreatedAt();

            Pageable pageable = PageRequest.of(0, 10);
            Page<Assignment> assignments = new PageImpl<>(List.of(assignment));

            given(studyRepository.findById(studyId)).willReturn(Optional.of(study));
            given(assignmentRepository.findAllByStudy(pageable, study)).willReturn(assignments);

            AssignmentListResponse response = assignmentService.getAssignments(pageable, studyId);

            assertThat(response).isNotNull();
            assertThat(response.assignments()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("파일 업로드")
    class UploadAssignments {

        @Test
        @DisplayName("과제 파일 업로드 성공")
        void uploadAssignmentFileSuccess() throws IOException {
            Integer assignmentId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();
            MultipartFile file = any();

            Assignment assignment = Assignment.builder()
                    .title("테스트 과제")
                    .content("테스트 설명")
                    .deadline(LocalDateTime.of(2024,12,12,12,0))
                    .build();
            assignment.initCreatedAt();

            given(assignmentRepository.findById(assignmentId)).willReturn(Optional.of(assignment));
            given(awsFileService.saveAssignment(file)).willReturn(new FileResponse(any(), "assignment/testFile.pdf", "https://testFile.pdf"));

            AssignmentFilesResponse response = assignmentService.uploadAssignments(member, assignmentId, file);

            assertThat(response).isNotNull();
            verify(assignmentFileRepository).save(any(AssignmentFile.class));
        }

        @Test
        @DisplayName("과제 파일 업로드 실패 - 파일 저장 오류")
        void uploadAssignmentFileFail_FileSaveError() throws IOException {
            Integer assignmentId = 1;
            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();
            MultipartFile file = any();

            Assignment assignment = Assignment.builder()
                    .title("테스트 과제")
                    .content("테스트 설명")
                    .deadline(LocalDateTime.of(2024,12,12,12,0))
                    .build();
            assignment.initCreatedAt();

            given(assignmentRepository.findById(assignmentId)).willReturn(Optional.of(assignment));
            willThrow(new GlobalException(ErrorCode.FILE_UPLOAD_FAILED)).given(awsFileService).saveAssignment(file);

            assertThatThrownBy(() -> assignmentService.uploadAssignments(member, assignmentId, file))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ErrorCode.FILE_UPLOAD_FAILED.getMessage());
        }
    }

    @Nested
    @DisplayName("과제 파일 다운로드")
    class DownloadAssignmentFile {

        @Test
        @DisplayName("과제 파일 다운로드 성공")
        void downloadAssignmentFileSuccess() throws IOException {
            Integer fileId = 1;

            Member member = Member.builder()
                    .name("테스트 회원")
                    .build();
            MultipartFile file = any();

            Assignment assignment = Assignment.builder()
                    .title("테스트 과제")
                    .content("테스트 설명")
                    .deadline(LocalDateTime.of(2024,12,12,12,0))
                    .build();
            assignment.initCreatedAt();

            AssignmentFile assignmentFile = new AssignmentFile("testFile", assignment, member, "http://example.com/testFile");

            given(assignmentFileRepository.findById(fileId)).willReturn(Optional.of(assignmentFile));
            given(awsFileService.downloadFile(assignmentFile.getFileName())).willReturn(ResponseEntity.ok(new byte[0]));

            ResponseEntity<byte[]> response = assignmentService.download(fileId);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }
    }
}
