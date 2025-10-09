package com.atomz.sawonz.domain.calendar.entity;

import com.atomz.sawonz.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "check_in_at")
    private LocalDateTime checkInAt;

    @Column(name = "check_out_at")
    private LocalDateTime checkOutAt;

    @Column(name = "check_in_ip")
    private String checkInIp;

    @Column(name = "check_out_ip")
    private String checkOutIp;

    @Column(name = "updated")
    private Boolean updated;

    public void punchIn(LocalDateTime now) {
        if (this.checkInAt != null) {
            throw new IllegalStateException("이미 출근이 찍혔습니다.");
        }
        this.checkInAt = now;
//        this.checkInIp = ip;
    }

    public void punchOut(LocalDateTime now) {
        if (this.checkInAt == null) {
            throw new IllegalStateException("출근 기록이 없어 퇴근할 수 없습니다.");
        }
        if (this.checkOutAt != null) {
            throw new IllegalStateException("이미 퇴근이 찍혔습니다.");
        }
        this.checkOutAt = now;
//        this.checkOutIp = ip;
    }


}
