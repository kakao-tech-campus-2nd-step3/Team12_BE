package katecam.luvicookie.ditto.domain.assignment.api;

import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentListResponse;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentRequest;
import katecam.luvicookie.ditto.domain.assignment.dto.AssignmentResponse;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
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
    public ResponseEntity<AssignmentListResponse> getAssignments(@PageableDefault Pageable pageable){
        AssignmentListResponse assignments = assignmentService.getAssignments(pageable);
        return ResponseEntity.ok(assignments);
    }

    //아이디로 조회
    @GetMapping("/{assignmentId}")
    public ResponseEntity<?> getAssignment(@PathVariable Integer assignmentId){
        AssignmentResponse assignment = assignmentService.getAssignment(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    //등록
    @PostMapping
    public ResponseEntity<Void> createAssignment(@RequestBody AssignmentRequest assignmentRequest){
        //teammate를 받아와야 함!
        assignmentService.create(assignmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    //수정
    @PutMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponse> updateAssignment(@PathVariable Integer assignmentId, @RequestBody AssignmentRequest assignmentRequest){
        //teammate를 받아와야 함!
        return ResponseEntity.ok(
                assignmentService.update(assignmentId, assignmentRequest));
    }

    //삭제
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer assignmentId){
        //teammate를 받아와야 함!
        assignmentService.delete(assignmentId);
        return ResponseEntity.noContent()
                .build();
    }

    //제출
    @PostMapping("/{assignmentId}")
    public ResponseEntity<?> submitAssignment(@PathVariable Integer assignmentId){
        //과제 제츨 방식 정해야 함
        //어떤 형식으로 제출할 지..
    }
}
