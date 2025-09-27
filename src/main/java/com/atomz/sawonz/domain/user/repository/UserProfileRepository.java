package com.atomz.sawonz.domain.user.repository;

import com.atomz.sawonz.domain.user.entity.UserPrivateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserPrivateEntity, Long> {

}
