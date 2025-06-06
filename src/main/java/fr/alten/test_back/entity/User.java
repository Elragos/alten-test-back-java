package fr.alten.test_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
     * User email, used for authentication.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User encoded password.
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wishlist_id", referencedColumnName = "id")
    private Wishlist wishlist;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

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

    /**
     * Default constructor, mandatory for JPA.
     */
    public User() {
    }

    /**
     * Get user DB ID.
     *
     * @return User DB ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set user DB ID.
     *
     * @param id New user DB ID.
     * @return self
     */
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get user email for Spring authentication.
     *
     * @return User email.
     */
    @Override
    public String getUsername() {
        return getEmail();
    }

    /**
     * Get real user name.
     *
     * @return Real user name.
     */
    public String getRealUsername() {
        return this.username;
    }

    /**
     * Set user name.
     *
     * @param username New user name.
     * @return self
     */
    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get user first name.
     *
     * @return User first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set user first name.
     *
     * @param firstname New user first name.
     * @return self
     */
    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * Get user email.
     *
     * @return User email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user email.
     *
     * @param email New user email.
     * @return self
     */
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get user encoded password, used for authentication.
     *
     * @return User encoded password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Set user encoded password.
     *
     * @param password New user encoded password.
     * @return self
     */
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Get user wishlist.
     *
     * @return User wishlist.
     */
    public Wishlist getWishlist() {
        return wishlist;
    }

    /**
     * Set user wishlist.
     *
     * @param wishlist New user wishlist.
     * @return self
     */
    public User setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
        return this;
    }

    /**
     * Get user creation date.
     *
     * @return User creation date.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Set user creation date.
     *
     * @param createdAt New user creation date.
     * @return self
     */
    public User setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get user last update date.
     *
     * @return User last update date.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set user last update date.
     *
     * @param updatedAt New user last update date.
     * @return self
     */
    public User setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get user roles.
     * 
     * @return User roles
     */
    public List<Role> getRoles(){
        return this.roles;
    }
    
    /**
     * Set user roles.
     *
     * @param roles New user role.
     * @return self
     */
    public User setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    /**
     * Get User expiration state (not used yet).
     *
     * @return <code>true</code> if not expired, <code>false</code> otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Get user lockdown state (not used yet).
     *
     * @return <code>true</code> if not lockdown, <code>false</code> otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Get user credentials expiration state (not used yet).
     *
     * @return <code>true</code> if not expired, <code>false</code> otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Get user activation state (not used yet).
     *
     * @return <code>true</code> if activated, <code>false</code> otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
