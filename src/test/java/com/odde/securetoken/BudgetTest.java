package com.odde.securetoken;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.spy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BudgetTest {

    BudgetService service = spy(BudgetService.class);

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void invalid_date() throws Exception {
        LocalDate start = LocalDate.of(2018, 3, 4);
        LocalDate end = LocalDate.of(2018, 3, 3);

        int amount = service.query(start, end);
        Assert.assertEquals(0, amount);
    }

    @Test
    public void same_date() throws Exception {
        givenMarch();

        LocalDate start = LocalDate.of(2018, 3, 3);
        LocalDate end = LocalDate.of(2018, 3, 3);

        int amount = service.query(start, end);
        Assert.assertEquals(1, amount);
    }

    @Test
    public void same_month() {
        givenMarch();
        LocalDate start = LocalDate.of(2018, 3, 1);
        LocalDate end = LocalDate.of(2018, 3, 31);

        int amount = service.query(start, end);
        Assert.assertEquals(31, amount);
    }

    @Test
    public void partial_month() {
        givenMarch();
        LocalDate start = LocalDate.of(2018, 3, 1);
        LocalDate end = LocalDate.of(2018, 3, 20);

        int amount = service.query(start, end);
        Assert.assertEquals(20, amount);
    }

    @Test
    public void cross_month() {
        given();
        LocalDate start = LocalDate.of(2018, 3, 1);
        LocalDate end = LocalDate.of(2018, 4, 2);

        int amount = service.query(start, end);
        Assert.assertEquals(31 + 4, amount);
    }

    @Test
    public void cross_multi_month() {
        given();
        LocalDate start = LocalDate.of(2018, 3, 31);
        LocalDate end = LocalDate.of(2018, 5, 1);

        int amount = service.query(start, end);
        Assert.assertEquals(1 + 60 + 2, amount);
    }

    @Test
    public void cross_year() {
        givenCrossYear();
        LocalDate start = LocalDate.of(2017, 12, 31);
        LocalDate end = LocalDate.of(2018, 2, 1);

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

    void givenMarch() {
        List<Budget> list = Arrays.asList(new Budget(){{
            setYearAndMonth("201803");
            setAmount(31);
        }});

        Mockito.when(service.findByYearMonth(isA(LocalDate.class), isA(LocalDate.class))).thenReturn(list);
    }
}
