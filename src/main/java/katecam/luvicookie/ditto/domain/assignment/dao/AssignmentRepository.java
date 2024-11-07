package katecam.luvicookie.ditto.domain.assignment.dao;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    public Page<Assignment> findAllByStudy(Pageable pageable, Study study);
}
