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
import uk.gov.homeofficedigital.swappr.controllers.exceptions.UserPasswordResetNotAllowedException;
import uk.gov.homeofficedigital.swappr.controllers.forms.UserForm;
import uk.gov.homeofficedigital.swappr.controllers.forms.UserPasswordResetForm;
import uk.gov.homeofficedigital.swappr.daos.UserDao;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import javax.validation.Valid;
import java.util.Arrays;

import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    private final PasswordEncoder encoder;
    private UserDao manager;

    public UserAdminController(UserDao manager, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.manager = manager;
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String userAdmin(Model model) {
        model.addAttribute("user", new UserForm());
        return "userAdmin";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("user") UserForm userForm, BindingResult result) {
        if (result.hasErrors()) {
            return "userAdmin";
        }
        GrantedAuthority auth = new SimpleGrantedAuthority(Role.USER.name());
        manager.createUser(new SwapprUser(userForm.getUsername(), encoder.encode(userForm.getPassword()), Arrays.asList(auth), userForm.getFullname(), userForm.getEmail()));
        return "redirect:/";
    }


    @RequestMapping(value = "/users/reset-password", method = RequestMethod.GET)
    public String showEditUser(SwapprUser currentUser, Model model) {
        addUserListToModel(currentUser, model);
        model.addAttribute("user", new UserPasswordResetForm());
        return "userAdminPasswordReset";
    }

    private void addUserListToModel(SwapprUser currentUser, Model model) {
        model.addAttribute("userMap", manager.getListOfUsers().stream()
                .filter(item -> {
                    if (currentUser.isInRole(Role.ADMIN)) {
                        return !item.getUsername().equals(currentUser.getUsername());
                    } else {
                        return item.getUsername().equals(currentUser.getUsername());
                    }
                })
                .collect(toMap(SwapprUser::getUsername, SwapprUser::getFullname)));
    }

    @RequestMapping(value = "/users/reset-password", method = RequestMethod.POST)
    public String updatePassword(SwapprUser currentUser, @Valid @ModelAttribute("user") UserPasswordResetForm userForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addUserListToModel(currentUser, model);
            return "userAdminPasswordReset";
        }
        try {
            manager.resetUserPassword(currentUser, userForm.getUsername(), encoder.encode(userForm.getPassword()));
            return "redirect:/";
        } catch (UserPasswordResetNotAllowedException ex) {
            addUserListToModel(currentUser, model);
            return "userAdminPasswordReset";
        }
    }
}