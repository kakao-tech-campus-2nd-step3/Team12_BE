package katecam.luvicookie.ditto.domain.studymember.dao;

import jakarta.transaction.Transactional;
import java.util.List;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {

    List<StudyMember> findAllByStudyIdAndRoleIn(Integer studyId, List<StudyMemberRole> roles);

    @Query("SELECT sm FROM StudyMember sm WHERE sm.studyId = :studyId AND sm.member.id = :memberId")
    StudyMember findByStudyIdAndMemberId(Integer studyId, Integer memberId);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudyMember sm WHERE sm.studyId = :studyId AND sm.member.id = :memberId")
    void deleteByStudyIdAndMemberId(Integer studyId, Integer memberId);

    boolean existsByStudyIdAndMemberAndRoleIn(Integer studyId, Member member, List<StudyMemberRole> roles);
}
