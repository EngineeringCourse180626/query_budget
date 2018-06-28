package com.odde.securetoken;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Budget {
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }

    private String yearAndMonth;

    private LocalDate getFirstDay() {
        return getYearMonth().atDay(1);
    }

    private YearMonth getYearMonth() {
        return YearMonth.parse(yearAndMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private int getDailyAmount() {
        return getAmount() / getFirstDay().lengthOfMonth();
    }

    private LocalDate getLastDay() {
        return getYearMonth().atEndOfMonth();
    }

    private Duration getDuration() {
        return new Duration(getFirstDay(), getLastDay());
    }

    public int getOverlappingAmount(Duration duration) {
        return getDailyAmount() * duration.getOverlappingDays(getDuration());
    }
}
