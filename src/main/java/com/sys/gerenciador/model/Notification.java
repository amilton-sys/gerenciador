package com.sys.gerenciador.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDate createdAt;
    private LocalDate readAt;

    @PrePersist
    private void changeCreatedAt() {
        this.createdAt = LocalDate.now();
    }
}
