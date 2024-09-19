package org.remind.melembraai.domain.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.remind.melembraai.domain.validationToken.model.VerifiedToken;
import org.remind.melembraai.utils.BRDatesUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "tb_users")
@Table(name = "tb_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private Boolean isActive;
    private Boolean isVerified;
    private Boolean firstLogin;
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user")
    private Set<VerifiedToken> verifiedTokens;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime lastActivityDate;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now(BRDatesUtils.getBrazilZoneId());
        updateAt = LocalDateTime.now(BRDatesUtils.getBrazilZoneId());
        lastActivityDate = LocalDateTime.now(BRDatesUtils.getBrazilZoneId());
        firstLogin = true;
        isActive = true;
        isVerified = false;
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now(BRDatesUtils.getBrazilZoneId());
        lastActivityDate = LocalDateTime.now(BRDatesUtils.getBrazilZoneId());
    }

    public void setVerified() {
        isVerified = true;
    }
    public void deactivate() {
        isActive = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive();
    }
}
