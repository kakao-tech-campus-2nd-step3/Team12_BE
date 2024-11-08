package katecam.luvicookie.ditto.domain.studymember.application;

import java.util.Arrays;
import java.util.List;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.dao.StudyMemberRepository;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyInviteResponse;
import katecam.luvicookie.ditto.domain.studymember.dto.response.StudyMemberResponse;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;

    public List<StudyMemberResponse> getStudyMemberList(Integer studyId) {
        return studyMemberRepository.findAllByStudyIdAndRoleIn(studyId, Arrays.asList(StudyMemberRole.LEADER, StudyMemberRole.MEMBER))
            .stream()
            .map(StudyMemberResponse::new)
            .toList();
    }

    public StudyMemberResponse updateStudyMember(Integer studyId, Integer memberId, StudyMemberRole role) {
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMember_Id(studyId, memberId);
        studyMember.setRole(role);
        studyMemberRepository.save(studyMember);
        return new StudyMemberResponse(studyMember);
    }

    public void deleteStudyMember(Integer studyId, Integer memberId) {
        studyMemberRepository.deleteByStudyIdAndMember_Id(studyId, memberId);
    }

    public List<StudyMemberResponse> getStudyMemberApplyList(Integer studyId) {
        return studyMemberRepository.findAllByStudyIdAndRoleIn(studyId, List.of(StudyMemberRole.APPLICANT))
            .stream()
            .map(StudyMemberResponse::new)
            .toList();
    }

    public StudyMemberResponse createStudyMember(Integer studyId, Member member, StudyMemberRole role, String message) {
        StudyMember studyMember = StudyMember.builder()
                .studyId(studyId)
                .member(member)
                .role(role)
                .message(message)
                .build();
        studyMemberRepository.save(studyMember);
        return new StudyMemberResponse(studyMember);
    }

    public StudyInviteResponse getStudyInviteToken(Integer studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        return new StudyInviteResponse(studyId, study.getInviteToken());
    }

    public StudyMemberResponse joinStudyMember(Integer studyId, Member member, String token) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        if (!study.getInviteToken().equals(token)) {
            throw new GlobalException(ErrorCode.INVALID_TOKEN);
        }
        return createStudyMember(studyId, member, StudyMemberRole.MEMBER, null);
    }

    public void validateStudyLeader(Integer studyId, Member member) {
        boolean isMember = studyMemberRepository.existsByStudyIdAndMemberAndRoleIn(studyId, member, List.of(StudyMemberRole.LEADER));
        if (!isMember) {
            throw new GlobalException(ErrorCode.NOT_STUDY_LEADER);
        }
    }

    public void validateStudyMember(Integer studyId, Member member) {
        boolean isMember = studyMemberRepository.existsByStudyIdAndMemberAndRoleIn(studyId, member, Arrays.asList(StudyMemberRole.LEADER, StudyMemberRole.MEMBER));
        if (!isMember) {
            throw new GlobalException(ErrorCode.NOT_STUDY_MEMBER);
        }
    }
}
