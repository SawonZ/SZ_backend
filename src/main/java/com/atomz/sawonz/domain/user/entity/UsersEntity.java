package com.atomz.sawonz.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", nullable = false, updatable = false)
    private String userName;

    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @Column(name = "status")
    private Boolean status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private UserPrivateEntity userPrivate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_MEMBER;

    @PrePersist
    private void ensureDefaults() {
        if (role == null) role = Role.ROLE_MEMBER;
    }

    public enum Role {
        ROLE_MEMBER, ROLE_MANAGER, ROLE_ADMIN
    }
}
