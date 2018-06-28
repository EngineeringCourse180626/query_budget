package com.odde.securetoken;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;

public class Duration {
    private final LocalDate start;
    private final LocalDate end;

    public Duration(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public boolean isSameMonth() {
        return YearMonth.from(start).equals(YearMonth.from(end));
    }

    public int getDays() {
        return Period.between(start, end).getDays() + 1;
    }

    public int getOverlappingDays(Duration anotherDuration) {
        LocalDate overlappingStart = start.isAfter(anotherDuration.start) ? start : anotherDuration.start;
        LocalDate overlappingEnd = end.isBefore(anotherDuration.end) ? end : anotherDuration.end;
        return new Duration(overlappingStart, overlappingEnd).getDays();
    }
}
