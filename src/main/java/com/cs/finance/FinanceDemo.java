package com.cs.finance;


import com.cs.finance.accessingdatajpa.MultiplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class FinanceDemo {

    private static final Logger log = LoggerFactory.getLogger(FinanceDemo.class);

    public static void main(String[] args) {
        SpringApplication.run(FinanceDemo.class);
    }

    @Bean
    public FinanceAssesment  buildAssesment(MultiplierRepository repository ) {
        log.warn( "Input expected in file: /develop/example_input.txt");

        Path path1 = Paths.get("/develop", "example_input.txt");
        return new FinanceAssesment(repository, path1);
    }

    @Bean
    public CommandLineRunner demo(FinanceAssesment assesment) {
        return (args) -> {
            assesment.instrument1Mean();

            assesment.instrument2NovemberMean();

            assesment.instrument3NovemberMinimum();

            assesment.latestEntries456();

            assesment.multiplierJPA();

        };
    }

}
