package katecam.luvicookie.ditto.domain.assignment.api;

import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentFileResponse;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentListResponse;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentRequest;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentResponse;
import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            @RequestParam("studyId") Integer studyId,
            @RequestBody AssignmentRequest assignmentRequest){

        AssignmentCreateResponse assignmentCreateResponse = assignmentService.create(assignmentRequest, studyId);
        return ResponseEntity.ok(assignmentCreateResponse);
    }

    //수정
    @PutMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponse> updateAssignment(@PathVariable Integer assignmentId, @RequestBody AssignmentRequest assignmentRequest){

        return ResponseEntity.ok(
                assignmentService.update(assignmentId, assignmentRequest));
    }

    //삭제
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer assignmentId){

        assignmentService.delete(assignmentId);
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
    @GetMapping("/files/{assignmentId}")
    public ResponseEntity<AssignmentFileResponse> getAssignmentFiles(
            @LoginUser Member member,
            @PathVariable Integer assignmentId
    ){
        AssignmentFileResponse files = assignmentService.getAssignmentFiles(assignmentId, member);
        return ResponseEntity.ok(files);
    }

    //다운로드
    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<byte[]> downloadAssignment(
            @PathVariable Integer fileId) throws IOException {
        return assignmentService.download(fileId);
    }
}
