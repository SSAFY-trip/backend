package com.ssafy.enjoytrip.login.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class User {
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String name;
    private AuthProvider provider;
    private Role role;
    public void setName(String name) {this.name=name;}
    public void setEmail(String email){this.email=email;}
    private User(String username, String password, String email, String name, AuthProvider provider, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.name=name;
    }

    public static class LocalUserBuilder {
        private String username;
        private String password;
        private Role role;
        private String name;
        public LocalUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LocalUserBuilder password(String password) {
            this.password = encodePassword(password);
            return this;
        }
        public LocalUserBuilder name(String name){
            this.name=name;
            return this;
        }
        public LocalUserBuilder role(Role role) {
            this.role = role;
            return this;
        }
        public User build() {
            return new User(username, password, generateRandomEmail(), name, AuthProvider.LOCAL,  role);
        }
    }

    public static class OAuthUserBuilder {
        private String username;
        private String email;
        private String name;
        private AuthProvider provider;
        private Role role;
        public OAuthUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public OAuthUserBuilder email(String email) {
            this.email = email != null ? email : generateRandomEmail();
            return this;
        }

        public OAuthUserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OAuthUserBuilder provider(AuthProvider provider) {
            this.provider = provider;
            return this;
        }

        public OAuthUserBuilder role(Role role) {
            this.role = role;
            return this;
        }
        public User build() {
            return new User(username, generateSecureRandomPassword(), email, name, provider, role);
        }
    }
    public static LocalUserBuilder localUserBuilder() { return new LocalUserBuilder(); }
    public static OAuthUserBuilder oauthUserBuilder() {
        return new OAuthUserBuilder();
    }
    private static String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
    private static String generateRandomEmail() {
        return UUID.randomUUID().toString() + "@example.com";
    }
    private static String generateSecureRandomPassword() {
        return UUID.randomUUID().toString();
    }
}