package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import web.models.User;
import web.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@ComponentScan(value = "web")
public class HelloController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Start!");
        messages.add("I'm CRUD application with Spring Security");
        model.addAttribute("messages", messages);
        return "hello";
    }

//    @GetMapping(value = "/user")
//    public String printWelcomeUser(ModelMap model) {
//        List<String> messages = new ArrayList<>();
//        messages.add("Hello User!");
//        messages.add("I'm CRUD application");
//        model.addAttribute("messages", messages);
//        return "hello";
//    }

    @GetMapping(value = "/hi")
    public String printWelcomeAdmin(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello Admin!");
        messages.add("I'm CRUD application");
        model.addAttribute("messages", messages);
        return "hello";
    }

    @GetMapping("/user")
    public String show( Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", userService.getUserByUsername(name)); //Костыль. не знаю как получить
        // id авторизированного пользователя
        return "user";
    }
}