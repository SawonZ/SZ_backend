package com.atomz.sawonz.domain.user.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_private")
public class UserPrivateEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UsersEntity user;

    @Column(name = "address", nullable = false)               // 주소
    private String address;

    @Column(name = "salary", nullable = false)                // 연봉
    private Integer salary;

    @Column(name = "annualLeaveCount", nullable = false)      // 연차(연간 휴가 개수)
    private Integer annualLeaveCount;

    @Column(name = "positionTitle", nullable = false)         // 직급
    private String positionTitle;

    @Column(name = "hiredAt")               // 입사일
    private LocalDate hiredAt;

    @Column(name = "resignedAt")            // 퇴사일
    private LocalDate resignedAt;
}