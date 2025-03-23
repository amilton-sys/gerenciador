package com.sys.gerenciador.model;

import com.sys.gerenciador.util.AppConstant;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private BigDecimal salary = BigDecimal.ZERO;
    private String role = "ROLE_USER";
    private Boolean isEnable = Boolean.TRUE;
    private Boolean accountNonLocked = Boolean.TRUE;
    private Integer failedAttempt = 0;
    private Date lockTime;
    private String resetToken;
    private LocalDate resetTokenExpiry;
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

    public void incrementFailedAttempt() {
        this.failedAttempt++;
    }

    public void lock() {
        this.accountNonLocked = false;
        this.lockTime = new Date();
    }

    public boolean unlockAccountTimeExpired() {
        if (this.lockTime == null) return false;

        long lockTime = this.lockTime.getTime();
        long unLockTime = lockTime + AppConstant.Security.UNLOCK_DURATION_TIME;
        long currentTime = System.currentTimeMillis();

        if (unLockTime < currentTime) {
            this.accountNonLocked = true;
            this.failedAttempt = 0;
            this.lockTime = null;
            return true;
        }
        return false;
    }

    public boolean isDisabled() {
        return !getIsEnable();
    }

    public void resetFailedAttempts() {
        this.failedAttempt = 0;
    }

    public boolean hasFailedAttempts() {
        return this.failedAttempt > 0;
    }

    public void resetToken() {
        this.resetToken = null;
    }

    public void updatePassword(String passwordEncoded) {
        this.password = passwordEncoded;
    }

}
