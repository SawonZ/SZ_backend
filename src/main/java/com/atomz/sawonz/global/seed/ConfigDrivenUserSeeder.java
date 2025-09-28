package com.atomz.sawonz.global.seed;

import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity.Role;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.seed.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ConfigDrivenUserSeeder implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final SeedProperties props;

    @Override
    public void run(String... args) {
        if (!props.isEnabled() || props.getUsers() == null || props.getUsers().isEmpty()) {
            log.info("[UserSeed] disabled or empty -> skip");
            return;
        }
        props.getUsers().forEach(this::createIfNotExists);
    }

    private void createIfNotExists(SeedProperties.SeedUser su) {
        if (usersRepository.existsByEmail(su.getEmail())) {
            log.info("[UserSeed] exists -> {}", su.getEmail());
            return;
        }

        Role role = parseRoleOrDefault(su.getRole(), Role.ROLE_MEMBER);

        UsersEntity user = UsersEntity.builder()
                .email(su.getEmail())
                .passwordHash(passwordEncoder.encode(su.getPassword()))
                .userName(su.getName())
                .status(true)                 // 기본 활성화(정책에 맞게 조정)
                .phone("00000000000")         // NOT NULL 제약 충족용 기본값(정책에 맞게 조정)
                .role(role)                   // 단일 Role 필드에 직접 세팅
                .build();

        usersRepository.save(user);
        log.info("[UserSeed] created -> {} ({})", su.getEmail(), role);
    }

    private Role parseRoleOrDefault(String raw, Role fallback) {
        if (raw == null || raw.isBlank()) return fallback;
        try {
            return Role.valueOf(raw.trim());
        } catch (IllegalArgumentException ex) {
            log.warn("[UserSeed] invalid role '{}', fallback -> {}", raw, fallback);
            return fallback;
        }
    }
}