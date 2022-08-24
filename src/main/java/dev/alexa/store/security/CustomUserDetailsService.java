package dev.alexa.store.security;

import dev.alexa.store.domain.Role;
import dev.alexa.store.domain.User;
import dev.alexa.store.payload.CurrentUser;
import dev.alexa.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User not found with %s credentials", usernameOrEmail)));
        CurrentUser currentUser = new CurrentUser(user.getId(), user.getEmail(), user.getPassword(), true, true, true, true, mapRolesToAuthorities(user.getRoles()));

        return currentUser;
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roleSet)
    {
        return roleSet.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
