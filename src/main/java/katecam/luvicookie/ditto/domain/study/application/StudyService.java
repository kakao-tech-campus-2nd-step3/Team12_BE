package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
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

    public StudyResponse getStudy(Long studyId) {
        return studyRepository.findById(studyId)
                .map(StudyResponse::toResponse)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
    }

    public void create(StudyCreateRequest request) {
        studyRepository.save(request.toEntity());
    }

    public void delete(Long studyId) {
        // 조장 검증 코드 필요
        studyRepository.deleteById(studyId);
    }

}
