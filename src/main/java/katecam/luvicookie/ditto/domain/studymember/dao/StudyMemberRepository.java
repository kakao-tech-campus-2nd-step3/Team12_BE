package katecam.luvicookie.ditto.domain.studymember.dao;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {
    List<StudyMember> findAllByStudyIdAndRoleIn(Integer studyId, List<StudyMemberRole> roles);

    List<StudyMember> findAllByMember_Id(Integer memberId);

    Optional<StudyMember> findByStudyIdAndMember_Id(Integer studyId, Integer memberId);

    void deleteByStudyIdAndMember_Id(Integer studyId, Integer memberId);

    boolean existsByStudyIdAndMemberAndRoleIn(Integer studyId, Member member, List<StudyMemberRole> roles);
}
