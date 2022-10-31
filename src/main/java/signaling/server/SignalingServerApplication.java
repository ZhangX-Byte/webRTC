package signaling.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SignalingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignalingServerApplication.class, args);
    }

}
