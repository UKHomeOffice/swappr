package uk.gov.homeofficedigital.swappr.daos;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.Role;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static uk.gov.homeofficedigital.swappr.model.SwapStatus.*;

public class SwapDaoTest extends SpringIntegrationTest {

    @Autowired
    private SwapDao swapDao;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Test
    public void createSwap_shouldPersistTheSwapInstance() throws Exception {
        User user = createUser();
        Swap swap = new Swap(user.getUsername(), LocalDate.now(), ShiftType.Earlies, LocalDate.now().plusDays(2), ShiftType.Lates, PROPOSED);

        swapDao.createSwap(swap);

        List<Swap> swaps = swapDao.findSwapsForUser(user.getUsername());
        assertEquals(1, swaps.size());
        Swap saved = swaps.get(0);
        assertThat(saved.getId(), greaterThan(1000));
        assertEquals(swap.getUsername(), saved.getUsername());
        assertEquals(swap.getFromDate(), saved.getFromDate());
        assertEquals(swap.getFromShift(), saved.getFromShift());
        assertEquals(swap.getToDate(), saved.getToDate());
        assertEquals(swap.getToShift(), saved.getToShift());
        assertEquals(swap.getStatus(), saved.getStatus());
    }

    @Test
    public void updatingStatusShouldOnlyAffectTheGivenSwap() throws Exception {
        User user = createUser();
        Swap first = new Swap(user.getUsername(), LocalDate.now(), ShiftType.Earlies, LocalDate.now().plusDays(2), ShiftType.Lates, PROPOSED);
        Swap second = new Swap(user.getUsername(), LocalDate.now(), ShiftType.Earlies, LocalDate.now().plusDays(3), ShiftType.Lates, ACCEPTED);

        swapDao.createSwap(first);
        swapDao.createSwap(second);

        List<Swap> savedSwaps = swapDao.findSwapsForUser(user.getUsername());
        assertThat(savedSwaps, hasSize(2));

        Swap proposed = savedSwaps.stream().filter(withStatus(PROPOSED)).findFirst().get();

        swapDao.updateSwapStatus(proposed.getId(), DENIED);

        List<Swap> updatedSwaps = swapDao.findSwapsForUser(user.getUsername());

        assertThat(updatedSwaps, hasSize(2));

        assertThat(updatedSwaps.stream().filter(withStatus(ACCEPTED)).toArray(), arrayWithSize(1));
        assertThat(updatedSwaps.stream().filter(withStatus(DENIED)).toArray(), arrayWithSize(1));
        assertThat(updatedSwaps.stream().filter(withStatus(PROPOSED)).toArray(), arrayWithSize(0));
    }

    private User createUser() {
        User user = new User(RandomStringUtils.random(50), "password", Arrays.asList(new SimpleGrantedAuthority(Role.USER.name())));
        userDetailsManager.createUser(user);
        return user;
    }

    private Predicate<Swap> withStatus(SwapStatus status) {
        return s -> s.getStatus() == status;
    }
}