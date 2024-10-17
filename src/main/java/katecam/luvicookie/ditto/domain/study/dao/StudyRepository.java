package katecam.luvicookie.ditto.domain.study.dao;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Integer> {

    @Query("SELECT s FROM Study s WHERE " +
            "(:topic IS NULL OR s.topic LIKE %:topic%) AND " +
            "(:name IS NULL OR s.name LIKE %:name%) AND " +
            "(:isOpen IS NULL OR s.isOpen = :isOpen)")
    Page<Study> findAllByTopicAndNameAndIsOpen(
            @Param("topic") String topic,
            @Param("name") String name,
            @Param("isOpen") Boolean isOpen,
            Pageable pageable
    );

}
