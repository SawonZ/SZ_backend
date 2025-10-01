package com.atomz.sawonz.domain.calendar.service;

import static com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType.AM_REST;
import static com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType.FULL_REST;
import static com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType.OUTSIDE_WORK;
import static com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType.PM_REST;
import static com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType.WORKTIME_UPDATE;

import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarCreateRequest;
import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarResponse;
import com.atomz.sawonz.domain.calendar.repository.CalendarRepository;
import com.atomz.sawonz.domain.leave.entity.CalendarEntity;
import com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final UsersRepository usersRepository;
    private final CalendarRepository calendarRepository;

    public CalendarResponse createCalendar(
            String email,
            CalendarCreateRequest calendarCreateRequest
    ) {
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        CalendarType calendarType = fromString(calendarCreateRequest.getCalendarType());

        CalendarEntity calendarEntity= calendarRepository.save(
                CalendarCreateRequest.toEntity(user, calendarType, calendarCreateRequest));

        return CalendarResponse.fromEntity(
                user,
                calendarEntity,
                calendarCreateRequest.getCalendarType()
        );
    }

    public static CalendarType fromString(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "calendarType이 비어 있습니다.");
        }

        // 필요하면 별칭 매핑
        switch (type) {
            case "outside_work":
                return OUTSIDE_WORK;
            case "full_rest":
                return FULL_REST;
            case "am_rest":
                return AM_REST;
            case "pm_rest":
                return PM_REST;
            case "worktime_update":
                return WORKTIME_UPDATE;
        }
        try {
            return CalendarType.valueOf(type);
        } catch (IllegalArgumentException ex) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "알 수 없는 일정 타입 요청: " + type);
        }
    }
}
