package uk.gov.homeofficedigital.swappr.daos;

import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RotaDao {

    private Set<Rota> rotas = new HashSet<>();
    private AtomicLong incrementingId = new AtomicLong(0);

    public Rota create(User worker, Shift shift) {
        Rota rota = new Rota(incrementingId.incrementAndGet(), worker, shift);
        rotas.add(rota);
        return rota;
    }

    public Optional<Rota> findById(Long rotaId) {
        return rotas.stream().filter(r -> r.getId().equals(rotaId)).findFirst();
    }

    public Set<Rota> findByWorker(User worker) {
        return rotas.stream().filter(r -> r.getWorker().equals(worker)).collect(Collectors.toSet());
    }
}
