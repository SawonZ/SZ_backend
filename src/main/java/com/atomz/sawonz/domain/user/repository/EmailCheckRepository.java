package com.atomz.sawonz.domain.user.repository;

import com.atomz.sawonz.domain.user.entity.EmailCheckEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCheckRepository extends JpaRepository<EmailCheckEntity, Long> {
    Optional<EmailCheckEntity> findByEmail(String email);
    boolean existsByEmailAndVerifiedTrue(String email);
}
