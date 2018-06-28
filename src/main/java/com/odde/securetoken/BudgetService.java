package com.odde.securetoken;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetService {


    public int query(LocalDate start, LocalDate end) {

        List<Budget> list = findByYearMonth(start, end);

        if (end.isBefore(start)) {
            return 0;
        }

        if (start.getYear() < end.getYear()) {
            return getStartPartialAmount(start, list) + getMiddleMonthsAmount(start, end, list) + getEndPartialAmount(end, list);
        }

        if (start.getMonth() != end.getMonth()) {
            if (end.getMonthValue() - start.getMonthValue() >= 2) {
                int fullMonthBudgetAmount = 0;
                LocalDate next = start.withDayOfMonth(1).plusMonths(1);
                while (next.isBefore(end.withDayOfMonth(1))) {
                    String str = next.format(DateTimeFormatter.ofPattern("yyyyMM"));
                    Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget(){{
                        setAmount(0);
                    }});
                    fullMonthBudgetAmount += budget.amount;
                    next = next.plusMonths(1);
                }
                return getStartPartialAmount(start, list) + getEndPartialAmount(end, list) + fullMonthBudgetAmount;
            }
            return getStartPartialAmount(start, list) + getEndPartialAmount(end, list);
        }

        if (start.getMonth() == end.getMonth()) {
            String str = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
            Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget(){{
                setAmount(0);
            }});
            int monthBudget = budget.getAmount();
            int diffDays = end.getDayOfMonth() - start.getDayOfMonth() + 1;
            return monthBudget / start.lengthOfMonth() * diffDays;
        }
        if (start.equals(end)) {
            String startString = start.format(DateTimeFormatter.ofPattern("yyyyMM"));
            List<Budget> budgetList = list.stream().filter(b -> b.getYearAndMonth().equals(startString)).collect(Collectors.toList());
            return budgetList.get(0).getAmount() / start.lengthOfMonth();
        }

        return 0;
    }

    private int getMiddleMonthsAmount(LocalDate start, LocalDate end, List<Budget> list) {
        int fullMonthBudgetAmount = 0;
        LocalDate next = start.withDayOfMonth(1).plusMonths(1);
        while (next.isBefore(end.withDayOfMonth(1))) {
            String str = next.format(DateTimeFormatter.ofPattern("yyyyMM"));
            Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget(){{
                setAmount(0);
            }});
            fullMonthBudgetAmount += budget.amount;
            next = next.plusMonths(1);
        }
        return fullMonthBudgetAmount;
    }

    private int getEndPartialAmount(LocalDate date, List<Budget> list) {
        String str = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget(){{
            setAmount(0);
        }});
        return budget.getAmount() / date.lengthOfMonth() * date.getDayOfMonth();
    }

    private int getStartPartialAmount(LocalDate date, List<Budget> list) {
        String str = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Budget budget = list.stream().filter(b -> b.getYearAndMonth().equals(str)).findFirst().orElse(new Budget(){{
            setAmount(0);
        }});
        return budget.getAmount() / date.lengthOfMonth() * (date.lengthOfMonth() - date.getDayOfMonth() + 1);
    }

    protected List<Budget> findByYearMonth(LocalDate start, LocalDate end) {
        return Arrays.asList();
    }
}
