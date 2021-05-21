package com.holiday.bank;

import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Optional;

public interface HolidayService {

    Optional<Holiday> getHolidayByDateAndTime(String date, String timeZone) throws ParseException;

    Optional<Holiday> getHolidayByDate(String date) throws ParseException;

    Optional<Holiday> getNextHoliday() throws ParseException;

    List<Holiday> getHolidayByYear(String year, int page, int offset) throws ParseException;
}
