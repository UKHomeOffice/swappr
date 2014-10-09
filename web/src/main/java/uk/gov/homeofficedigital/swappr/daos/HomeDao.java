package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HomeDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public HomeDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String name() {
        String result = jdbcTemplate.queryForObject("values (current_date )", new HashMap<String, Object>(), String.class);
        return "billy " + result;
    }
}
