package com.pad.connectwords.service;

import com.pad.connectwords.ConnectWordsApplication;
import com.pad.connectwords.Entity.Player;
import org.springframework.stereotype.Service;

@Service
public class IndexService {
    public Player createPlayer(String name, String color){
        Player player = new Player(Player.generateId(), name, color);
        ConnectWordsApplication.listPlayer.add(player);
        return player;
    }
}
