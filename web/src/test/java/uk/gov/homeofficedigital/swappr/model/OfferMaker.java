package uk.gov.homeofficedigital.swappr.model;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.Property.newProperty;

public class OfferMaker {
    private static AtomicLong idSeq = new AtomicLong(2000);
    private static Property<Offer, Long> id = newProperty();
    private static Property<Offer, Rota> rota = newProperty();
    private static Property<Offer, Shift> shift = newProperty();
    private static Property<Offer, OfferStatus> status = newProperty();

    public static Instantiator<Offer> Offer = (lookup) ->
            new Offer(lookup.valueOf(id, idSeq.getAndIncrement()),
                    lookup.valueOf(rota, make(a(RotaMaker.Rota))),
                    lookup.valueOf(shift, new Shift(LocalDate.now().plusDays(5), ShiftType.T1H)),
                    lookup.valueOf(status, OfferStatus.CREATED)
                    );

}
