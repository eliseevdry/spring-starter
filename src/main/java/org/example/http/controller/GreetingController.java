package org.example.http.controller;

import org.example.database.entity.Role;
import org.example.dto.UserReadDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@SessionAttributes("user")
public class GreetingController {

    @ModelAttribute("role")
    public List<Role> roles() {
        return List.of(Role.values());
    }

    @GetMapping("/hello")
    public String hello(Model model,
                        @ModelAttribute UserReadDto userReadDto,
                        @RequestParam(required = false) String game) {
        model.addAttribute("user", new UserReadDto(
                1L, "Ivan", LocalDate.now(), "", "", "", Role.USER, null));
        model.addAttribute("game", game);
        return "greeting/hello";
    }

    @GetMapping("/bye")
    public String bye(Model model,
                      @SessionAttribute("user") UserReadDto user) {
        return "greeting/bye";
    }
}
