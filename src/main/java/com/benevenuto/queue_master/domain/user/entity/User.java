package com.benevenuto.queue_master.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.benevenuto.queue_master.domain.user.constants.UserRole;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User implements UserDetails {
    private static final long serialVersionUID = 1076916410014315260L;
    
    public User(String name, String email, Integer operator_number, String password, UserRole role, Boolean active) {
        this.name = name;
        this.email = email;
        this.operatorNumber = operator_number;
        this.password = password;
        this.role = role;
        this.active = active != null ? active : true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true)
    private Integer operatorNumber;
    
    @Column(nullable = false, unique = true, length = 180) // Adicionado e-mail
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING) // Garante que salve "ADMIN" no banco e não 0
    @Column(nullable = false, length = 50)
    private UserRole role; 

    @Column(nullable = false)
    private Boolean active = true;

    @Column
    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(this.role.getRoleName()));

        if (this.role == UserRole.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(UserRole.INVENTOR.getRoleName()));
            authorities.add(new SimpleGrantedAuthority(UserRole.OPERATOR.getRoleName()));
        } else if (this.role == UserRole.INVENTOR) {
            authorities.add(new SimpleGrantedAuthority(UserRole.OPERATOR.getRoleName()));
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }
}