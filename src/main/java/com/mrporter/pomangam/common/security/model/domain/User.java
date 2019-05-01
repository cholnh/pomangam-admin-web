package com.mrporter.pomangam.common.security.model.domain;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    
    private String nickname;
    private String groupname;
    private String moddate;
    private Integer curTarget;
    
    private List<Role> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
}
