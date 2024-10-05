package com.project.pastebinsimple.feature.service;

import com.project.pastebinsimple.api.dto.response.UserResponse;
import com.project.pastebinsimple.feature.mapper.UserMapper;
import com.project.pastebinsimple.store.model.User;
import com.project.pastebinsimple.store.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public void saveUser(OAuth2User oAuth2User) {
        if (repository.existsByOauth2Id(oAuth2User.getName())) {
            mapper.toEntity(oAuth2User);
            return;
        }

        User user = mapper.toEntity(oAuth2User);

        repository.save(user);
    }

    public UserResponse getUserResponseByOauth2Id(String oauth2Id) {
        User user = repository.findByOauth2Id(oauth2Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapper.toResponse(user);
    }

    public User getUser(String oauth2Id) {
        return repository.findByOauth2Id(oauth2Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void confirmUser(User user, OAuth2User oAuth2User) {
        if (!user.getOauth2Id().equals(oAuth2User.getName()))
            throw new SecurityException("User has not access");
    }

}
