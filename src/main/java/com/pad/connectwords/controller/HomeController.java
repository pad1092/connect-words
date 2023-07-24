package com.pad.connectwords.controller;

import com.pad.connectwords.Entity.Player;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session){
        Player p = (Player)session.getAttribute("player");
        System.out.println(p == null);
        if (session.getAttribute("player") == null)
            return "index";
        return "redirect:/lobby";
    }
}
