package com.atomz.sawonz.domain.calendar.repository;

import com.atomz.sawonz.domain.calendar.entity.AttendanceEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    Optional<AttendanceEntity> findByUserAndWorkDate(UsersEntity user, LocalDate workDate);
    boolean existsByUserAndWorkDate(UsersEntity user, LocalDate workDate);
}
