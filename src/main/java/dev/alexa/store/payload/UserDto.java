package dev.alexa.store.payload;

import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.domain.Role;
import dev.alexa.store.security.View;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserDto {
    @JsonView({View.UserView.MinimalView.class, View.ResponseView.class})
    private Long id;
    @NotEmpty
    @Size(min = 2)
    @JsonView({View.UserView.MinimalView.class,  View.ResponseView.class})
    private String username;
    @JsonView({View.UserView.MinimalView.class, View.ResponseView.class})
    private Set<Role> roles;

    @NotEmpty
    @JsonView(View.UserView.FullInfo.class)
    private String email;
    @JsonView(View.UserView.FullInfo.class)
    private String lastName;
    @JsonView(View.UserView.FullInfo.class)
    private String firstName;

}
