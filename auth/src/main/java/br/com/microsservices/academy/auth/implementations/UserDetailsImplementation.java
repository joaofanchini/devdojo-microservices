package br.com.microsservices.academy.auth.implementations;

import br.com.microsservices.academy.core.models.User;
import br.com.microsservices.academy.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Search user with username: '{}'", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User not found with username %s", username)));
        return new CustomUserDetails(user);
    }


    // Como o UserDetails é uma interface com getters, eu consigo fazer com que estendendo o objeto seja possível obter tais funções, como getUsername e getPassword
    private static final class CustomUserDetails extends User implements UserDetails{
        private CustomUserDetails(@NotNull User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_".concat(this.getRole())); // O framework exige que o nome das autorizações se iniciem por ROLE_
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
}
