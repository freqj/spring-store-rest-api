package dev.alexa.store.domain;


import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.security.View;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role")
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 60)
    @JsonView({View.RoleView.MinimalView.class, View.UserView.MinimalView.class, View.ResponseView.MinimalView.class})
    private String name;

}
