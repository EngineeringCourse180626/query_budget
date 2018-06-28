package com.odde.securetoken;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Budget {
    int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }

    String yearAndMonth;

    public LocalDate getFirstDay() {
        return YearMonth.parse(yearAndMonth, DateTimeFormatter.ofPattern("yyyyMM")).atDay(1);
    }
}
