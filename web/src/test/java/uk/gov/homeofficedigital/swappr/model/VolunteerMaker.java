package uk.gov.homeofficedigital.swappr.model;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.util.concurrent.atomic.AtomicLong;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static com.natpryce.makeiteasy.Property.newProperty;

public class VolunteerMaker {

    private static AtomicLong idSeq = new AtomicLong(1000);
    public static Property<Volunteer, Long> id = newProperty();
    public static Property<Volunteer, Rota> rota = newProperty();
    public static Property<Volunteer, Offer> offer = newProperty();
    public static Property<Volunteer, VolunteerStatus> status = newProperty();

    public static Instantiator<Volunteer> Volunteer = (lookup) ->
            new Volunteer(lookup.valueOf(id, idSeq.getAndIncrement()),
                    lookup.valueOf(rota, make(a(RotaMaker.Rota, with(RotaMaker.user, UserMaker.bill())))),
                    lookup.valueOf(offer, make(an(OfferMaker.Offer))),
                    lookup.valueOf(status, VolunteerStatus.CREATED));

}
