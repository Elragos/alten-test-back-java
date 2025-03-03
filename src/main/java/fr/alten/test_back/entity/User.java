package fr.alten.test_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity representing an app user.
 *
 * @author AMarechal
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * User DB id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User name.
     */
    @Column(nullable = false)
    private String username;

    /**
     * User first name.
     */
    @Column(nullable = false)
    private String firstname;

    /**
     * User email.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User encoded password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * User creation date.
     */
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * User last update date.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get user email for Spring authentication.
     * @return User email.
     */
    @Override
    public String getUsername() {
        return getEmail();
    }
    
    /**
     * Get user real username.
     * @return User real username.
     */
    public String getRealUsername(){
        return this.username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Getters et Setters
    public User setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
        return true;
    }
}
