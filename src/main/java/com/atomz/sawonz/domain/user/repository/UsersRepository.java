package com.atomz.sawonz.domain.user.repository;

import com.atomz.sawonz.domain.user.entity.UsersEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<UsersEntity> findByEmail(String email);
}
