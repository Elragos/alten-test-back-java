package fr.alten.test_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

/**
 * Entity representing a user authority, defining what role the user has.
 *
 * @author Amarechal
 */
@Entity
public class Authority implements GrantedAuthority {

    /**
     * Authority DB ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Authority value.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthorityEnum authority;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set DB ID.
     *
     * @param id New DB ID.
     * @return self
     */
    public Authority setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getAuthority() {
        return authority.name();
    }

    /**
     * Set authority value.
     *
     * @param authority New authority value.
     * @return self
     */
    public Authority setAuthority(AuthorityEnum authority) {
        this.authority = authority;
        return this;
    }
}
