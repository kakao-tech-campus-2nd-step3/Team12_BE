package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public StudyListResponse getStudyList(Pageable pageable, StudyCriteria studyCriteria) {
        Page<StudyResponse> studyResponses = studyRepository.findAllByTopicAndNameAndIsOpen(
                studyCriteria.getTopic(),
                studyCriteria.getName(),
                studyCriteria.getIsOpen(),
                pageable)
                .map(StudyResponse::from);
        return StudyListResponse.from(studyResponses);
    }

    public StudyResponse getStudy(Integer studyId) {
        return studyRepository.findById(studyId)
                .map(StudyResponse::from)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
    }

    public void create(StudyCreateRequest request) {
        studyRepository.save(request.toEntity());
    }

    public void delete(Integer studyId) {
        // 조장 검증 코드 필요
        studyRepository.deleteById(studyId);
    }

}