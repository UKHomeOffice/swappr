package uk.gov.homeofficedigital.swappr.controllers;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateDisplay {
    public String month(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public String month(Month month) {
        return month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public String day(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public String fullDate(LocalDate date) { return date.format(DateTimeFormatter.ofPattern("eeee, d MMMM"));}

    public String isoDate(LocalDate date) { return date.format(DateTimeFormatter.ISO_DATE);}
}
