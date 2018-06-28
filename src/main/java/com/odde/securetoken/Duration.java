package com.odde.securetoken;

import java.time.LocalDate;
import java.time.Period;

public class Duration {
    private final LocalDate start;
    private final LocalDate end;

    public Duration(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    private int getDays() {
        return Period.between(start, end).getDays() + 1;
    }

    public int getOverlappingDays(Duration anotherDuration) {
        LocalDate overlappingStart = start.isAfter(anotherDuration.start) ? start : anotherDuration.start;
        LocalDate overlappingEnd = end.isBefore(anotherDuration.end) ? end : anotherDuration.end;

        if (overlappingStart.isAfter(overlappingEnd))
            return 0;

        return new Duration(overlappingStart, overlappingEnd).getDays();
    }
}
