package com.pad.connectwords.restcontroller;

import com.pad.connectwords.ConnectWordsApplication;
import com.pad.connectwords.Entity.Gameplay;
import com.pad.connectwords.Entity.Player;
import com.pad.connectwords.controller.GamePlayController;
import com.pad.connectwords.service.GameplayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GameplayRestcontroller {
    @Autowired
    GameplayService gameplayService;
    @GetMapping("/newgame")
    public Long newGame(HttpSession session){
        Gameplay gameplay = gameplayService.createNewGame((Player) session.getAttribute("player"));
        return gameplay.getId();
    }
    @GetMapping("/quit-game/{gameId}")
    public boolean quitGame(HttpSession session, @PathVariable("gameId") Long idGame){
        System.out.println("quit game");
        Player player = (Player) session.getAttribute("player");
        Gameplay gameplay = gameplayService.getGameplayById(idGame);

        if (gameplayService.exitGameProcess(gameplay, player)){
            return true;
        };
        return false;
    }

    @GetMapping("/join-game/{id}")
    public boolean joinGame(@PathVariable("id") Long idGame, HttpSession session){
        return gameplayService.joinGame(idGame, session);
    }
}
