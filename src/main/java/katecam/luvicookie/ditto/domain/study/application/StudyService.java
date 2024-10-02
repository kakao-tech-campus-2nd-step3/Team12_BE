package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public StudyListResponse getStudyList() {
        List<Study> studies = studyRepository.findAll();
        List<StudyResponse> studyResponses = studies.stream()
                .map(StudyResponse::toResponse)
                .toList();
        return StudyListResponse.toResponse(studyResponses);
    }

}
