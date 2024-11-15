package katecam.luvicookie.ditto.domain.assignment.dao;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    Page<Assignment> findAllByStudy(Pageable pageable, Study study);
    List<Assignment> findAllByStudy(Study study);
}
