package com.kilopo.vape;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VapeApplication {

    public static void main(String[] args) {
//        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/vapedb", "postgres", "root").load();
//        flyway.migrate();
        SpringApplication.run(VapeApplication.class, args);
    }
}
