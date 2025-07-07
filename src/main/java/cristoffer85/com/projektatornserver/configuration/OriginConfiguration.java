package cristoffer85.com.projektatornserver.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class OriginConfiguration implements WebMvcConfigurer {

    @Value("${app.frontend.origin}")
    private String frontendOrigin;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendOrigin)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

/* REMEMBER! To check the / in CORS-config for frontend! */