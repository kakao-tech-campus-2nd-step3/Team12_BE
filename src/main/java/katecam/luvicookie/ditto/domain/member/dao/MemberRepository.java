package katecam.luvicookie.ditto.domain.member.dao;


import katecam.luvicookie.ditto.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);
}
