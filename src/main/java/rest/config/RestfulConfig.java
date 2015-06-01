package rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import rest.controller.UserController;

/**
 * Created by L.x on 15-6-1.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = UserController.class)
public class RestfulConfig {

}
