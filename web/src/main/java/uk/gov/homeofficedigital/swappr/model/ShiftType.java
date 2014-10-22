package uk.gov.homeofficedigital.swappr.model;

import static uk.gov.homeofficedigital.swappr.model.ShiftTypeLabel.*;

public enum ShiftType {

    B1H(Early),
    BFH(Early),
    C1H(Mid),
    CFH(Mid),
    S1H(Late),
    T1H(Late);

    private final ShiftTypeLabel label;

    private ShiftType(ShiftTypeLabel label) {
        this.label = label;
    }

    public ShiftTypeLabel getLabel() {
        return label;
    }
}
