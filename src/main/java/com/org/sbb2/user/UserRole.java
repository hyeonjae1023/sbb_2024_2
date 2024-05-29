package com.org.sbb2.user;

import lombok.Getter;

@Getter
public enum UserRole {
    // enum: 자료 열거형.
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
