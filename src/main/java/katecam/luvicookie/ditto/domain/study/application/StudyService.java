package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyUpdateRequest;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
import katecam.luvicookie.ditto.domain.studymember.dao.StudyMemberRepository;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;
    private final AwsFileService awsFileService;
    private final StudyMemberService studyMemberService;
    private final StudyMemberRepository studyMemberRepository;

    public StudyListResponse getStudyList(Pageable pageable, StudyCriteria studyCriteria) {
        Page<StudyResponse> studyResponses = studyRepository.findAllByTopicAndNameAndIsOpen(
                studyCriteria.topic(),
                studyCriteria.name(),
                studyCriteria.isOpen(),
                pageable)
                .map(study -> StudyResponse.from(study, studyMemberService.getStudyLeader(study.getId())));
        System.out.println(studyCriteria.isOpen());
        return StudyListResponse.from(studyResponses);
    }

    public StudyResponse getStudy(Integer studyId) {
        return studyRepository.findById(studyId)
                .map(study -> StudyResponse.from(study, studyMemberService.getStudyLeader(study.getId())))
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
    }

    public List<StudyResponse> getStudyListByMemberId(Integer memberId) {
        return studyMemberRepository.findAllByMember_Id(memberId)
                .stream()
                .map(studyMember -> studyRepository.findById(studyMember.getStudyId())
                        .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND)))
                .map(study -> StudyResponse.from(study, studyMemberService.getStudyLeader(study.getId())))
                .toList();
    }

    @Transactional
    public void create(Member member, StudyCreateRequest request, MultipartFile profileImage) {
        try {
            String imageUrl = awsFileService.saveStudyProfileImage(profileImage);
            Study study = request.toEntity(imageUrl);
            studyRepository.save(study);
            studyMemberService.createStudyMember(study.getId(), member, StudyMemberRole.LEADER, "");
        } catch (IOException exception) {
            throw new GlobalException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void delete(Member member, Integer studyId) {
        studyMemberService.validateStudyLeader(studyId, member);
        studyMemberService.deleteAllStudyMember(studyId);
        studyRepository.deleteById(studyId);
    }

    @Transactional
    public void update(Member member, Integer studyId, StudyUpdateRequest request) {
        studyMemberService.validateStudyLeader(studyId, member);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        Study newStudy = request.toEntity();
        study.update(newStudy);
    }

    @Transactional
    public void updateProfileImage(Member member, Integer studyId, MultipartFile profileImage) {
        studyMemberService.validateStudyLeader(studyId, member);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        try {
            String imageUrl = awsFileService.saveStudyProfileImage(profileImage);
            study.changeProfileImage(imageUrl);
        } catch (IOException exception) {
            throw new GlobalException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

}