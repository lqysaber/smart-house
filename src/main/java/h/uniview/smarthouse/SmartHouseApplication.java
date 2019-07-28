package h.uniview.smarthouse;

import h.uniview.smarthouse.data.PropCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({PropCenter.class})
public class SmartHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHouseApplication.class, args);
    }

}
