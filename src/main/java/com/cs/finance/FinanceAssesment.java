package com.cs.finance;

import com.cs.finance.accessingdatajpa.InstrumentRecord;
import com.cs.finance.accessingdatajpa.MultiplierEntity;
import com.cs.finance.accessingdatajpa.MultiplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FinanceAssesment {


    private static final Logger log = LoggerFactory.getLogger(FinanceAssesment.class);
    MultiplierRepository repository;

    Path pp1 = Paths.get("/develop", "example_input.txt");
    FinanceAssesment(MultiplierRepository pRepository, java.nio.file.Path path1) {
        repository = pRepository;
        pp1 = path1;
    }
    static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");  // 01-Mar-1996

    static InstrumentRecord toInstrument(String line) {
        String[] field = line.split(",");
        LocalDate localDate = LocalDate.parse(field[1], dateTimeFormat);
        Double price = Double.valueOf(field[2]);
        return new InstrumentRecord(field[0], localDate, price);
    }

    public void instrument1Mean() throws IOException {
        Path p1 = Paths.get("/develop", "example_input.txt");
        Double doubleMean = Files.lines(this.pp1)
                .map(FinanceAssesment::toInstrument)
                .filter(i -> i.name().equals("INSTRUMENT1"))
                .map(i -> i.price())
                .collect(Collectors.averagingDouble(Double::doubleValue));

        log.info( "INSTRUMENT1  Mean:" + doubleMean);

    }

    public void instrument2NovemberMean() throws IOException {
        Chronology chrono = IsoChronology.INSTANCE;
        final ChronoLocalDate oct31 = chrono.date(LocalDate.of(2014,10,31));
        final ChronoLocalDate dec01 = chrono.date(LocalDate.of(2014,12,01));

        Double november2014Mean = Files.lines(this.pp1)
                .map(FinanceAssesment::toInstrument)
                .filter(i -> i.name().equals("INSTRUMENT2"))
                .filter(i -> i.date().isAfter(oct31) && i.date().isBefore(dec01) )
                .map(i -> i.price())
                .collect(Collectors.averagingDouble(Double::doubleValue));

        log.info( "INSTRUMENT2  November 2014  Mean:" + november2014Mean);
    }


    public void instrument3NovemberMinimum() throws IOException {
        Chronology chrono = IsoChronology.INSTANCE;
        final ChronoLocalDate oct31 = chrono.date(LocalDate.of(2014,10,31));
        final ChronoLocalDate dec01 = chrono.date(LocalDate.of(2014,12,01));

        Optional<Double> november2014Min = Files.lines(this.pp1)
                .map(FinanceAssesment::toInstrument)
                .filter(i -> i.name().equals("INSTRUMENT3"))
                .filter(i -> i.date().isAfter(oct31) && i.date().isBefore(dec01) )
                .map(i -> i.price())
                .min( (i,j) -> Double.compare(i,j));

        november2014Min.ifPresentOrElse(
                (val)	-> log.info( "INSTRUMENT3  November 2014  Minimum:" + val),
                () -> log.info( "INSTRUMENT3  November 2014  Minimum: NONE" ));
    }


    public void latestEntries456() throws IOException {
        Set<String> smalInstruments = Set.of("INSTRUMENT1", "INSTRUMENT2", "INSTRUMENT3");

        List<String> largeInstruments = Files.lines(this.pp1)
                .map(FinanceAssesment::toInstrument)
                .map( i -> i.name())
                .filter(nm -> !smalInstruments.contains(nm))
                .distinct()
                .collect(Collectors.toList());

        for ( String instr: largeInstruments) {
            List<InstrumentRecord> recordList0 = Files.lines(this.pp1)
                    .map(FinanceAssesment::toInstrument)
                    .filter(i -> i.name().equals(instr))
                    .map(List::of)
                    .reduce(List.of(), FinanceAssesment::latestReducer);

            log.info("Instrument:" + instr + " latest entries");
            recordList0.stream().forEach( (i) -> log.info( "  " + i.name() + " " + i.date().format(dateTimeFormat) + " " + i.price()));
        }

    }


    static private List<InstrumentRecord>  latestReducer(  List<InstrumentRecord> acc, List<InstrumentRecord> newI ) {
        final int ACC_LENGTH = 10;

        Comparator<InstrumentRecord> byDate2 = (me, other) -> {
            Chronology chrono = IsoChronology.INSTANCE;
            final ChronoLocalDate otherCh = chrono.date(other.date());
            return me.date().compareTo(otherCh);
        };

        List<InstrumentRecord> newAcc = Stream.concat(newI.stream(), acc.stream()).sorted(byDate2).limit(ACC_LENGTH).collect(toList());

        return newAcc;
    }


    static private Map<String,Double> multiplierMap = new HashMap<>();

    static private InstrumentRecord multiplierFunc(InstrumentRecord in ) {
        Double mm;
        if ((mm = multiplierMap.get(in.name())) != null) {
            return new InstrumentRecord(in.name(), in.date(), in.price() * mm);
        } else {
            return in;
        }
    };

    public void multipierJPA() throws IOException {

        repository.save(new MultiplierEntity("INSTRUMENT1", 2.0D));
        repository.save(new MultiplierEntity("INSTRUMENT2", 1.5D));

        repository.findAll().forEach(mul -> {
            multiplierMap.put(mul.getName(), mul.getMultiplier());
        });

        Double multiplierMean = Files.lines(this.pp1)
                .map(FinanceAssesment::toInstrument)
                .filter(i -> i.name().equals("INSTRUMENT1"))
                .map(FinanceAssesment::multiplierFunc )
                .map( i -> i.price())
                .collect(Collectors.averagingDouble(Double::doubleValue));

        log.info( "INSTRUMENT1  Multiplier Mean:" + multiplierMean);

    }
}


