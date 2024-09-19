package maoi.platforme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MaoiPlatformeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaoiPlatformeApplication.class, args);
    }

}
