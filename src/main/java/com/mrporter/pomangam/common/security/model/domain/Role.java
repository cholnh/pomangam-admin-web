package com.mrporter.pomangam.common.security.model.domain;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Privilege> privileges;

    @Override
    public String getAuthority() {
        return this.name;
    }
}