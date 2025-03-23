package com.sys.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.sys.gerenciador.util.AppConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(exclude = { "user", "category"})
@EqualsAndHashCode(of = "id")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal value;
    private LocalDate date;
    private Boolean isRecurring;
    private Boolean isPaid;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private LocalDate paidDate;
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
    
    public Boolean isRecurrence(){
        return this.isRecurring;
    }
}
