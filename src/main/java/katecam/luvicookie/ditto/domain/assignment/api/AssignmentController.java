package katecam.luvicookie.ditto.domain.assignment.api;

import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.assignment.dto.response.*;
import katecam.luvicookie.ditto.domain.assignment.dto.request.AssignmentRequest;
import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AwsFileService awsFileService;

    //전체 조회
    @GetMapping
    public ResponseEntity<AssignmentListResponse> getAssignments(
            @RequestParam("studyId") Integer studyId,
            @PageableDefault Pageable pageable){
        AssignmentListResponse assignments = assignmentService.getAssignments(pageable, studyId);
        return ResponseEntity.ok(assignments);
    }

    //아이디로 조회
    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponse> getAssignment(
            @PathVariable Integer assignmentId){
        AssignmentResponse assignment = assignmentService.getAssignment(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    //등록
    @PostMapping
    public ResponseEntity<AssignmentCreateResponse> createAssignment(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestBody AssignmentRequest assignmentRequest){

        AssignmentCreateResponse assignmentCreateResponse = assignmentService.create(assignmentRequest, studyId, member);
        return ResponseEntity.ok(assignmentCreateResponse);
    }

    //수정
    @PutMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Integer assignmentId,
            @LoginUser Member member,
            @RequestBody AssignmentRequest assignmentRequest){
        return ResponseEntity.ok(
                assignmentService.update(assignmentId, assignmentRequest, member));
    }

    //삭제
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(
            @LoginUser Member member,
            @PathVariable Integer assignmentId){
        assignmentService.delete(assignmentId, member);
        return ResponseEntity.noContent()
                .build();
    }

    //제출
    @PostMapping("/files/{assignmentId}")
    public ResponseEntity<AssignmentFileResponse> submitAssignment(
            @LoginUser Member member,
            @PathVariable Integer assignmentId,
            @RequestPart MultipartFile file) throws IOException {

        AssignmentFileResponse assignmentFileResponse = assignmentService.uploadAssignments(member, assignmentId, file);
        awsFileService.saveAssignment(file);
        return ResponseEntity.ok(assignmentFileResponse);
    }

    //현재 과제에 제출한 파일 목록
    @GetMapping("/files/{assignmentId}/{memberId}")
    public ResponseEntity<AssignmentFileResponse> getAssignmentFiles(
            @PathVariable Integer assignmentId,
            @PathVariable Integer memberId
    ){
        AssignmentFileResponse files = assignmentService.getAssignmentFiles(assignmentId, memberId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/{assignmentId}")
    public ResponseEntity<AssignmentFileListResponse> getAllAssignmentFiles(
            @PathVariable Integer assignmentId,
            @LoginUser Member member,
            @PageableDefault Pageable pageable
    ){
        AssignmentFileListResponse files = assignmentService.getAllAssignmentFiles(assignmentId, member, pageable);
        return ResponseEntity.ok(files);
    }

    //다운로드
    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<byte[]> downloadAssignment(
            @PathVariable Integer fileId) throws IOException {
        return assignmentService.download(fileId);
    }

    @GetMapping("files/rate")
    public ResponseEntity<Void> getRate(){
        log.info(String.valueOf(assignmentService.getSubmissionRate(11)));
        log.info(String.valueOf(assignmentService.getTotalAssignmentCount(11)));
        return ResponseEntity.noContent()
                .build();
    }

}
