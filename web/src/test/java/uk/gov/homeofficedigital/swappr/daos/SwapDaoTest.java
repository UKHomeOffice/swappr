package uk.gov.homeofficedigital.swappr.daos;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SwapDaoTest {


    private SwapDao swapDao;
    private JDBCDataSource ds;
    private JdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:mem:noddydb");
        ds.setUser("SA");

        template = new JdbcTemplate(ds);
        executeScript("/schema.sql");
        executeScript("/seed.sql");

        swapDao = new SwapDao(new NamedParameterJdbcTemplate(ds));
    }

    private void executeScript(String classpathOfScript) {
        InputStream inputStream = SwapDaoTest.class.getResourceAsStream(classpathOfScript);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String sql = reader.lines().collect(Collectors.joining("\n"));
        template.execute(sql);
    }

    @After
    public void tearDown() {
        template.execute("drop schema public cascade");
    }

    @Test
    public void createSwap_shouldPersistTheSwapInstance() throws Exception {
        Swap swap = new Swap("Bill", LocalDate.now(), ShiftType.Earlies, LocalDate.now().plusDays(2), ShiftType.Lates);

        swapDao.createSwap(swap);

        List<Swap> swaps = swapDao.findSwapsForUser("Bill");
        assertEquals(1, swaps.size());
        Swap saved = swaps.get(0);
        assertEquals(swap.getUsername(), saved.getUsername());
        assertEquals(swap.getFromDate(), saved.getFromDate());
        assertEquals(swap.getFromShift(), saved.getFromShift());
        assertEquals(swap.getToDate(), saved.getToDate());
        assertEquals(swap.getToShift(), saved.getToShift());
    }
}