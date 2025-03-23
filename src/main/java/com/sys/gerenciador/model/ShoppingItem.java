package com.sys.gerenciador.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString(exclude = {"category", "shopping"})
@EqualsAndHashCode(of = "id")
public class ShoppingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private int quantity;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "shopping_id")
    private Shopping shopping;

    @PrePersist
    private void changeCreatedAt() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    private void changeUpdatedAt() {
        this.updatedAt = LocalDate.now();
    }
}
