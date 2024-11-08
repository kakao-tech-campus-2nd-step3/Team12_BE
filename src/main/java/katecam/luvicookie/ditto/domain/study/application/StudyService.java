package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final AwsFileService awsFileService;

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

    public void create(Member member, StudyCreateRequest request, MultipartFile profileImage) {
        try {
            // Todo - 스터디 조장 설정
            String imageUrl = awsFileService.saveStudyProfileImage(profileImage);
            studyRepository.save(request.toEntity(imageUrl));
        } catch (IOException exception) {
            throw new GlobalException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public void delete(Member member, Integer studyId) {
        // Todo - 멤버가 해당 스터디의 조장인지 검증
        studyRepository.deleteById(studyId);
    }

}