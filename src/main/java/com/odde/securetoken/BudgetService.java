package com.odde.securetoken;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        if (duration.isSameMonth()) {
            Budget startBudget = getBudget(duration.getStart(), list);
            return startBudget.getDailyAmount() * duration.getDays();
        }

        int total = 0;

        Budget startBudget = getBudget(duration.getStart(), list);
        total += startBudget.getDailyAmount() * new Duration(duration.getStart(), startBudget.getDuration().getEnd()).getDays();

        LocalDate next = duration.getStart().withDayOfMonth(1).plusMonths(1);
        while (next.isBefore(duration.getEnd().withDayOfMonth(1))) {
            Budget budget = getBudget(next, list);
            total += budget.getDailyAmount() * budget.getDuration().getDays();
            next = next.plusMonths(1);
        }

        Budget endBudget = getBudget(duration.getEnd(), list);
        total += endBudget.getDailyAmount() * new Duration(endBudget.getDuration().getStart(), duration.getEnd()).getDays();

        return total;
    }

    private Budget getBudget(LocalDate start, List<Budget> list) {
        String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
            setYearAndMonth(str);
        }});
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
