package uk.gov.homeofficedigital.swappr.model;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SwapMaker {

    public static Property<Swap, String> user = Property.newProperty();
    public static Property<Swap, SwapStatus> status = Property.newProperty();
    public static Property<Swap, Long> id = Property.newProperty();
    public static Property<Swap, List<Swap>> related = Property.newProperty();

    public static Instantiator<Swap> Swap = (lookup) ->
            new Swap(
                    lookup.valueOf(id, 1l),
                    lookup.valueOf(user, "Bill"),
                    LocalDate.now(),
                    ShiftType.B1H,
                    LocalDate.now().plusDays(2),
                    ShiftType.CFH,
                    lookup.valueOf(status, SwapStatus.PROPOSED),
                    lookup.valueOf(related, new ArrayList())
            );
}
