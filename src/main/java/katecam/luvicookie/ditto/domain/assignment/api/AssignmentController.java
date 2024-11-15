package katecam.luvicookie.ditto.domain.assignment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.assignment.dto.request.AssignmentRequest;
import katecam.luvicookie.ditto.domain.assignment.dto.response.*;
import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
@Tag(name = "과제", description = "스터디의 과제 관련 API 입니다.")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AwsFileService awsFileService;

    @GetMapping
    @Operation(summary = "과제 조회 - 페이지", description = "스터디에 등록된 과제 목록을 페이지로 조회합니다.")
    public ResponseEntity<AssignmentListResponse> getAssignments(
            @RequestParam("studyId") Integer studyId,
            @PageableDefault Pageable pageable){
        AssignmentListResponse assignments = assignmentService.getAssignments(pageable, studyId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{assignmentId}")
    @Operation(summary = "과제 조회 - ID", description = "과제 ID로 과제를 조회합니다.")
    public ResponseEntity<AssignmentResponse> getAssignment(
            @PathVariable Integer assignmentId){
        AssignmentResponse assignment = assignmentService.getAssignment(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    @PostMapping
    @Operation(summary = "생성", description = "과제를 등록합니다.")
    public ResponseEntity<AssignmentCreateResponse> createAssignment(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestBody AssignmentRequest assignmentRequest){

        AssignmentCreateResponse assignmentCreateResponse = assignmentService.create(assignmentRequest, studyId, member);
        return ResponseEntity.ok(assignmentCreateResponse);
    }

    @PutMapping("/{assignmentId}")
    @Operation(summary = "수정", description = "과제를 수정합니다.")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Integer assignmentId,
            @LoginUser Member member,
            @RequestBody AssignmentRequest assignmentRequest){
        return ResponseEntity.ok(
                assignmentService.update(assignmentId, assignmentRequest, member));
    }

    @DeleteMapping("/{assignmentId}")
    @Operation(summary = "삭제", description = "과제를 삭제합니다.")
    public ResponseEntity<Void> deleteAssignment(
            @LoginUser Member member,
            @PathVariable Integer assignmentId){
        assignmentService.delete(assignmentId, member);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/files/{assignmentId}")
    @Operation(summary = "제출", description = "과제 파일을 제출합니다.")
    public ResponseEntity<AssignmentFilesResponse> submitAssignment(
            @LoginUser Member member,
            @PathVariable Integer assignmentId,
            @RequestPart MultipartFile file) throws IOException {

        AssignmentFilesResponse assignmentFilesResponse = assignmentService.uploadAssignments(member, assignmentId, file);
        awsFileService.saveAssignment(file);
        return ResponseEntity.ok(assignmentFilesResponse);
    }

    @GetMapping("/files/{assignmentId}/{memberId}")
    @Operation(summary = "파일 조회 - 회원 ID", description = "해당 과제에 해당 회원이 제출한 과제를 조회합니다.")
    public ResponseEntity<AssignmentFilesResponse> getAssignmentFiles(
            @PathVariable Integer assignmentId,
            @PathVariable Integer memberId
    ){
        AssignmentFilesResponse files = assignmentService.getAssignmentFiles(assignmentId, memberId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/{assignmentId}")
    @Operation(summary = "파일 조회 - 과제 ID", description = "해당 과제에 스터디 멤버들이 제출한 파일을 페이지로 조회합니다.")
    public ResponseEntity<AssignmentFileListResponse> getAllAssignmentFiles(
            @PathVariable Integer assignmentId,
            @PageableDefault Pageable pageable
    ){
        AssignmentFileListResponse files = assignmentService.getAllAssignmentFiles(assignmentId, pageable);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/download/{fileId}")
    @Operation(summary = "과제 파일 다운로드", description = "제출된 과제 파일을 다운로드 합니다.")
    public ResponseEntity<byte[]> downloadAssignment(
            @PathVariable Integer fileId) throws IOException {
        return assignmentService.download(fileId);
    }

}
