package uk.gov.homeofficedigital.swappr.model;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static com.natpryce.makeiteasy.Property.newProperty;

public class RotaMaker {
    private static AtomicLong idSeq = new AtomicLong(3000);
    public static Property<Rota, Long> id = newProperty();
    public static Property<Rota, User> user = newProperty();
    public static Property<Rota, Shift> shift = newProperty();

    public static Instantiator<Rota> Rota = (lookup) ->
            new Rota(lookup.valueOf(id, idSeq.getAndIncrement()),
                    lookup.valueOf(user, UserMaker.bill()),
                    lookup.valueOf(shift, new Shift(LocalDate.now().plusDays(3), ShiftType.CFH)));

}
