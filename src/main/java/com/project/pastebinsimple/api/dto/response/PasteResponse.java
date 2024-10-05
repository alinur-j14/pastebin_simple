package com.project.pastebinsimple.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasteResponse {

    private Long id;
    private String url;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("expires_at")
    private Instant expiresAt;

}
