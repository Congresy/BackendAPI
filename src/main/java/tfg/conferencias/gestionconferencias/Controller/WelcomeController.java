package tfg.conferencias.gestionconferencias.Controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {


    @RequestMapping("/")
    public ModelAndView welcome(){
        return new ModelAndView("redirect:/swagger-ui.html");
    }

}
