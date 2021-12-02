package com.example.demo_restful.controller;

import com.example.demo_restful.model.User;
import com.example.demo_restful.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final MessageRepository messageRepository;
    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user){
        HashMap<Object, Object> data = new HashMap<>();

        data.put("profile", user);
        data.put("messages", messageRepository.findAll());

        model.addAttribute("frontendData", data);
        return "index";
    }
}
