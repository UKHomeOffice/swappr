package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.Swap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

public class SwapDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SwapDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createSwap(Swap swap) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder(new ArrayList<>(Arrays.asList(new HashMap<String, Object>(){{put("id", Long.class);}})));

        jdbcTemplate.update("insert into shift (username, shiftType, shiftDate) values (:username, :fromShift, :fromDate)",
                new MapSqlParameterSource(toMap("username", swap.getUsername(), "fromShift", swap.getFromShift().name(), "fromDate", Date.valueOf(swap.getFromDate()))),
                holder);
        int shiftId = holder.getKey().intValue();

        jdbcTemplate.update("insert into swap (shiftId, alternateShiftType, alternateShiftDate, status) values (:shiftId, :alternateShiftType, :alternateShiftDate, 'Offered')",
                toMap("shiftId", shiftId, "alternateShiftDate", Date.valueOf(swap.getToDate()), "alternateShiftType", swap.getToShift().name()));
    }

    private Map<String, Object> toMap(Object... args) {
        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < args.length; i+= 2) {
            map.put((String) args[i], args[i+1]);
        }
        return map;
    }

    public List<Swap> findSwapsForUser(String username) {
        Map<String, Object> args = new HashMap<>();
        args.put("username", username);
        String sql = "select * from swap s, shift h where h.username = :username and s.shiftId = h.id";
        return jdbcTemplate.query(sql, args, this::mapSwap);
    }

    private Swap mapSwap(ResultSet rs, int row) throws SQLException {
        return new Swap(rs.getString("username"),
                rs.getDate("shiftDate").toLocalDate(),
                ShiftType.valueOf(rs.getString("shiftType")),
                rs.getDate("alternateShiftDate").toLocalDate(),
                ShiftType.valueOf(rs.getString("alternateShiftType")));
    }

    public List<Swap> findAllSwaps() {
        Map<String, Object> args = new HashMap<>();
        String sql = "select * from swap s, shift h where s.shiftId = h.id";
        return jdbcTemplate.query(sql, args, this::mapSwap);
    }
}
