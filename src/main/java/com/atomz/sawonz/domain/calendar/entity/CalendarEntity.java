package com.atomz.sawonz.domain.leave.entity;

import com.atomz.sawonz.domain.user.entity.UsersEntity;
import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calendar")
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long calendarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name="calendar_type", nullable = false)
    private CalendarType calendarType;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "calendar_title", nullable = false)
    private String calendarTitle;

    @Column(name = "calendar_memo")
    private String calendarMemo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum CalendarType {
        OUTSIDE_WORK, FULL_REST, AM_REST, PM_REST, WORKTIME_UPDATE
    }

}
