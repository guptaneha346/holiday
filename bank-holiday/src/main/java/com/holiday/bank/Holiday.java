package com.holiday.bank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class Holiday {
    private  String day;
    private  LocalDate date;
    private  String description;
    private  List<String> timezone;

}
