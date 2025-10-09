package com.atomz.sawonz.domain.calendar.dto;

import com.atomz.sawonz.domain.calendar.entity.AttendanceEntity;
import java.time.LocalDate;
import java.time.LocalTime;
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
    public static class MyAttendanceResponse {

        private Long attendanceId;
        private LocalDate workDate;
        private LocalTime checkInAt;
        private LocalTime checkOutAt;
        private String checkInIp;
        private String checkOutIp;
        private Boolean updated;

        public static MyAttendanceResponse fromEntity(
                AttendanceEntity attendanceEntity
                ) {
            return MyAttendanceResponse.builder()
                    .attendanceId(attendanceEntity.getAttendanceId())
                    .workDate(attendanceEntity.getWorkDate())
                    .checkInAt(attendanceEntity.getCheckInAt())
                    .checkOutAt(attendanceEntity.getCheckOutAt())
                    .checkInIp(attendanceEntity.getCheckInIp())
                    .checkOutIp(attendanceEntity.getCheckOutIp())
                    .updated(attendanceEntity.getUpdated())
                    .build();
        }
    }
}
