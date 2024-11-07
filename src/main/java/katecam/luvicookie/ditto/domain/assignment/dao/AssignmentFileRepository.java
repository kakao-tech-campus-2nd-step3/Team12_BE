package katecam.luvicookie.ditto.domain.assignment.dao;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Integer> {
    public List<AssignmentFile> findAllByAssignmentAndMember(Assignment assignment, Member member);
}
