package com.holiday.bank;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {

    private AwsConfig awsConfig;

    public HolidayServiceImpl(AwsConfig awsConfig) {
        this.awsConfig = awsConfig;
    }


    @Override
    public Optional<Holiday> getHolidayByDateAndTime(String date, String timeZone) throws ParseException {
        final List<Holiday> holidayList = awsConfig.getHolidayList();
        return holidayList.stream()
                .filter(d -> d.getDate().equals(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MMM-yyyy"))))
                .filter(t -> t.getTimezone().stream()
                        .anyMatch(l ->
                                l.equals(timeZone)))
                .findFirst();
    }

    @Override
    public Optional<Holiday> getHolidayByDate(String date) throws ParseException {
        final List<Holiday> holidayList = awsConfig.getHolidayList();
        return holidayList.stream()
                .filter(a -> a.getDate().equals(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MMM-yyyy"))))
                .findFirst();
    }

    @Override
    public Optional<Holiday> getNextHoliday() throws ParseException {
        final List<Holiday> holidayList = awsConfig.getHolidayList();
        return holidayList.stream()
                .filter(a -> a.getDate().isAfter(LocalDate.now()))
                .findFirst();
    }

    @Override
    public List<Holiday> getHolidayByYear(String year, int page, int offset) throws ParseException {
        final List<Holiday> holidayList = awsConfig.getHolidayList();
        holidayList.stream()
               .filter(a -> year.equals(String.valueOf(a.getDate().getYear())))
               .collect(Collectors.toList()) ;
        return holidayList.subList(offset*(page-1), offset*page);
    }
}
