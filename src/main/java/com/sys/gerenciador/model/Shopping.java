package com.sys.gerenciador.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(of = "id")
public class Shopping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal totalValue;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
    @PrePersist
    private void changeCreatedAt() {
        this.createdAt = LocalDate.now();
    }
    
    @PreUpdate
    private void changeUpdatedAt() {
        this.updatedAt = LocalDate.now();
    }
}
