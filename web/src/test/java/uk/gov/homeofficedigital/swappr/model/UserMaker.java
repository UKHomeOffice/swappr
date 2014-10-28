package uk.gov.homeofficedigital.swappr.model;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Arrays;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.natpryce.makeiteasy.Property.newProperty;

public class UserMaker {

    public static Property<User, String> username = newProperty();
    public static Property<User, String> password = newProperty();

    public static Instantiator<User> User = (lookup) ->
            new User(lookup.valueOf(username, "default"),
                    lookup.valueOf(password, "password"),
                    new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));

    public static User bill() {
        return make(a(User, with(username, "Bill")));
    }

    public static User ben() {
        return make(a(User, with(username, "Ben")));
    }
}
