package api.lectures.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class DbConfig implements ApplicationListener<ApplicationReadyEvent> {
    private final DatabaseClient databaseClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        // reactor: publisher, subscriber
//        databaseClient.sql("SELECT 1").fetch().one()
//                .subscribe(
//                        success -> {
//                            log.info("Initialize r2dbc database connection.");
//                        },
//                        error -> {
//                            log.error("Failed to initialize r2dbc database connection.");
//                            SpringApplication.exit(event.getApplicationContext(), () -> -110);
//                        }
//                );

        databaseClient.sql("SELECT 1")
                .fetch()
                .first()
                .doOnNext(result -> System.out.println("Connection successful: " + result))
                .doOnError(error -> System.err.println("Connection failed: " + error.getMessage()))
                .subscribe(
                        success -> {
                            log.info("Initialize r2dbc database connection.");
                        },
                        error -> {
                            log.error("Failed to initialize r2dbc database connection.");
                            SpringApplication.exit(event.getApplicationContext(), () -> -110);
                        }
                );
    }
}

