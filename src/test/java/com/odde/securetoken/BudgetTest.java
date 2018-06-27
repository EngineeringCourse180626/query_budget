package com.odde.securetoken;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.spy;

public class BudgetTest {

    BudgetService service = spy(BudgetService.class);

    @Test
    public void invalid_date() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(0,
                of(2018, 3, 4),
                of(2018, 3, 3));
    }

    @Test
    public void same_date() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(1,
                of(2018, 3, 3),
                of(2018, 3, 3));
    }

    @Test
    public void same_month() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(31,
                of(2018, 3, 1),
                of(2018, 3, 31));
    }

    @Test
    public void partial_month() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(20,
                of(2018, 3, 1),
                of(2018, 3, 20));
    }

    @Test
    public void start_is_before_month_start() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(10,
                of(2018, 2, 20),
                of(2018, 3, 10));
    }

    @Test
    public void end_is_after_month_end() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(12,
                of(2018, 3, 20),
                of(2018, 4, 10));
    }

    @Test
    public void start_is_after_month_end() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(0,
                of(2018, 4, 5),
                of(2018, 4, 10));
    }

    @Test
    public void end_is_before_month_start() {
        givenBudgets(budget("201803", 31));

        assertTotalAmountEquals(0,
                of(2018, 1, 5),
                of(2018, 2, 10));
    }

    @Test
    public void end_is_before_month_start_over_2_months() {
        givenBudgets(budget("201808", 31));

        assertTotalAmountEquals(0,
                of(2018, 1, 5),
                of(2018, 4, 10));
    }

    @Test
    public void cross_month() {
        givenBudgets(
                budget("201803", 31),
                budget("201804", 30 * 2)
        );

        assertTotalAmountEquals(12 + 2 * 10,
                of(2018, 3, 20),
                of(2018, 4, 10));
    }

    @Test
    public void cross_multi_month() {
        givenBudgets(
                budget("201803", 31),
                budget("201805", 62));

        assertTotalAmountEquals(1 + 2,
                of(2018, 3, 31),
                of(2018, 5, 1));
    }

    @Test
    public void cross_year() {
        givenBudgets(
                budget("201712", 31),
                budget("201801", 62),
                budget("201802", 28));

        assertTotalAmountEquals(1 + 62 + 1,
                of(2017, 12, 31),
                of(2018, 2, 1));
    }

    @Test
    public void same_month_but_different_year() {
        givenBudgets(
                budget("201712", 31));

        assertTotalAmountEquals(0,
                of(2018, 12, 10),
                of(2019, 12, 11));
    }

    private void givenBudgets(Budget... budget) {
        List<Budget> list = Arrays.asList(budget);

        Mockito.when(service.findByYearMonth(isA(LocalDate.class), isA(LocalDate.class))).thenReturn(list);
    }

    private void assertTotalAmountEquals(int expected, LocalDate start, LocalDate end) {
        Assert.assertEquals(expected, service.query(start, end));
    }

    private Budget budget(final String theYearAndMonth, final int theAmount) {
        return new Budget() {{
            setYearAndMonth(theYearAndMonth);
            setAmount(theAmount);
        }};
    }


}
