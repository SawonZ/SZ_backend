package com.atomz.sawonz.domain.calendar.repository;

import com.atomz.sawonz.domain.leave.entity.CalendarEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    List<CalendarEntity> findByUser(UsersEntity user);
}
