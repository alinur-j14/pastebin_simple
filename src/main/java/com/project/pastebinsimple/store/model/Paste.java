package com.project.pastebinsimple.store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Instant;

@Entity
@Table(name = "pastes")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paste {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paste_sequence")
    @SequenceGenerator(name = "paste_sequence", sequenceName = "paste_seq", allocationSize = 10)
    private Long id;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
