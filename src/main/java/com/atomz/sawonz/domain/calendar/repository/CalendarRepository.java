package com.atomz.sawonz.domain.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.atomz.sawonz.domain.leave.entity.CalendarEntity;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

}
