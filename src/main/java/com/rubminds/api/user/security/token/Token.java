package com.rubminds.api.user.security.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {
    private String token;
    private LocalDateTime expiredAt;

    public Token(String token, LocalDateTime expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public static Token create(String token, Date exp) {
        return new Token(token, Instant.ofEpochMilli(exp.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
