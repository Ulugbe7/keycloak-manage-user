package uz.zaytun.zaytunuserms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.zaytun.zaytunuserms.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class )
public class ZaytunUserMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaytunUserMsApplication.class, args);
    }

}
