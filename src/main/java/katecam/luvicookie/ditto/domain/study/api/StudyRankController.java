package katecam.luvicookie.ditto.domain.study.api;

import katecam.luvicookie.ditto.domain.study.application.StudyRankService;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyRankListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class StudyRankController {

    private final StudyRankService studyRankService;

    @GetMapping
    public ResponseEntity<StudyRankListResponse> getStudyRankings(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(studyRankService.getStudyRankings(pageable));
    }

}
