package tfg.conferencias.gestionconferencias.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tfg.conferencias.gestionconferencias.Domain.User;
import tfg.conferencias.gestionconferencias.Repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> ppl(){
        List<User> users = userRepository.findAll();
        return users;

    }
}
