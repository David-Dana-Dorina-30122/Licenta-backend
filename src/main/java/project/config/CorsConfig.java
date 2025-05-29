package project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Permite acces la toate endpoint-urile
                        .allowedOrigins("http://localhost:4200")  // Permite doar din Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Permite metodele HTTP
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
