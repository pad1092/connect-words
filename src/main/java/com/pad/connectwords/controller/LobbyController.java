package com.pad.connectwords.controller;

import com.pad.connectwords.ConnectWordsApplication;
import com.pad.connectwords.Entity.Gameplay;
import com.pad.connectwords.Entity.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LobbyController {
    @GetMapping("/lobby")
    public String lobby(HttpSession session, Model model){
        if (session.getAttribute("player" ) == null)
            return "redirect:/";

        List<Gameplay> listGameplay = ConnectWordsApplication.listGameplay;
        Player player = (Player)session.getAttribute("player");
        model.addAttribute("listGameplay", listGameplay);
        model.addAttribute("player", player);
        return "lobby";
    }
}
