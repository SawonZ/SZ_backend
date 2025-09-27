package com.atomz.sawonz.domain.user.entity;

import jakarta.persistence.*;
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
    private String salary;

    @Column(name = "annualLeaveCount", nullable = false)      // 연차(연간 휴가 개수)
    private String annualLeaveCount;

    @Column(name = "positionTitle", nullable = false)         // 직급
    private String positionTitle;

    @Column(name = "hiredAt", nullable = false)               // 입사일
    private String hiredAt;

    @Column(name = "resignedAt", nullable = false)            // 퇴사일
    private String resignedAt;
}