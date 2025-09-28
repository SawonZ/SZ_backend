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
        private String role;
        private String password;
        private String positionTitle;
    }
}