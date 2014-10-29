package uk.gov.homeofficedigital.swappr.spring;

import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.util.Optional;

public class VelocitySecurityHelper {

    public Optional<SwapprUser> loggedInUser() {

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).flatMap(auth -> {
            Object principal = auth.getPrincipal();
            if (principal instanceof SwapprUser) {
                return Optional.of((SwapprUser) principal);
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
