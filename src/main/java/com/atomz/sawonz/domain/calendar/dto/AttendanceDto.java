package com.atomz.sawonz.domain.calendar.dto;

import com.atomz.sawonz.domain.calendar.entity.AttendanceEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AttendanceDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendanceCreateResponse {

        String msg;

        public static AttendanceEntity toEntity(
                UsersEntity usersEntity,
                LocalDate today,
                LocalDateTime now
                ) {
            return AttendanceEntity.builder()
                    .user(usersEntity)
                    .workDate(today)
                    .checkInAt(now)
                    .updated(false)
                    .build();
        }
    }
}
