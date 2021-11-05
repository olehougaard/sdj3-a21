package dk.via.web_service;

import dk.via.cars.CarDAO;
import dk.via.cars.ws.Cars;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "dk.via.web_service")
public class WebServiceConfig {
    @Bean
    @Scope("singleton")
    Cars getCars() {
        return new CarDAO();
    }
}