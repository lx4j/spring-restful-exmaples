package rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rest.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by L.x on 15-6-1.
 */
@RestController
public class UserController {

    @RequestMapping("/users")
    public List<User> list() {
        return Arrays.asList(new User("zhangsan", "123456"));
    }
}
