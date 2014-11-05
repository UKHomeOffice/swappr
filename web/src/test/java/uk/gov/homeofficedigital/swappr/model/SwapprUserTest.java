package uk.gov.homeofficedigital.swappr.model;

import org.junit.Test;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.junit.Assert.*;

public class SwapprUserTest {


    @Test
    public void ensureThatUserIsInRoleShouldReturnTrueIfTheyContainTheSpecifiedRole() {
        // Arrange

        SwapprUser user = make(a(UserMaker.User, with(UserMaker.authorities, UserMaker.adminAuthority)));
        // Act

        boolean isInrole = user.isInRole(Role.ADMIN);
        //Assert

        assertTrue("Expected user to be in Admin Role.",isInrole);
    }

}