package com.example.setup;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@Slf4j
public class SetupRunner {
    private final ApplicationContext applicationContext;

    @Value("${spring.flyway.schemas}")
    private String schemas;
    @Value("${spring.flyway.url}")
    private String url;
    @Value("${spring.flyway.user}")
    private String user;
    @Value("${spring.flyway.password}")
    private String password;

    public SetupRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void run() {
        List<ApplicationRunner> runners = getApplicationRunners();
        runners.forEach(r-> {
            try {
                r.run(null);
            } catch (Exception e) {
                log.error("Error running application runner {}", r.getClass().getName(), e);
            }
        });

        try {
            runFlywayMigrations();
        } catch (Exception e) {
            log.error("Error running flyway migrations", e);
            System.exit(-1);
        }
    }

    private void runFlywayMigrations() {
        Flyway flyway = Flyway.configure()
                .dataSource(getDataSource())
                .schemas(schemas)
                .load();

        flyway.migrate();
    }

    private List<ApplicationRunner> getApplicationRunners() {
        return List.of(applicationContext.getBeanProvider(ApplicationRunner.class)
                .orderedStream()
                .toArray(ApplicationRunner[]::new));
    }

    private DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
