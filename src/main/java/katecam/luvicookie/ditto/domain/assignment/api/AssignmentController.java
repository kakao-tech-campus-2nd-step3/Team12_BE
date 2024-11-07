package katecam.luvicookie.ditto.domain.assignment.api;

import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentListResponse;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentRequest;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
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
    public ResponseEntity<AssignmentResponse> getAssignment(@PathVariable Integer assignmentId){
        AssignmentResponse assignment = assignmentService.getAssignment(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    //등록
    @PostMapping
    public ResponseEntity<Void> createAssignment(
            @RequestParam("studyId") Integer studyId,
            @RequestBody AssignmentRequest assignmentRequest){

        assignmentService.create(assignmentRequest, studyId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
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
    /*@PostMapping("/{assignmentId}")
    public ResponseEntity<?> submitAssignment(@PathVariable Integer assignmentId){

    }*/
}
