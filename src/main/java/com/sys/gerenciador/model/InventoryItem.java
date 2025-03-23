package com.sys.gerenciador.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal lastPrice;
    private LocalDate lastPriceUpdate;
    private int minQuantity;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @PrePersist
    private void changeCreatedAt() {
        this.createdAt = LocalDate.now();
    }
    
    @PreUpdate
    private void changeUpdatedAt() {
        this.updatedAt = LocalDate.now();
    }
}
