package com.railmate.authservice.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/** Simple JPA entity representing a role. */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Store enum value as a string column (“USER”, “ADMIN”). */
    @Enumerated(EnumType.STRING)
    private RoleName name;

    /* ---------- constructors ---------- */

    public Role() { }                         // JPA needs this

    public Role(RoleName name) {              //  FIX  used by bootstrap & tests
        this.name = name;
    }

    /* ---------- getters / setters ---------- */

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public RoleName getName() {               //  FIX  getter now exists for compiler
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
