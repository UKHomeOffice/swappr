package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DateDisplayTest {

    private DateDisplay display = new DateDisplay();

    @Test
    public void fullDate_shouldConvertALocalDateToADateString() throws Exception {
        assertEquals("Thursday, 25 December", display.fullDate(LocalDate.of(2014, 12, 25)));
    }

    @Test
    public void day_shouldDisplayTheDayName_givenALocalDate() throws Exception {
        assertEquals("Thursday", display.day(LocalDate.of(2014, 12, 25)));
    }

    @Test
    public void month_shouldDisplayTheMonthName_givenALocalDate() throws Exception {
        assertEquals("December", display.month(LocalDate.of(2014, 12, 25)));
    }
}