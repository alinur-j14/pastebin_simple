package com.project.pastebinsimple.feature.service;

import com.project.pastebinsimple.api.dto.request.PasteRequest;
import com.project.pastebinsimple.api.dto.response.PasteResponse;
import com.project.pastebinsimple.feature.mapper.PasteMapper;
import com.project.pastebinsimple.feature.util.DateTimeUtil;
import com.project.pastebinsimple.store.model.Paste;
import com.project.pastebinsimple.store.model.User;
import com.project.pastebinsimple.store.repository.PasteRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor
public class PasteService {
    private final PasteRepository repository;
    private final StorageService storageService;
    private final PasteMapper mapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;

    @Value("${domain}")
    private String domain;

    @Transactional
    public void savePaste(PasteRequest pasteRequest, OAuth2User oAuth2User) {
        String fileName = storageService.uploadText(pasteRequest.getText());

        if (repository.existsByUrl(fileName))
            throw new EntityExistsException("Paste with this name already exists");

        Instant now = DateTimeUtil.getCurrentInstant();
        Instant expiration = DateTimeUtil.getInstant(pasteRequest.getDate());
        User user = userService.getUser(oAuth2User.getName());
        Paste paste = mapper.toPaste(fileName, now, expiration, user);

        repository.save(paste);
    }

    @Transactional
    public CompletableFuture<Void> savePasteAsync(PasteRequest pasteRequest, OAuth2User oAuth2User) {
        return storageService.uploadFileAsync(pasteRequest.getText())
                .thenAccept(fileName -> {
                    if (repository.existsByUrl(fileName)) {
                        log.error("Paste with this name already exists: {}", fileName);
                        throw new EntityExistsException("Paste with this name already exists");
                    }

                    Instant now = DateTimeUtil.getCurrentInstant();
                    Instant expiration = DateTimeUtil.getInstant(pasteRequest.getDate());
                    User user = userService.getUser(oAuth2User.getName());

                    Paste paste = mapper.toPaste(fileName, now, expiration, user);
                    repository.save(paste);
                    log.info("Successfully saved paste: {}", fileName);
                })
                .exceptionally(ex -> {
                    log.error("Ошибка при асинхронном сохранении пасты", ex);
                    return null;
                });
    }

    @Transactional(readOnly = true)
    public List<PasteResponse> getPastesByUserOauth2Id(String oauth2Id) {
        List<Paste> pastes = repository.findByUserOauth2Id(oauth2Id);
        List<PasteResponse> pastesResponse = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        pastes.forEach(paste -> {
            builder.append(domain).append(getHashFromPasteId(paste.getId()));
            pastesResponse.add(mapper.toResponse(paste, builder.toString()));
            builder.setLength(0);
        });


        return pastesResponse;
    }

    @Transactional(readOnly = true)
    public String getPasteUrl(String hash) {
        String cache = redisTemplate.opsForValue().get(hash);
        if (cache != null)
            return cache;

        Long id = getIdFromHash(hash);
        Paste paste = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paste with this id does not exist"));

        redisTemplate.opsForValue().set(hash, paste.getUrl());

        return paste.getUrl();
    }

    @Transactional
    public void deletePaste(String url, OAuth2User oAuth2User) {
        String hash = extractLastPathSegmentFromUrl(url);

        Long id = getIdFromHash(hash);
        Paste paste = getPaste(id);

        userService.confirmUser(paste.getUser(), oAuth2User);


        if (Boolean.TRUE.equals(redisTemplate.hasKey(hash)))
            redisTemplate.delete(hash);

        storageService.deleteFile(paste.getUrl());
        repository.delete(paste);
    }

    public String getText(String url) {
        byte[] bytes = storageService.downloadFile(url);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void deleteCache(List<Long> ids) {
        for (Long id : ids) {
            String hash = getHashFromPasteId(id);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(hash)))
                redisTemplate.delete(hash);
        }
    }

    private String extractLastPathSegmentFromUrl(String url) {
        int index = url.lastIndexOf('/');
        return (index != -1) ? url.substring(index + 1) : url;
    }

    private Paste getPaste(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paste with this id does not exist"));
    }

    private void deletePasteWithCache(String hash) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(hash)))
            redisTemplate.delete(hash);

        Long id = getIdFromHash(hash);
        Paste paste = getPaste(id);

        storageService.deleteFile(paste.getUrl());
        repository.delete(paste);
    }

    private String getHashFromPasteId(Long id) {
        return Base64.getEncoder().encodeToString(String.valueOf(id).getBytes());
    }

    private Long getIdFromHash(String hash) {
        try {
            String value = new String(Base64.getDecoder().decode(hash));
            return Long.parseLong(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException ("Incorrect hash", e);
        }

    }

    @Scheduled(cron = "0 */2 * * * *")
    public void cleanUp() {
        Instant now = DateTimeUtil.getCurrentInstant();

        List<Paste> expiredPastes = repository.findByExpiresAtBefore(now);
        expiredPastes.forEach(paste -> deletePasteWithCache(getHashFromPasteId(paste.getId())));

    }

}
