package com.odde.securetoken;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BudgetService {

    public int query(LocalDate start, LocalDate end) {
        return findByYearMonth(start, end).stream()
                .mapToInt(budget -> budget.getOverlappingAmount(new Duration(start, end)))
                .sum();
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
