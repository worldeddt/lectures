package api.lectures.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;


@Configuration
public class R2dbcConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "mariadb")
                        .option(HOST, "localhost")
                        .option(PORT, 3306)
                        .option(USER, "root")
                        .option(PASSWORD, "eddy")
                        .option(DATABASE, "lecture")
                        .build()
        );
    }
}