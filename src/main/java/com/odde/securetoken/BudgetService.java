package com.odde.securetoken;

import java.time.LocalDate;
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
            int monthBudget = startBudget.getAmount();
            int diffDays = end.getDayOfMonth() - start.getDayOfMonth() + 1;
            return monthBudget / start.lengthOfMonth() * diffDays;
        }

        int total = 0;

        Budget startBudget = getBudget(start, list);
        total += startBudget.getAmount() / start.lengthOfMonth() * (start.lengthOfMonth() - start.getDayOfMonth() + 1);

        int fullMonthBudgetAmount = 0;
        LocalDate next = start.withDayOfMonth(1).plusMonths(1);
        while (next.isBefore(end.withDayOfMonth(1))) {
            Budget budget = getBudget(next, list);
            fullMonthBudgetAmount += budget.amount;
            next = next.plusMonths(1);
        }
        total += fullMonthBudgetAmount;

        Budget endBudget = getBudget(end, list);
        total += endBudget.getAmount() / end.lengthOfMonth() * end.getDayOfMonth();

        return total;
    }

    private Budget getBudget(LocalDate start, List<Budget> list) {
        String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
        }});
    }

    private boolean isSameMonth(LocalDate start, LocalDate end) {
        return YearMonth.from(start).equals(YearMonth.from(end));
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
