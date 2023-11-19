package com.cs.finance;

import com.cs.finance.accessingdatajpa.Customer;
import com.cs.finance.accessingdatajpa.CustomerRepository;
import com.cs.finance.accessingdatajpa.MultiplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;

@SpringBootApplication
public class FinanceDemo {

    private static final Logger log = LoggerFactory.getLogger(FinanceDemo.class);

    public static void main(String[] args) {
        SpringApplication.run(FinanceDemo.class);
    }


    @Bean
    public FinanceAssesment  buildAssesment(MultiplierRepository repository ) {
        return new FinanceAssesment(repository, Paths.get("/develop", "example_input.txt"));
    }


    @Bean
    public CommandLineRunner demo(CustomerRepository repository, FinanceAssesment assesment) {
        return (args) -> {
            assesment.instrument1Mean();

            assesment.instrument2NovemberMean();

            assesment.instrument3NovemberMinimum();

            assesment.latestEntries456();

            assesment.multipierJPA();

            // save a few customers
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(customer -> {
                log.info(customer.toString());
            });
            log.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findById(1L);
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            log.info("");
        };
    }

}
