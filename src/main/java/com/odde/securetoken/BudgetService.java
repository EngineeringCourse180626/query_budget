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

        if (YearMonth.from(start).equals(YearMonth.from(end))) {
            return getAmountInMonth(start, end, list);
        }

        return getStartPartialAmount(start, list) + getMiddleMonthsAmount(start, end, list) + getEndPartialAmount(end, list);
    }

    private int getAmountInMonth(LocalDate start, LocalDate end, List<Budget> list) {
        String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
        }});
        int monthBudget = budget.getAmount();
        int diffDays = end.getDayOfMonth() - start.getDayOfMonth() + 1;
        return monthBudget / start.lengthOfMonth() * diffDays;
    }

    private int getMiddleMonthsAmount(LocalDate start, LocalDate end, List<Budget> list) {
        int fullMonthBudgetAmount = 0;
        LocalDate next = start.withDayOfMonth(1).plusMonths(1);
        while (next.isBefore(end.withDayOfMonth(1))) {
            String str = next.format(DateTimeFormatter.ofPattern("yyyyMM"));
            Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
                setAmount(0);
            }});
            fullMonthBudgetAmount += budget.amount;
            next = next.plusMonths(1);
        }
        return fullMonthBudgetAmount;
    }

    private int getEndPartialAmount(LocalDate date, List<Budget> list) {
        String str = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
        }});
        return budget.getAmount() / date.lengthOfMonth() * date.getDayOfMonth();
    }

    private int getStartPartialAmount(LocalDate date, List<Budget> list) {
        String str = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget() {{
            setAmount(0);
        }});
        return budget.getAmount() / date.lengthOfMonth() * (date.lengthOfMonth() - date.getDayOfMonth() + 1);
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
