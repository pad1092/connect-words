package com.pad.connectwords.service;

import com.pad.connectwords.ConnectWordsApplication;
import com.pad.connectwords.Entity.Gameplay;
import com.pad.connectwords.Entity.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GameplayService {
    public Gameplay createNewGame(Player hostPlayer){
        Gameplay gameplay = new Gameplay (Gameplay.generateId(),  hostPlayer, null, "Waiting");
        ConnectWordsApplication.listGameplay.add(gameplay);
        return gameplay;
    }

    public Gameplay getGameplayById(Long id){
        Gameplay res = new Gameplay();
        System.out.println("ID REQUETS: " + id);
        for (Gameplay gameplay : ConnectWordsApplication.listGameplay){
            System.out.println("GAMEPLAY: " + gameplay.getId());
            if (gameplay.getId().equals(id)) {
                res = gameplay;
            }
        }
        return res;
    }
    public boolean exitGameProcess(Gameplay gameplay, Player exitPlayer){
        Player host = gameplay.getHost();
        Player player = gameplay.getPlayer();

//        if exit player not in the game, then do not anything
        if (!checkPlayerOfGame(exitPlayer, gameplay))
            return false;

        ConnectWordsApplication.listGameplay.remove(gameplay);
        return true;
    }

    public boolean joinGame(Long id, HttpSession session){
        Gameplay gameplay =getGameplayById(id);
        if (gameplay.getId() == null)
            return false;
        if (gameplay.getPlayer() != null && gameplay.getPlayer() != null)
            return false;
        gameplay.setPlayer((Player) session.getAttribute("player"));
        gameplay.setStatus("Full");
        return true;
    }

    private boolean checkPlayerOfGame(Player player, Gameplay gameplay){
        if (player == gameplay.getPlayer() || player == gameplay.getHost())
            return true;
        return false;
    }

}
