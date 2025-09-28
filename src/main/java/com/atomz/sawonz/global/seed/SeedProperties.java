package com.atomz.sawonz.global.seed;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.seed")
public class SeedProperties {
    private boolean enabled = false;
    private List<SeedUser> users;

    @Getter @Setter
    public static class SeedUser {
        private String email;
        private String name;
        private String role;     // 예: ROLE_ADMIN / ROLE_MEMBER / ROLE_SUPPORT
        private String password; // 평문 -> 런타임에서 인코딩
    }
}