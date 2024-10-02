package katecam.luvicookie.ditto.domain.study.api;

import katecam.luvicookie.ditto.domain.study.application.StudyService;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @GetMapping
    public ResponseEntity<StudyListResponse> getStudyList() {
        return ResponseEntity.ok(studyService.getStudyList());
    }

}
