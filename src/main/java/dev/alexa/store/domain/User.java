package dev.alexa.store.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name ="usr")
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    private String email;
    private String username;
    private String lastName;
    private String firstName;
    private String password;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Item> userItems;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id" , referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "user_role", referencedColumnName = "id"))
    private Set<Role> roles;
}
