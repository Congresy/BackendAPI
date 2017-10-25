package tfg.conferencias.gestionconferencias;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tfg.conferencias.gestionconferencias.Domain.User;
import tfg.conferencias.gestionconferencias.Repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableSwagger2
public class GestionConferenciasApplication{


	public static void main(String[] args) {
		SpringApplication.run(GestionConferenciasApplication.class, args);
	}

   /* @Bean
    CommandLineRunner init(UserRepository userRepository) {

        return args -> {

            userRepository.deleteAll();
            User user1 = new User("Jose", "Pérez");
            User user2 = new User("Juan", "López");
            List<User> users = new ArrayList<>();
            users.add(user1);
            users.add(user2);
            userRepository.save(users);

        };

    }*/

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }
}
