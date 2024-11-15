package katecam.luvicookie.ditto.domain.assignment.dao;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Integer> {
    List<AssignmentFile> findAllByAssignmentAndMember(Assignment assignment, Member member);
    boolean existsByAssignmentAndMemberId(Assignment assignment, Integer memberId);
    Page<AssignmentFile> findAllByAssignment(Pageable pageable, Assignment assignment);
}
