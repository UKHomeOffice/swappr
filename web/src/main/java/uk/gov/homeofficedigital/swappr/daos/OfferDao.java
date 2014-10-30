package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import uk.gov.homeofficedigital.swappr.model.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static uk.gov.homeofficedigital.swappr.daos.DaoUtil.toMap;

public class OfferDao {
    private NamedParameterJdbcTemplate template;
    private final RotaDao rotaDao;

    public OfferDao(NamedParameterJdbcTemplate template, RotaDao rotaDao) {
        this.template = template;
        this.rotaDao = rotaDao;
    }

    public Offer create(Rota swapFrom, Shift swapTo, OfferStatus status) {
        long id = DaoUtil.create(template,
                "insert into offer (rotaId, shiftDate, shiftCode, status) values (:rotaId, :shiftDate, :shiftCode, :status)",
                toMap("rotaId", swapFrom.getId(), "shiftDate", Date.valueOf(swapTo.getDate()), "shiftCode", swapTo.getType().name(), "status", status.name()));
        return new Offer(id, swapFrom, swapTo, status);
    }

    public Optional<Offer> findById(Long offerId) {
        List<Offer> offers = template.query("select * from offer where id = :id", toMap("id", offerId), this::mapOffer);
        if (offers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(offers.get(0));
        }
    }

    public Set<Offer> findByRota(Rota rota) {
        List<Offer> offers = template.query("select * from offer where rotaId = :id", toMap("id", rota.getId()), mapOfferFor(rota));
        return new HashSet<>(offers);
    }

    public Offer updateStatus(Offer offer, OfferStatus status) {
        template.update("update offer set status = :status where id = :id", toMap("status", status.name(), "id", offer.getId()));
        return new Offer(offer.getId(), offer.getSwapFrom(), offer.getSwapTo(), status);
    }

    private Offer mapOffer(ResultSet rs, int idx) throws SQLException {
        Rota rota = rotaDao.findById(rs.getLong("rotaId")).get();
        return new Offer(rs.getLong("id"), rota, new Shift(rs.getDate("shiftDate").toLocalDate(), ShiftType.valueOf(rs.getString("shiftCode"))), OfferStatus.valueOf(rs.getString("status")));
    }

    private RowMapper<Offer> mapOfferFor(Rota rota) {
        return (rs, idx) -> new Offer(rs.getLong("id"), rota, new Shift(rs.getDate("shiftDate").toLocalDate(), ShiftType.valueOf(rs.getString("shiftCode"))), OfferStatus.valueOf(rs.getString("status")));
    }

    public Set<Offer> findOffersBetween(LocalDate start, LocalDate end) {
        List<Offer> offers = template.query("select o.* from offer o join rota r on o.rotaId = r.id where r.shiftDate between :start and :end",
                toMap("start", Date.valueOf(start), "end", Date.valueOf(end)), this::mapOffer);
        return new HashSet<>(offers);
    }
}
