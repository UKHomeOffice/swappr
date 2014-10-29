package uk.gov.homeofficedigital.swappr.controllers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.homeofficedigital.swappr.controllers.forms.UserForm;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    private final PasswordEncoder encoder;
    private UserDao manager;

    public UserAdminController(UserDao manager, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.manager = manager;
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
        GrantedAuthority auth = new SimpleGrantedAuthority(Role.USER.name());
        manager.createUser(new SwapprUser(userForm.getUsername(), encoder.encode(userForm.getPassword()), Arrays.asList(auth)));
        return "redirect:/";
    }
}