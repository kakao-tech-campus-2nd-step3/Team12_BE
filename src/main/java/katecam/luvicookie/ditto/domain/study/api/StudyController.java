package katecam.luvicookie.ditto.domain.study.api;

import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.study.application.StudyService;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @GetMapping
    public ResponseEntity<StudyListResponse> getStudyList() {
        return ResponseEntity.ok(studyService.getStudyList());
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyResponse> getStudy(@PathVariable Integer studyId) {
        return ResponseEntity.ok(studyService.getStudy(studyId));
    }

    // 회원 로그인 검증 필요
    @PostMapping
    public ResponseEntity<Void> createStudy(@Valid StudyCreateRequest request) {
        studyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    // 회원 로그인 검증 필요
    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> deleteStudy(@PathVariable Integer studyId) {
        studyService.delete(studyId);
        return ResponseEntity.noContent()
                .build();
    }

}
