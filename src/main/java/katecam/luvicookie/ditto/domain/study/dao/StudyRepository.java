package katecam.luvicookie.ditto.domain.study.dao;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Integer> {
    Page<Study> findAllByTopicContainsAndNameContainsAndIsOpenOrIsOpenIsNull(String topic, String name, Boolean isOpen, Pageable pageable);
}
