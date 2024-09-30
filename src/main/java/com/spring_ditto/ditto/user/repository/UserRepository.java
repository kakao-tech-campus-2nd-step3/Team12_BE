package com.spring_ditto.ditto.user.repository;

import com.spring_ditto.ditto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findBySocialId(String socialId);
}
