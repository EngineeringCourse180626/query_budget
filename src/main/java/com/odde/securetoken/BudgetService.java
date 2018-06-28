package com.odde.securetoken;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BudgetService {


    public int query(LocalDate start, LocalDate end) {

        List<Budget> list = findByYearMonth(start, end);

        if (end.isBefore(start)) {
            return 0;
        }

        return getTotalAmount(new Duration(start, end), list);
    }

    private int getTotalAmount(Duration duration, List<Budget> list) {
        int total = 0;

        for (Budget budget : list) {
            total += budget.getDailyAmount() * duration.getOverlappingDays(budget.getDuration());
        }

        return total;
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
