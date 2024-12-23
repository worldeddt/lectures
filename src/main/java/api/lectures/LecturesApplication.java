package api.lectures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "api.lectures")
public class LecturesApplication {

    public static void main(String[] args) {
        SpringApplication.run(LecturesApplication.class, args);
    }

}
