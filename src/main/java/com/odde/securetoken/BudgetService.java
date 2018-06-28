package com.odde.securetoken;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class BudgetService {


    public int query(LocalDate start, LocalDate end) {

        List<Budget> list = findByYearMonth(start, end);

        if (end.isBefore(start)) {
            return 0;
        }

        if (isSameMonth(start, end)) {
            Budget startBudget = getBudget(start, list);
            return startBudget.getDailyAmount() * (Period.between(start, end).getDays() + 1);
        }

        int total = 0;

        Budget startBudget = getBudget(start, list);
        total += startBudget.getDailyAmount() * (Period.between(start, startBudget.getLastDay()).getDays() + 1);

        LocalDate next = start.withDayOfMonth(1).plusMonths(1);
        while (next.isBefore(end.withDayOfMonth(1))) {
            Budget budget = getBudget(next, list);
            total += budget.getDailyAmount() * (Period.between(budget.getFirstDay(), budget.getLastDay()).getDays() + 1);
            next = next.plusMonths(1);
        }

        Budget endBudget = getBudget(end, list);
        total += endBudget.getDailyAmount() * (Period.between(endBudget.getFirstDay(), end).getDays() + 1);

        return total;
    }

    private Budget getBudget(LocalDate start, List<Budget> list) {
        String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
            setYearAndMonth(str);
        }});
    }

    private boolean isSameMonth(LocalDate start, LocalDate end) {
        return YearMonth.from(start).equals(YearMonth.from(end));
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
