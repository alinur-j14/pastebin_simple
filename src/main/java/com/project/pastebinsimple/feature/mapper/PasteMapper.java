package com.project.pastebinsimple.feature.mapper;
import com.project.pastebinsimple.api.dto.response.PasteResponse;
import com.project.pastebinsimple.store.model.Paste;
import com.project.pastebinsimple.store.model.User;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class PasteMapper {

    public Paste toPaste(String url, Instant createdAt, Instant expiration, User user) {
        return Paste
                .builder()
                .createdAt(createdAt)
                .expiresAt(expiration)
                .url(url)
                .user(user)
                .build();
    }

    public PasteResponse toResponse(Paste paste, String url) {
        return PasteResponse
                .builder()
                .id(paste.getId())
                .url(url)
                .createdAt(paste.getCreatedAt())
                .expiresAt(paste.getExpiresAt())
                .build();
    }

}
