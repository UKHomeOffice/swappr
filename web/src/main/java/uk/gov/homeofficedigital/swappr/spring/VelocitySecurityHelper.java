package uk.gov.homeofficedigital.swappr.spring;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.model.Role;

import java.util.Optional;

public class VelocitySecurityHelper {

    public Optional<User> loggedInUser() {

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).flatMap(auth -> {
            Object principal = auth.getPrincipal();
            if (principal instanceof User) {
                return Optional.of((User) principal);
            } else return Optional.empty();
        });

    }

    public boolean hasAuthority(final Role role) {
        return loggedInUser()
                .map(u -> u.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(role.name())))
                .orElse(false);
    }

    public boolean isAdmin() {
        return hasAuthority(Role.ADMIN);
    }
}
