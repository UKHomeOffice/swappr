package uk.gov.homeofficedigital.swappr.model;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static com.natpryce.makeiteasy.Property.newProperty;

public class UserMaker {

    public static Property<User, String> username = newProperty();
    public static Property<User, String> password = newProperty();
    public static Property<User, List<GrantedAuthority>> authorities = newProperty();
    public static List<GrantedAuthority> userAuthority = Arrays.asList(new SimpleGrantedAuthority(Role.USER.name()));
    public static List<GrantedAuthority> adminAuthority = Arrays.asList(new SimpleGrantedAuthority(Role.ADMIN.name()));

    public static Instantiator<User> User = (lookup) ->
            new User(lookup.valueOf(username, "default"),
                    lookup.valueOf(password, "password"),
                    lookup.valueOf(authorities, userAuthority));

    public static User bill() {
        return make(a(User, with(username, "Bill")));
    }

    public static User ben() {
        return make(a(User, with(username, "Ben")));
    }
}
