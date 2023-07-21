package com.pad.connectwords.Entity;

import com.pad.connectwords.ConnectWordsApplication;
import lombok.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Gameplay {
    private Long id;
    private Player host;
    private Player player;
    private String status;
    public static Long generateId(){
        Long idGenerate =  ThreadLocalRandom.current().nextLong(1L, 100000L);
        ConnectWordsApplication.listGameplay.forEach(player -> {
            if (idGenerate == player.getId())
                generateId();
        });
        return idGenerate;
    }
}
