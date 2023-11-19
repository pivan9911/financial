package com.cs.finance.accessingdatajpa;

import java.time.LocalDate;

public record InstrumentRecord(String name, LocalDate date, Double price) {
}
