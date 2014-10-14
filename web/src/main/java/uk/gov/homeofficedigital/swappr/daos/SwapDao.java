package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwapDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SwapDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createSwap(Swap swap) {
        Map<String, Object> args = new HashMap<>();
        args.put("username", swap.getUsername());
        args.put("fromDate", Date.valueOf(swap.getFromDate()));
        args.put("fromShift", swap.getFromShift().name());
        args.put("toDate", Date.valueOf(swap.getToDate()));
        args.put("toShift", swap.getToShift().name());

        jdbcTemplate.update("insert into swap (username, fromDate, fromShift, toDate, toShift) values (:username, :fromDate, :fromShift, :toDate, :toShift)", args);
    }

    public List<Swap> findSwapsForUser(String username) {
        Map<String, Object> args = new HashMap<>();
        args.put("username", username);
        String sql = "select * from swap where username = :username";
        return jdbcTemplate.query(sql, args, this::mapSwap);
    }

    private Swap mapSwap(ResultSet rs, int row) throws SQLException {
        return new Swap(rs.getString("username"),
                rs.getDate("fromDate").toLocalDate(),
                ShiftType.valueOf(rs.getString("fromShift")),
                rs.getDate("toDate").toLocalDate(),
                ShiftType.valueOf(rs.getString("toShift")));
    }
}
