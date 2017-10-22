package tfg.conferencias.gestionconferencias;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tfg.conferencias.gestionconferencias.Domain.User;
import tfg.conferencias.gestionconferencias.Repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
@Configuration
@ComponentScan
public class GestionConferenciasApplication{


	public static void main(String[] args) {
		SpringApplication.run(GestionConferenciasApplication.class, args);
	}

    @Bean
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

    }
}
