package com.benevenuto.queue_master.domain.user.constants;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    INVENTOR("ROLE_INVENTOR"),
    OPERATOR("ROLE_OPERATOR");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }
}