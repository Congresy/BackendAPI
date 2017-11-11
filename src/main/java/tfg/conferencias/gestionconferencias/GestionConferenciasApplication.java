package tfg.conferencias.gestionconferencias;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tfg.conferencias.gestionconferencias.Domain.Actor;
import tfg.conferencias.gestionconferencias.Repository.ActorRepository;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@Configuration
@ComponentScan
@EnableSwagger2
@EnableAutoConfiguration
public class GestionConferenciasApplication{


	public static void main(String[] args) {
		SpringApplication.run(GestionConferenciasApplication.class, args);
	}

//   @Bean
//   CommandLineRunner init(ActorRepository actorRepository) {
//
//        return args -> {
//
//            actorRepository.deleteAll();
//            Actor actor1 = new Actor("Jose", "Pérez");
//            Actor actor2 = new Actor("Juan", "López");
//            Actor actor3 = new Actor("Alba", "Valverde");
//            Actor actor4 = new Actor("Trini", "Gallardo");
//            List<Actor> actors = new ArrayList<>();
//            actors.add(actor1);
//            actors.add(actor2);
//            actors.add(actor3);
//            actors.add(actor4);
//            actorRepository.save(actors);
//
//        };
//a
//    }

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }
}
