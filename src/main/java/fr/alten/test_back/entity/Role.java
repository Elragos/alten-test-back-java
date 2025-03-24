package fr.alten.test_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity representing a user role, defining role the user is allowed to.
 *
 * @author Amarechal
 */
@Entity
public class Role {

    /**
     * Role DB ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Role value.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

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
    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get role value.
     *
     * @return Role value.
     */
    public RoleEnum getRole() {
        return this.role;
    }

    /**
     * Set role value.
     *
     * @param role New role value.
     * @return self
     */
    public Role setRole(RoleEnum role) {
        this.role = role;
        return this;
    }
}
