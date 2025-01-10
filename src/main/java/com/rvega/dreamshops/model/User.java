package com.rvega.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    // ID of the user, auto-generated with a strategy of identity.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User's first name.
    private String firstName;

    // User's last name.
    private String lastName;

    // Email of the user, marked as a Natural ID for unique identification.
    @NaturalId
    private String email;

    // User's password, to be securely stored.
    private String password;

    // The Cart associated with the user. Cascade operations propagate to the Cart entity.
    // Orphan removal means that if the Cart is removed from the User, it will also be deleted.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    // A list of Orders placed by the user. Similar cascade and orphan removal operations apply.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, // Automatically persists related entities when the user is persisted.
            CascadeType.MERGE,   // Synchronizes changes made to related entities.
            CascadeType.DETACH,  // Detaches related entities from the persistence context.
            CascadeType.REFRESH  // Refreshes the state of related entities from the database.
    })
    @JoinTable(
            name = "user_roles", // Specifies the name of the join table that links users and roles.
            joinColumns = @JoinColumn(
                    name = "user_id", // Defines the foreign key column referencing the user's ID in the join table.
                    referencedColumnName = "id"), // Maps to the primary key column in the user table.
                    inverseJoinColumns = @JoinColumn(
                    name = "role_id", // Defines the foreign key column referencing the role's ID in the join table.
                    referencedColumnName = "id" // Maps to the primary key column in the role table.
            )
    )
    private Collection<Role> roles = new HashSet<>();

}
