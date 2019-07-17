package h.uniview.smarthouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import h.uniview.smarthouse.data.PropCenter;
import h.uniview.smarthouse.data.UserCenter;

@SpringBootApplication
@EnableConfigurationProperties({PropCenter.class, UserCenter.class})
public class SmartHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHouseApplication.class, args);
    }

}
