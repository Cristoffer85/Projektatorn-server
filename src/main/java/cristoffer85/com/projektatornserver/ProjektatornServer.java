package cristoffer85.com.projektatornserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjektatornServer {

    public static void main(String[] args) {
        
        SpringApplication.run(ProjektatornServer.class, args);
    }
} 