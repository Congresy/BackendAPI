package tfg.conferencias.gestionconferencias.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tfg.conferencias.gestionconferencias.Domain.User;
import tfg.conferencias.gestionconferencias.Repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
@Api(value = "users", description = "Operations pertaining to users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value ="View a list of all users", response = Iterable.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succesfully retrieved list"),
                           @ApiResponse(code = 401, message = "Not authorized to view the resource"),
                           @ApiResponse(code = 500, message = "Non-existent resource")})
    @GetMapping("/all")
    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users;

    }

    @ApiOperation(value = "Search an User with a name", response = User.class)
    @GetMapping("{name}")
    public User getById(@PathVariable("name") String name){
        return userRepository.findByFirstName(name);
    }

    @ApiOperation(value = "Add a user", response = User.class)
    @PostMapping(value = "/add")
    public User saveProduct(@RequestBody User user){
        return userRepository.save(user);

    }

    @ApiOperation(value = "Update a User")
    @PutMapping(value = "/update/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user){
        User storedUser = userRepository.findById(id);
        storedUser.setFirstName(user.getFirstName());
        storedUser.setLastName(user.getLastName());
        return userRepository.save(storedUser);

    }

    @ApiOperation(value = "Delete a User")
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity delete(@PathVariable String id){
        userRepository.delete(userRepository.findById(id));
        return new ResponseEntity("Product deleted successfully", HttpStatus.OK);

    }
}
