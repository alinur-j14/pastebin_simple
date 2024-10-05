package com.project.pastebinsimple.feature.mapper;

import com.project.pastebinsimple.api.dto.response.UserResponse;
import com.project.pastebinsimple.store.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(OAuth2User user) {
        return User
                .builder()
                .oauth2Id(user.getName())
                .username(user.getAttribute("name"))
                .email(user.getAttribute("email"))
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .oauth2Id(user.getOauth2Id())
                .build();
    }

}
