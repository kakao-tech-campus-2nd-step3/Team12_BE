package katecam.luvicookie.ditto.domain.study.dao;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
