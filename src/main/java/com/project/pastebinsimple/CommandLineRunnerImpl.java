package com.project.pastebinsimple;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Value("${starter.message}")
    private String message;

    @Override
    public void run(String... args) throws Exception {
        log.info("\u001B[32m{}\u001B[0m", message);
    }

}
