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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final UsersRepository usersRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
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
                calendarEntity,
                calendarCreateRequest.getCalendarType()
        );
    }

    @Transactional(readOnly = true)
    public List<CalendarResponse> listAllCalendars(
            String email,
            String list
    ) {

        final String listMode = (list != null && list.trim().equalsIgnoreCase("me"))
                ? "me"
                : "all";

        List<CalendarEntity> calendarEntities;

        if (Objects.equals(listMode, "me")) {
            if (email == null || email.isBlank()) {
                throw new ErrorException(ResponseCode.BAD_REQUEST, "me 조회 시 email값이 필요합니다.");
            }
            UsersEntity usersEntity = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));
            calendarEntities = calendarRepository.findByUser(usersEntity);
        } else {
            calendarEntities = calendarRepository.findAll();
        }

        List<CalendarResponse> calendarResponseList = new ArrayList<>();

        for(CalendarEntity calendarEntity : calendarEntities) {

            calendarResponseList.add(
                    CalendarResponse.fromEntity(
                            calendarEntity, toString(
                                    calendarEntity.getCalendarType()
                            )
                    )
            );
        }
        return calendarResponseList;
    }

    @Transactional
    public String deleteCalendar(String email, Long calendarId) {
        CalendarEntity calendarEntity = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_CALENDAR));

        if(!calendarEntity.getUser().getEmail().equals(email)) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "일정 삭제는 본인것만 가능합니다.");
        }

        if(calendarEntity.getStatus() != null) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "일정 삭제는 상태가 진행중일때만 가능합니다.");
        }

        calendarRepository.delete(calendarEntity);

        return "삭제 요청이 정상적으로 처리되었습니다";
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

    public static String toString(CalendarType type) {
        if (type == null) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "calendarType이 null 입니다.");
        }
        switch (type) {
            case OUTSIDE_WORK:
                return "outside_work";
            case FULL_REST:
                return "full_rest";
            case AM_REST:
                return "am_rest";
            case PM_REST:
                return "pm_rest";
            case WORKTIME_UPDATE:
                return "worktime_update";
            default:
                // 별칭을 정의하지 않은 enum은 기본 name() 반환
                return type.name();
        }
    }

}
