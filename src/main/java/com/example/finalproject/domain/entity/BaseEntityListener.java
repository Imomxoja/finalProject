package com.example.finalproject.domain.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class BaseEntityListener {
    @PrePersist
    public void prePersist(BaseEntity entity) {

        entity.setCreatedBy(getAuthorizedName());
    }
    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setLastModifiedBy(getAuthorizedName());
    }
    public String getAuthorizedName() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user == null) {
            return "Telegram bot";
        }
        return user.getName();
    }
}
