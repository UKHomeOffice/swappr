package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;

class UserHelper {

    User userFromPrincipal(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

}