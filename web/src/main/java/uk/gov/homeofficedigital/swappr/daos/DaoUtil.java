package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DaoUtil {

    public static long create(NamedParameterJdbcTemplate jdbcTemplate, String sql, Map<String, Object> args) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder(new ArrayList<>(Arrays.asList(new HashMap<String, Object>() {{
            put("id", Long.class);
        }})));

        jdbcTemplate.update(sql,
                new MapSqlParameterSource(args),
                holder);
        return holder.getKey().longValue();
    }

    public static Map<String, Object> toMap(Object... args) {
        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < args.length; i+= 2) {
            map.put((String) args[i], args[i+1]);
        }
        return map;
    }

}
