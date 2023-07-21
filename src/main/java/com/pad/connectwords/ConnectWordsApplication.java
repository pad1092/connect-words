package com.pad.connectwords;

import com.pad.connectwords.Entity.Gameplay;
import com.pad.connectwords.Entity.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ConnectWordsApplication {
    public static List<Player> listPlayer;
    public static List<Gameplay> listGameplay;
    public static void main(String[] args) {
        SpringApplication.run(ConnectWordsApplication.class, args);
        listPlayer = new ArrayList<>();
        listGameplay = new ArrayList<>();
    }

}
