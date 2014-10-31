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

    public static Property<SwapprUser, String> username = newProperty();
    public static Property<SwapprUser, String> password = newProperty();
    public static Property<SwapprUser, String> fullName = newProperty();
    public static Property<SwapprUser, String> emailAddress = newProperty();

    public static Property<SwapprUser, List<GrantedAuthority>> authorities = newProperty();
    public static List<GrantedAuthority> userAuthority = Arrays.asList(new SimpleGrantedAuthority(Role.USER.name()));
    public static List<GrantedAuthority> adminAuthority = Arrays.asList(new SimpleGrantedAuthority(Role.ADMIN.name()));

    public static Instantiator<SwapprUser> User = (lookup) ->
            new SwapprUser(lookup.valueOf(username, "default"),
                    lookup.valueOf(password, "password"),
                    lookup.valueOf(authorities, userAuthority),
                    lookup.valueOf(fullName, "default full name"),
                    lookup.valueOf(emailAddress, "default@mail.com")
                    );

    public static SwapprUser bill() {
        return make(a(User, with(username, "bill"), with(fullName, "Bill Banks")));
    }

    public static SwapprUser ben() {
        return make(a(User, with(username, "ben"), with(fullName, "Ben Benarke")));
    }
}
