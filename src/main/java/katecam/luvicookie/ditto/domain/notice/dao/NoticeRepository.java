package katecam.luvicookie.ditto.domain.notice.dao;

import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
