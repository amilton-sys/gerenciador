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
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @PrePersist
    public void changeCreatedAt() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void changeUpdatedAt() {
        this.updatedAt = LocalDate.now();
    }

}
