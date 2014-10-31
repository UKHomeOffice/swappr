package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;
import uk.gov.homeofficedigital.swappr.model.ShiftType;
import uk.gov.homeofficedigital.swappr.model.SwapprUser;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static uk.gov.homeofficedigital.swappr.daos.DaoUtil.toMap;

public class RotaDao {
    private NamedParameterJdbcTemplate template;
    private final UserDetailsService userService;

    public RotaDao(NamedParameterJdbcTemplate template, UserDetailsService userService) {
        this.template = template;
        this.userService = userService;
    }

    public Rota create(SwapprUser worker, Shift shift) {
        long id = DaoUtil.create(template,
                "insert into rota (username, shiftDate, shiftCode) values (:username, :shiftDate, :shiftCode)",
                toMap("username", worker.getUsername(), "shiftDate", Date.valueOf(shift.getDate()), "shiftCode", shift.getType().name()));
        return new Rota(id, worker, shift);
    }

    @Transactional
    public void create(SwapprUser worker, Shift from, Shift to) {
        for(LocalDate date = from.getDate(); date.isBefore(to.getDate().plusDays(1)); date = date.plusDays(1)) {
            create(worker, new Shift(date, from.getType()));
        }
    }

    public Rota findOrCreate(SwapprUser worker, Shift shift) {
        List<Long> id = template.query("select id from rota where username = :user and shiftDate = :shiftDate and shiftCode = :shiftCode",
                toMap("user", worker.getUsername(), "shiftDate", Date.valueOf(shift.getDate()), "shiftCode", shift.getType().name()), (rs, idx) -> rs.getLong("id"));
        if (!id.isEmpty()) {
            return new Rota(id.get(0), worker, shift);
        } else {
            return create(worker, shift);
        }
    }

    public Optional<Rota> findById(Long rotaId) {
        List<Rota> rotas = template.query("select * from rota where id = :id", toMap("id", rotaId), this::mapRota);
        if (rotas.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(rotas.get(0));
        }
    }

    public Set<Rota> findByWorker(SwapprUser worker) {
        List<Rota> rota = template.query("select * from rota where username = :username and shiftDate between :start and :end",
                toMap("username", worker.getUsername(), "start", Date.valueOf(LocalDate.now()), "end", Date.valueOf(LocalDate.now().plusMonths(2))), this::mapRota);
        return new HashSet<>(rota);
    }

    public Set<Rota> findAll() {
        List<Rota> rota = template.query("select * from rota where shiftDate between :start and :end", toMap("start", Date.valueOf(LocalDate.now()), "end", Date.valueOf(LocalDate.now().plusMonths(2))), this::mapRota);
        return new HashSet<>(rota);
    }

    private Rota mapRota(ResultSet rs, int row) throws SQLException {
        SwapprUser user = (SwapprUser) userService.loadUserByUsername(rs.getString("username"));
        Shift shift = new Shift(rs.getDate("shiftDate").toLocalDate(), ShiftType.valueOf(rs.getString("shiftCode")));
        return new Rota(rs.getLong("id"), user, shift);
    }
}
