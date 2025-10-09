package com.atomz.sawonz.domain.calendar.service;

import com.atomz.sawonz.domain.calendar.entity.AttendanceEntity;
import com.atomz.sawonz.domain.calendar.repository.AttendanceRepository;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UsersRepository usersRepository;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public String checkIn(String email) {
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        LocalDate today = LocalDate.now(KST);
        LocalDateTime now = LocalDateTime.now(KST);

        if (attendanceRepository.existsByUserAndWorkDate(user, today)) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "이미 오늘 출근이 처리되었습니다.");
        }

        AttendanceEntity attendanceEntity =  AttendanceEntity.builder()
                                                .user(user)
                                                .workDate(today)
                                                .checkInAt(now)
                                                .updated(false)
                                                .build();

        attendanceRepository.save(attendanceEntity);

        return today + " 출근 처리가 완료되었습니다.";
    }

    @Transactional
    public String checkOut(String email) {
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        LocalDate today = LocalDate.now(KST);
        LocalDateTime now = LocalDateTime.now(KST);

        AttendanceEntity attendanceEntity = attendanceRepository.findByUserAndWorkDate(user, today)
                .orElseThrow(() -> new ErrorException(ResponseCode.BAD_REQUEST, "오늘 출근 기록이 없습니다."));

        if (attendanceEntity.getCheckOutAt() != null) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "이미 오늘 퇴근이 처리되었습니다.");
        }

        attendanceEntity.setCheckOutAt(now);

        return today + " 퇴근 처리가 완료되었습니다.";
    }
}
