package katecam.luvicookie.ditto.domain.assignment.dao;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
}
