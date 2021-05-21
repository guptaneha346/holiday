package com.holiday.bank;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class HolidayServiceImplTest {

    @Mock
    private AwsConfig awsConfig;

    private HolidayServiceImpl holidayService;

    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
        holidayService = new HolidayServiceImpl(awsConfig);
        when(awsConfig.getHolidayList()).thenReturn(getHolidayList());
    }

    @Test
    public void testGetHolidayByDateAndTime() throws Exception {
        final Optional<Holiday> result = holidayService.getHolidayByDateAndTime("31-May-2021", "Asia/Kolkata");
        assertThat(result).isNotEmpty();
        assertThat(result.get().getDate()).isEqualTo("2021-05-31");
        assertThat(result.get().getTimezone().get(0)).isEqualTo("Asia/Kolkata");

    }

    @Test
    public void testGetHolidayByDateAndTimeNotInList() throws Exception {
        final Optional<Holiday> result = holidayService.getHolidayByDateAndTime("12-May-2021", "Asia/Kolkata");
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetHolidayByDate() throws Exception {
        final Optional<Holiday> result = holidayService.getHolidayByDate("31-May-2021");
        assertThat(result).isNotEmpty();
        assertThat(result.get().getDate()).isEqualTo("2021-05-31");
    }


    @Test
    public void testGetNextHoliday() throws Exception {
        final Optional<Holiday> result = holidayService.getNextHoliday();
        assertThat(result).isNotEmpty();
        assertThat(result.get().getDate()).isEqualTo("2021-05-31");
    }

    @Test
    public void testGetHolidayByYear() throws Exception {
        final List<Holiday> result = holidayService.getHolidayByYear("2021", 1, 1);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDate()).isEqualTo("2021-05-31");
    }


    private List<Holiday> getHolidayList() {
        List<Holiday> holidayList = new ArrayList<>();
            Holiday holiday = Holiday.builder()
                    .day("friday")
                    .date(LocalDate.parse("2021-05-31"))
                    .description("Memorial Day")
                    .timezone(Collections.singletonList("Asia/Kolkata"))
                    .build();
         holidayList.add(holiday);
         return holidayList;
    }

}

