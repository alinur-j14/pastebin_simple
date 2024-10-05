package com.project.pastebinsimple.feature.service;

import com.project.pastebinsimple.api.dto.response.UserResponse;
import com.project.pastebinsimple.feature.event.OnDeleteCacheEvent;
import com.project.pastebinsimple.feature.event.OnDeleteUserEvent;
import com.project.pastebinsimple.feature.mapper.UserMapper;
import com.project.pastebinsimple.store.model.User;
import com.project.pastebinsimple.store.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

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

    public void deleteUser(OAuth2User oAuth2User) {
        User user = getUser(oAuth2User.getName());

        List<String> pasteUrls = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        user.getPastes().forEach(paste -> pasteUrls.add(paste.getUrl()));
        user.getPastes().forEach(paste -> ids.add(paste.getId()));

        eventPublisher.publishEvent(new OnDeleteUserEvent(pasteUrls));
        eventPublisher.publishEvent(new OnDeleteCacheEvent(ids));

        repository.delete(user);
    }

}
