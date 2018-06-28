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
            String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
            Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
                setAmount(0);
            }});
            int monthBudget = budget.getAmount();
            int diffDays = end.getDayOfMonth() - start.getDayOfMonth() + 1;
            return monthBudget / start.lengthOfMonth() * diffDays;
        }

        int total = 0;

        String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
        }});
        total += budget.getAmount() / start.lengthOfMonth() * (start.lengthOfMonth() - start.getDayOfMonth() + 1);

        int fullMonthBudgetAmount = 0;
        LocalDate next = start.withDayOfMonth(1).plusMonths(1);
        while (next.isBefore(end.withDayOfMonth(1))) {
            String str1 = next.format(DateTimeFormatter.ofPattern("yyyyMM"));
            Budget budget1 = list.stream().filter(b -> b.getYearAndMonth().equals(str1)).findFirst().orElse(new Budget() {{
                setAmount(0);
            }});
            fullMonthBudgetAmount += budget1.amount;
            next = next.plusMonths(1);
        }
        total += fullMonthBudgetAmount;

        String str1 = end.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget1 = list.stream().filter(b -> b.getYearAndMonth().equals(str1)).findFirst().orElse(new Budget() {{
            setAmount(0);
        }});
        total += budget1.getAmount() / end.lengthOfMonth() * end.getDayOfMonth();

        return total;
    }

    private boolean isSameMonth(LocalDate start, LocalDate end) {
        return YearMonth.from(start).equals(YearMonth.from(end));
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
