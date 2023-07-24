package com.pad.connectwords.controller;

import com.pad.connectwords.ConnectWordsApplication;
import com.pad.connectwords.Entity.Gameplay;
import com.pad.connectwords.Entity.Player;
import com.pad.connectwords.service.GameplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class GamePlayController {
    @Autowired
    GameplayService service;

    @GetMapping("/gameplay/{id}")
    public String gameplay(@PathVariable("id") Long gamePlayId, HttpSession session, Model model){
        Gameplay gameplay = service.getGameplayById(gamePlayId);

        if (session.getAttribute("player" ) == null || gameplay.getId() == null)
            return "redirect:/";

        Player player1 = gameplay.getHost();
        Player player2 = (Player) session.getAttribute("player");

        if (player1.getId() == player2.getId()) {
            player2 = new Player();
            player2.setName("Waiting...");
        }
        else{
            ConnectWordsApplication.listGameplay.forEach(gp -> {
                if (gp.getId() == gamePlayId)
                    gp.setStatus("Full");
            });
        }
        model.addAttribute("player1", player1);
        model.addAttribute("player2", player2);
        model.addAttribute("gameplay", gameplay);
        return "gameplay";
    }

    @GetMapping("/gameplay/{id}/{word}")
    public void sendWords(@PathVariable("id") Long idGameplay, @PathVariable("word") String word, HttpSession session){
        service.handleSendingWords(idGameplay, word, session);

    }
}
