package com.atomz.sawonz.domain.board.repository;

import com.atomz.sawonz.domain.board.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
}
