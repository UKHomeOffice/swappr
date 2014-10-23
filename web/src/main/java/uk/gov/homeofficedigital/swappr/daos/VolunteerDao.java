package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Volunteer;
import uk.gov.homeofficedigital.swappr.model.VolunteerStatus;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static uk.gov.homeofficedigital.swappr.daos.DaoUtil.toMap;

public class VolunteerDao {

    private NamedParameterJdbcTemplate template;
    private OfferDao offerDao;
    private RotaDao rotaDao;

    public VolunteerDao(NamedParameterJdbcTemplate template, OfferDao offerDao, RotaDao rotaDao) {
        this.template = template;
        this.offerDao = offerDao;
        this.rotaDao = rotaDao;
    }

    public Volunteer create(Rota swapFrom, Offer swapTo, VolunteerStatus status) {
        long id = DaoUtil.create(template,
                "insert into volunteer (rotaId, offerId, status) values (:rotaId, :offerId, :status)",
                toMap("rotaId", swapFrom.getId(), "offerId", swapTo.getId(), "status", status.name()));
        return new Volunteer(id, swapFrom, swapTo, status);
    }

    public Optional<Volunteer> findById(Long volunteerId) {
        List<Volunteer> volunteers = template.query("select * from volunteer where id = :id", toMap("id", volunteerId), this::mapVolunteer);
        if (volunteers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(volunteers.get(0));
        }
    }

    public Set<Volunteer> findByOffer(Offer offer) {
        return new HashSet<>(template.query("select * from volunteer where offerId = :id", toMap("id", offer.getId()), mapVolunteerFor(offer)));
    }

    public Set<Volunteer> findByRota(Rota rota) {
        return new HashSet<>(template.query("select * from volunteer where rotaId = :id", toMap("id", rota.getId()), mapVolunteerFor(rota)));
    }

    public Volunteer updateStatus(Volunteer volunteer, VolunteerStatus requested) {
        template.update("update volunteer set status = :status where id = :id", toMap("status", requested.name(), "id", volunteer.getId()));
        return volunteer.withStatus(requested);
    }

    private Volunteer mapVolunteer(ResultSet rs, int idx) throws SQLException {
        Rota rota = rotaDao.findById(rs.getLong("rotaId")).orElseThrow(() -> new RuntimeException("foreign key constraint broken"));
        Offer offer = offerDao.findById(rs.getLong("offerId")).orElseThrow(() -> new RuntimeException("foreign key constraint broken"));
        return new Volunteer(rs.getLong("id"), rota, offer, VolunteerStatus.valueOf(rs.getString("status")));
    }

    private RowMapper<Volunteer> mapVolunteerFor(Rota rota) {
        return (rs, idx) -> {
            Offer offer = offerDao.findById(rs.getLong("offerId")).orElseThrow(() -> new RuntimeException("foreign key constraint broken"));
            return new Volunteer(rs.getLong("id"), rota, offer, VolunteerStatus.valueOf(rs.getString("status")));
        };
    }

    private RowMapper<Volunteer> mapVolunteerFor(Offer offer) {
        return (rs, idx) -> {
            Rota rota = rotaDao.findById(rs.getLong("rotaId")).orElseThrow(() -> new RuntimeException("foreign key constraint broken"));
            return new Volunteer(rs.getLong("id"), rota, offer, VolunteerStatus.valueOf(rs.getString("status")));
        };
    }

}
