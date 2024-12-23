package api.lectures;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;

@SpringBootTest
public class MariaDBConnectionTest {

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    void testConnection() {
        databaseClient.sql("SELECT 1")
                .fetch()
                .first()
                .doOnNext(result -> System.out.println("Connection successful: " + result))
                .doOnError(error -> System.err.println("Connection failed: " + error.getMessage()))
                .subscribe();
    }
}