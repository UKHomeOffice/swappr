package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.forms.UserForm;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    private final PasswordEncoder encoder;
    private UserDetailsManager manager;

    public UserAdminController(UserDetailsService manager, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.manager = (UserDetailsManager) manager;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String userAdmin(Model model) {
        model.addAttribute("user", new UserForm());
        return "userAdmin";
    }

    @RequestMapping(value="/users/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("user") UserForm userForm, BindingResult result) {
        if (result.hasErrors()) {
            return "userAdmin";
        }
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_USER");
        manager.createUser(new User(userForm.getUsername(), encoder.encode(userForm.getPassword()), Arrays.asList(auth)));
        return "redirect:/";
    }
}
