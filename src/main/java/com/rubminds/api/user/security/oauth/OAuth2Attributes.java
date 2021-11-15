package com.rubminds.api.user.security.oauth;

import com.rubminds.api.user.domain.Role;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String oauthId;
    private SignupProvider signupProvider;

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        }
        throw new IllegalArgumentException("올바르지 않은 소셜 로그인 방법입니다!");
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .oauthId((String) attributes.get(userNameAttributeName))
                .signupProvider(SignupProvider.GOOGLE)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .oauthId(String.valueOf(attributes.get("id")))
                .signupProvider(SignupProvider.KAKAO)
                .attributes(attributes)
                .nameAttributeKey("id")
                .build();
    }

    public User toEntity() {
        return User.builder()
                .oauthId(oauthId)
                .role(Role.USER)
                .provider(signupProvider)
                .build();
    }
}
