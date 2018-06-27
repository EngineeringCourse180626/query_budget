package com.odde.securetoken;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.spy;

public class BudgetTest {

    BudgetService service = spy(BudgetService.class);

    @Test
    public void invalid_date() {
        givenBudgets(new Budget() {{
            setYearAndMonth("201803");
            setAmount(31);
        }});

        assertTotalAmountEquals(0,
                of(2018, 3, 4),
                of(2018, 3, 3));
    }

    @Test
    public void same_date() {
        givenBudgets(new Budget() {{
            setYearAndMonth("201803");
            setAmount(31);
        }});

        assertTotalAmountEquals(1,
                of(2018, 3, 3),
                of(2018, 3, 3));
    }

    @Test
    public void same_month() {
        givenBudgets(new Budget() {{
            setYearAndMonth("201803");
            setAmount(31);
        }});
        LocalDate start = of(2018, 3, 1);
        LocalDate end = of(2018, 3, 31);

        int amount = service.query(start, end);
        Assert.assertEquals(31, amount);
    }

    @Test
    public void partial_month() {
        given();
        LocalDate start = of(2018, 3, 1);
        LocalDate end = of(2018, 3, 20);

        int amount = service.query(start, end);
        Assert.assertEquals(20, amount);
    }

    @Test
    public void cross_month() {
        givenBudgets(
                budget("201803", 31),
                budget("201804", 30 * 2)
        );

        assertTotalAmountEquals(31 + 2 * 2,
                of(2018, 3, 1),
                of(2018, 4, 2));
    }

    private Budget budget(final String theYearAndMonth, final int theAmount) {
        return new Budget(){{
            setYearAndMonth(theYearAndMonth);
            setAmount(theAmount);
        }};
    }

    @Test
    public void cross_multi_month() {
        given();
        LocalDate start = of(2018, 3, 31);
        LocalDate end = of(2018, 5, 1);

        int amount = service.query(start, end);
        Assert.assertEquals(1 + 60 + 2, amount);
    }

    @Test
    public void cross_year() {
        givenCrossYear();
        LocalDate start = of(2017, 12, 31);
        LocalDate end = of(2018, 2, 1);

        int amount = service.query(start, end);
        Assert.assertEquals(1 + 62 + 1, amount);
    }

    private void givenCrossYear() {
        List<Budget> list = Arrays.asList(new Budget(){{
            setYearAndMonth("201712");
            setAmount(31);
        }}, new Budget(){{
            setYearAndMonth("201801");
            setAmount(62);
        }}, new Budget(){{
            setYearAndMonth("201802");
            setAmount(28);
        }});
        Mockito.when(service.findByYearMonth(any(), any())).thenReturn(list);
    }

    void given() {
        List<Budget> list = Arrays.asList(new Budget(){{
            setYearAndMonth("201803");
            setAmount(31);
        }}, new Budget(){{
            setYearAndMonth("201804");
            setAmount(60);
        }}, new Budget() {{
            setYearAndMonth("201805");
            setAmount(62);
        }});

        Mockito.when(service.findByYearMonth(any(), any())).thenReturn(list);
    }

    void givenBudgets(Budget... budget) {
        List<Budget> list = Arrays.asList(budget);

        Mockito.when(service.findByYearMonth(isA(LocalDate.class), isA(LocalDate.class))).thenReturn(list);
    }

    private void assertTotalAmountEquals(int expected, LocalDate start, LocalDate end) {
        Assert.assertEquals(expected, service.query(start, end));
    }

}
