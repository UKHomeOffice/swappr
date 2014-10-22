package uk.gov.homeofficedigital.swappr.controllers.views;

import java.time.LocalDate;
import java.util.Optional;

public class DayView {

    private final LocalDate day;
    private final Optional<RotaView> rotaView;

    public DayView(LocalDate day, Optional<RotaView> rotaView) {
        this.day = day;
        this.rotaView = rotaView;
    }

    public LocalDate getDay() {
        return day;
    }

    public Optional<RotaView> getRotaView() {
        return rotaView;
    }
}
