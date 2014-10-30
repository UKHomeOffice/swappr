package uk.gov.homeofficedigital.swappr.controllers.forms;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
    @NotNull
    @Size(min=3, max=25)
    private String username;

    @Email
    @Size(min=5, max=150)
    private String email;

    @NotNull
    @Size(min=1, max=150)
    private String fullname;

    @NotNull
    @Size(min=4, max=25)
    private String password;
    @NotNull
    @Size(min=4, max=25)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @AssertTrue(message = "Passwords do not match")
    public boolean isMatchingPassword() {
        if (password == null) return true;
        return password.equals(confirmPassword);
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }
}
