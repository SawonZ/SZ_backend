package com.atomz.sawonz.domain.calendar.dto;

import com.atomz.sawonz.domain.leave.entity.CalendarEntity;
import com.atomz.sawonz.domain.leave.entity.CalendarEntity.CalendarType;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CalendarDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CalendarCreateRequest {

        @NotNull(message = "calendarType은 필수입니다.")
        private String calendarType;

        @NotNull(message = "date는 필수입니다.")
        private LocalDate date;

        @NotNull(message = "startTime은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @NotNull(message = "endTime은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime endTime;

        @NotBlank(message = "status는 필수입니다.")
        private String calendarTitle;

        @Size(max = 500, message = "calendarMemo는 최대 500자까지 가능합니다.")
        private String calendarMemo;

        public static CalendarEntity toEntity(
                UsersEntity usersEntity,
                CalendarType calendarType,
                CalendarCreateRequest calendarCreateRequest
        ) {
            return CalendarEntity.builder()
                    .user(usersEntity)
                    .calendarType(calendarType)
                    .date(calendarCreateRequest.getDate())
                    .startTime(calendarCreateRequest.getStartTime())
                    .endTime(calendarCreateRequest.getEndTime())
                    .status(null)
                    .calendarTitle(calendarCreateRequest.getCalendarTitle())
                    .calendarMemo(calendarCreateRequest.getCalendarMemo())
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CalendarResponse {

        private Long calendarId;
        private String userName;
        private String email;
        private String phone;
        private String positionTitle;
        private String calendarType;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private String calendarTitle;
        private String calendarMemo;
        private Boolean status;

        public static CalendarResponse fromEntity(
                CalendarEntity calendarEntity,
                String calendarType
        ) {
            return CalendarResponse.builder()
                    .calendarId(calendarEntity.getUser().getId())
                    .userName(calendarEntity.getUser().getUserName())
                    .email(calendarEntity.getUser().getEmail())
                    .phone(calendarEntity.getUser().getPhone())
                    .positionTitle(calendarEntity.getUser().getUserPrivate().getPositionTitle())
                    .calendarType(calendarType)
                    .date(calendarEntity.getDate())
                    .startTime(calendarEntity.getStartTime())
                    .endTime(calendarEntity.getEndTime())
                    .calendarTitle(calendarEntity.getCalendarTitle())
                    .calendarMemo(calendarEntity.getCalendarMemo())
                    .status(calendarEntity.getStatus())
                    .build();
        }

    }

}
